package no.fint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import org.json.JSONObject;

import io.github.kobakei.materialfabspeeddial.FabSpeedDial;

public class MainActivity extends AppCompatActivity {
    Student student;
    School school;
    SharedPreferences mainActivitySharedPreferences;
    SharedPreferences.Editor editor;
    RequestQueue queue;
    String personHREF;
    String studentRelationHREF;
    String schoolHREF;
    boolean isPersonDataReceived, isSchoolDataReceived;
    TextView studentNameTextView;
    TextView studentBirthDateTextView;
    TextView studentScoolNameTextView;
    ImageView studentProfilePicture;
    Response.ErrorListener errorListener;
    LinearLayout linearLayoutStudentProof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivitySharedPreferences = getSharedPreferences(FintStudentAppSharedPreferences.sharedPreferencesMainKey, MODE_PRIVATE);
        editor = mainActivitySharedPreferences.edit();
        student = new Student();
        school = new School();
        student.setSchool(school);
        queue = Volley.newRequestQueue(this);
        studentNameTextView = findViewById(R.id.main_student_name_text_view);
        studentBirthDateTextView = findViewById(R.id.main_student_birth_date_text_view);
        studentScoolNameTextView = findViewById(R.id.main_student_school_text_view);
        studentProfilePicture = findViewById(R.id.front_page_student_picture);
        linearLayoutStudentProof = findViewById(R.id.student_proof_text_linear_layout);
        if (!mainActivitySharedPreferences.getBoolean(FintStudentAppSharedPreferences.isLoggedIn, false)) {
            Intent logInIntent = new Intent(this, LogInActivity.class);
            startActivity(logInIntent);
        }
        else{
            String username = (getIntent().hasExtra("Brukernavn")) ? getIntent().getExtras().getString("Brukernavn") : mainActivitySharedPreferences.getString(FintStudentAppSharedPreferences.username,"no username");
            if (username.equals("no username")){
                editor.putBoolean(FintStudentAppSharedPreferences.isLoggedIn, false);
                editor.apply();
                Intent logInIntent = new Intent(this, LogInActivity.class);
                startActivity(logInIntent);
            }
            editor.putString(FintStudentAppSharedPreferences.username, username);
            editor.apply();
            errorListener = getErrorListener();
            queue.add(getStudentData(username)).setTag("studentData");
            queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<JsonObjectRequest>() {
                @Override
                public void onRequestFinished(Request<JsonObjectRequest> request) {
                    if (request.getTag().equals("studentData")) {
                        queue.add(searchForPersonInformation(personHREF)).setTag("personData");
                        queue.add(searchForElevForholdInformation(studentRelationHREF)).setTag("studentRelationData");
                    }
                    if (request.getTag().equals("studentRelationData")) {
                        queue.add(searchForSchoolInfo(schoolHREF)).setTag("schoolData");
                    }
                    if (request.getTag().equals("personData")) {
                        isPersonDataReceived = true;
                    }
                    if (request.getTag().equals("schoolData")) {
                        isSchoolDataReceived = true;
                    }
                    if (isPersonDataReceived || isSchoolDataReceived)
                        drawUpStudent(student);
                }
            });
        }

        final FabSpeedDial fabSpeedDialMenu = findViewById(R.id.fab_menu);

        fabSpeedDialMenu.addOnMenuItemClickListener(new FabSpeedDial.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(FloatingActionButton miniFab, @Nullable TextView label, int itemId) {

                if (itemId == R.id.fab_buss) {
                    Intent intent = new Intent(getApplicationContext(), BusCardActivity.class);
                    intent.putExtra("student", student);
                    startActivity(intent);
                }
                if (itemId == R.id.fab_library) {
                    getFragment(LibraryCardFragment.newInstance());
                }
                if (itemId == R.id.fab_open_doors) {
                    getApplication().startActivity(
                            new Intent(getApplicationContext(), UnlockDoorsListActivity.class)
                    );
                }
                if (itemId == R.id.fab_log_out) {
                    getSharedPreferences(
                            FintStudentAppSharedPreferences.sharedPreferencesMainKey, MODE_PRIVATE)
                            .edit()
                            .remove(FintStudentAppSharedPreferences.isLoggedIn)
                            .apply()
                    ;
                    getApplicationContext().startActivity(
                            new Intent(getApplicationContext(), LogInActivity.class)
                    );
                }
            }
        });
    }

    // example brukernavn: leonahansen@sundetvgs.haugfk.no
    private JsonObjectRequest getStudentData(String username) {
        String URL = String.format("%s%s", "https://play-with-fint.felleskomponent.no/utdanning/elev/elev/feidenavn/", username);
        return new JsonObjectRequest(Request.Method.GET,
                URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String json = response.toString();
                            String identifierValue = JsonPath.read(json, "$.feidenavn.identifikatorverdi");
                            student.setStudentId(identifierValue);
                            editor.putString(FintStudentAppSharedPreferences.studentID, identifierValue);
                            personHREF = JsonPath.read(json, "$._links.person[0].href");
                            studentRelationHREF = JsonPath.read(json, "$._links.elevforhold[0].href");
                        } catch (PathNotFoundException e) {
                            e.printStackTrace();
                            System.out.println(response);
                        }
                    }
                }, errorListener);
    }

    private JsonObjectRequest searchForPersonInformation(String personHREF) {
        return new JsonObjectRequest(Request.Method.GET,
                personHREF, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String json = response.toString();
                            student.setFirstName(JsonPath.read(json, "$.navn.fornavn").toString());
                            student.setLastName(JsonPath.read(json, "$.navn.etternavn").toString());

                            student.setBirthDate(JsonPath.read(json, "$.fodselsdato").toString());
                            editor.putString(FintStudentAppSharedPreferences.studentFirstName, student.getFirstName());
                            editor.putString(FintStudentAppSharedPreferences.studentLastName, student.getLastName());
                            editor.putString(FintStudentAppSharedPreferences.studentBirthDate, student.getBirthDate());
                        } catch (PathNotFoundException e) {
                            e.printStackTrace();
                            System.out.println(response);
                        }
                    }
                }, errorListener);
    }

    private JsonObjectRequest searchForElevForholdInformation(String studentRelationHREF) {
        return new JsonObjectRequest(Request.Method.GET,
                studentRelationHREF, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            schoolHREF = JsonPath.read(response.toString(), "$._links.skole[0].href");
                        } catch (PathNotFoundException e) {
                            e.printStackTrace();
                            System.out.println(response);
                        }
                    }
                }, errorListener);
    }

    private JsonObjectRequest searchForSchoolInfo(String schoolHREF) {
        return new JsonObjectRequest(Request.Method.GET,
                schoolHREF, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String json = response.toString();
                            school.setSchoolName(JsonPath.read(json, "$.navn").toString());
                            editor.putString(FintStudentAppSharedPreferences.studentScoolName, school.getSchoolName());
                            school.setSchoolId(JsonPath.read(json, "$.skolenummer.identifikatorverdi").toString());
                            editor.putString(FintStudentAppSharedPreferences.studentScoolID, school.getSchoolId());
                            student.setSchool(school);
                            editor.apply();
                            drawUpStudent(student);
                        } catch (PathNotFoundException e) {
                            e.printStackTrace();
                            System.out.println(response);
                        }
                    }
                }, errorListener);
    }

    private void drawUpStudent(Student studentToDraw) {

        studentToDraw.setPhotoId(R.drawable.student_profile_picture);
        if (isPersonDataReceived) {
            studentNameTextView.setText(String.format("%s %s", studentToDraw.getFirstName(), studentToDraw.getLastName()));
            studentBirthDateTextView.setText(studentToDraw.getBirthDate());
            studentProfilePicture.setImageResource(studentToDraw.getPhotoId());
        }
        if (isSchoolDataReceived) {
            studentScoolNameTextView.setText(studentToDraw.getSchool().getSchoolName());
        }

        linearLayoutStudentProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(1500);
                rotate.setInterpolator(new LinearInterpolator());
                studentProfilePicture.startAnimation(rotate);
            }
        });
        studentProfilePicture.setClipToOutline(true);

    }

    private void getFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,
                fragment).addToBackStack(null).commit();
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                queue.stop();
                TextView errorTextView = findViewById(R.id.text_view_student_proof_main_activity_top);
                errorTextView.setText("IKKE GYLDIG");
                errorTextView = findViewById(R.id.text_view_student_proof_main_activity_bottom);
                errorTextView.setText("Elevbevis");
                linearLayoutStudentProof.setBackgroundColor(getColor(R.color.colorError));

                FrameLayout fl = findViewById(R.id.main_activity_frame_layout);
                Snackbar.make(fl ,"Noe gikk galt :'(", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                Log.e("ERROR", volleyError.toString());
                volleyError.printStackTrace();
            }
        };
    }
}
