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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.github.kobakei.materialfabspeeddial.FabSpeedDial;

public class MainActivity extends AppCompatActivity {
    Student student;
    School school;
    SharedPreferences mainActivitySharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivitySharedPreferences = getSharedPreferences(FintStudentAppSharedPreferences.sharedPreferencesMainKey, MODE_PRIVATE);
        editor = mainActivitySharedPreferences.edit();
        student = new Student();
        school = new School();
        //Starts LogInActivity is not logged inn
        //hard coded to be true, but set to false to start intent
        if (!mainActivitySharedPreferences.getBoolean(FintStudentAppSharedPreferences.isLoggedIn, false)) {
            Intent logInIntent = new Intent(this, LogInActivity.class);
            startActivity(logInIntent);
        }
        student.setPhotoId(R.drawable.student_profile_picture);
        if (getIntent().hasExtra("Brukernavn")){
            String username = getIntent().getExtras().getString("Brukernavn");
            getStudentData(username);
        }
        else {
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
    private void getStudentData(String username) {
        String URL = String.format("%s%s", "https://play-with-fint.felleskomponent.no/utdanning/elev/elev/feidenavn/", username);
        JsonObjectRequest getFeideInfo = new JsonObjectRequest(Request.Method.GET,
                URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject studentNumberJsonObject = response.getJSONObject("elevnummer");
                            String identifierValue = studentNumberJsonObject.getString("identifikatorverdi");
                            student.setStudentId(identifierValue);
                            editor.putString(FintStudentAppSharedPreferences.studentID, identifierValue);
                            JSONObject links = response.getJSONObject("_links");
                            JSONArray personLinkArray = links.getJSONArray("person");
                            JSONArray studentRelationLinkArray = links.getJSONArray("elevforhold");
                            JSONObject personHREFObject = personLinkArray.getJSONObject(0);
                            JSONObject personStudentRelationObject = studentRelationLinkArray.getJSONObject(0);
                            String personHREF = personHREFObject.getString("href");
                            String studentRelationHREF = personStudentRelationObject.getString("href");
                            searchForPersonInformation(personHREF, studentRelationHREF);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println(response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("ERROR", volleyError.toString());
                volleyError.printStackTrace();
                System.out.println(volleyError.getMessage());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(getFeideInfo);
    }

    private void searchForPersonInformation(String personHREF, final String studentRelationHREF) {
        JsonObjectRequest getPersonInfo = new JsonObjectRequest(Request.Method.GET,
                personHREF, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject nameObject = response.getJSONObject("navn");

                            student.setFirstName(nameObject.getString("fornavn"));
                            student.setLastName(nameObject.getString("etternavn"));
                            student.setBirthDate(response.getString("fodselsdato"));
                            editor.putString(FintStudentAppSharedPreferences.studentFirstName, nameObject.getString("fornavn"));
                            editor.putString(FintStudentAppSharedPreferences.studentLastName, nameObject.getString("etternavn"));
                            editor.putString(FintStudentAppSharedPreferences.studentBirthDate, response.getString("fodselsdato"));
                            searchForElevForholdInformation(studentRelationHREF);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println(response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("ERROR", volleyError.toString());
                volleyError.printStackTrace();
                System.out.println(volleyError.getMessage());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(getPersonInfo);
    }

    private void searchForElevForholdInformation(String studentRelationHREF) {
        JsonObjectRequest getStudentRelationInfo = new JsonObjectRequest(Request.Method.GET,
                studentRelationHREF, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject links = response.getJSONObject("_links");
                            JSONArray schoolJSONArray = links.getJSONArray("skole");
                            JSONObject schoolJSONObject = schoolJSONArray.getJSONObject(0);
                            searchForSchoolInfo(schoolJSONObject.getString("href"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println(response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("ERROR", volleyError.toString());
                volleyError.printStackTrace();
                System.out.println(volleyError.getMessage());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(getStudentRelationInfo);
    }

    private void searchForSchoolInfo(String schoolHREF) {
        JsonObjectRequest getElevForholdInfo = new JsonObjectRequest(Request.Method.GET,
                schoolHREF, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            school.setSchoolName(response.getString("navn"));
                            editor.putString(FintStudentAppSharedPreferences.studentScoolName, response.getString("navn"));
                            JSONObject schoolNumberObject = response.getJSONObject("skolenummer");
                            school.setSchoolId(schoolNumberObject.getString("identifikatorverdi"));
                            editor.putString(FintStudentAppSharedPreferences.studentScoolID, schoolNumberObject.getString("identifikatorverdi"));
                            student.setSchool(school);
                            editor.apply();
                            drawUpStudent(student);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println(response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("ERROR", volleyError.toString());
                volleyError.printStackTrace();
                System.out.println(volleyError.getMessage());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(getElevForholdInfo);
    }

    private void drawUpStudent(Student student) {
        TextView studentNameTextView = findViewById(R.id.main_student_name_text_view);
        studentNameTextView.setText(String.format("%s %s", student.getFirstName(), student.getLastName()));
        TextView studentBirthDateTextView = findViewById(R.id.main_student_birth_date_text_view);
        studentBirthDateTextView.setText(student.getBirthDate());
        TextView studentScoolName = findViewById(R.id.main_student_school_text_view);
        studentScoolName.setText(student.getSchool().getSchoolName());

        LinearLayout linearLayoutStudentProof = findViewById(R.id.student_proof_text_linear_layout);
        final ImageView studentProfilePicture = findViewById(R.id.front_page_student_picture);
        studentProfilePicture.setImageResource(student.getPhotoId());
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
}
