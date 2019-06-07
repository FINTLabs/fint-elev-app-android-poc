package no.fint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
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

import org.json.JSONArray;
import org.json.JSONException;
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
    TextView studentScoolName;
    ImageView studentProfilePicture;
    Response.ErrorListener errorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivitySharedPreferences = getSharedPreferences(FintStudentAppSharedPreferences.sharedPreferencesMainKey, MODE_PRIVATE);
        editor = mainActivitySharedPreferences.edit();
        student = new Student();
        school = new School();
        queue = Volley.newRequestQueue(this);
        studentNameTextView = findViewById(R.id.main_student_name_text_view);
        studentBirthDateTextView = findViewById(R.id.main_student_birth_date_text_view);
        studentScoolName = findViewById(R.id.main_student_school_text_view);
        studentProfilePicture = findViewById(R.id.front_page_student_picture);
        //Starts LogInActivity is not logged inn
        //hard coded to be true, but set to false to start intent
        if (!mainActivitySharedPreferences.getBoolean(FintStudentAppSharedPreferences.isLoggedIn, false)) {
            Intent logInIntent = new Intent(this, LogInActivity.class);
            startActivity(logInIntent);
        }
        student.setPhotoId(R.drawable.student_profile_picture);
        if (getIntent().hasExtra("Brukernavn")) {
            String username = getIntent().getExtras().getString("Brukernavn");
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
        } else {
            student.setFirstName(mainActivitySharedPreferences
                    .getString(FintStudentAppSharedPreferences.studentFirstName
                            , "Ingen fornavn"));
            student.setLastName(mainActivitySharedPreferences
                    .getString(FintStudentAppSharedPreferences.studentLastName
                            , "Ingen etternavn"));
            student.setStudentId(mainActivitySharedPreferences
                    .getString(FintStudentAppSharedPreferences.studentID
                            , "Ingen ID"));
            student.setBirthDate(mainActivitySharedPreferences
                    .getString(FintStudentAppSharedPreferences.studentBirthDate
                            , "Ingen fødselsdato"));
            school.setSchoolId(mainActivitySharedPreferences
                    .getString(FintStudentAppSharedPreferences.studentScoolID
                            , "Ingen skoleID"));
            school.setSchoolName(mainActivitySharedPreferences
                    .getString(FintStudentAppSharedPreferences.studentScoolName
                            , "Ingen skolenavn"));
            student.setSchool(school);
            isSchoolDataReceived = isPersonDataReceived = true;
            drawUpStudent(student);
        }



        /*
        LinearLayout linearLayoutBottomFrontPage = findViewById(R.id.student_bottom_text);
        AnimatorSet glow = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.glow_animation);
        glow.setTarget(linearLayoutBottomFrontPage);
        glow.start();
        */

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
                    getApplication().startActivity(
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
                        } catch (Exception e) { //kan vi bruke JsonPathException??
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
                            student.setFirstName((String)JsonPath.read(json, "$.navn.fornavn"));
                            student.setLastName((String)JsonPath.read(json, "$.navn.etternavn"));
                            student.setBirthDate((String)JsonPath.read(json, "$.fodselsdato"));
                            editor.putString(FintStudentAppSharedPreferences.studentFirstName, student.getFirstName());
                            editor.putString(FintStudentAppSharedPreferences.studentLastName, student.getLastName());
                            editor.putString(FintStudentAppSharedPreferences.studentBirthDate, student.getBirthDate());
                        } catch (Exception e) { //kan vi bruke JsonPathException??
                            e.printStackTrace();
                            System.out.println(response);
                        }
                    }
                }, errorListener);
         /*queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> r) {
                System.out.println("r = " + r);
            }
        });*/
    }

    private JsonObjectRequest searchForElevForholdInformation(String studentRelationHREF) {
        return new JsonObjectRequest(Request.Method.GET,
                studentRelationHREF, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            schoolHREF = JsonPath.read(response.toString(), "$._links.skole[0].href");
                        } catch (Exception e) { //kan vi bruke JsonPathException??
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
                            school.setSchoolName((String)JsonPath.read(json, "$.navn"));
                            editor.putString(FintStudentAppSharedPreferences.studentScoolName, school.getSchoolName());
                            school.setSchoolId((String)JsonPath.read(json, "$.skolenummer.identifikatorverdi"));
                            editor.putString(FintStudentAppSharedPreferences.studentScoolID, school.getSchoolId());
                            student.setSchool(school);
                            editor.apply();
                            drawUpStudent(student);
                        } catch (Exception e) { //kan vi bruke JsonPathException??
                            e.printStackTrace();
                            System.out.println(response);
                        }
                    }
                }, errorListener);
        /*person p;
        navn = JsonPath.get(p, "$._links.elevforhold[*].href");*/
    }

    private void drawUpStudent(Student student) {
        if (isPersonDataReceived){
            studentNameTextView.setText(String.format("%s %s", student.getFirstName(), student.getLastName()));
            studentBirthDateTextView.setText(student.getBirthDate());
            studentProfilePicture.setImageResource(student.getPhotoId());
        }
        if (isSchoolDataReceived){
            studentScoolName.setText(student.getSchool().getSchoolName());
        }
        LinearLayout linearLayoutStudentProof = findViewById(R.id.student_proof_text_linear_layout);
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
                Log.e("ERROR", volleyError.toString());
                volleyError.printStackTrace();
            }
        };
    }
}
