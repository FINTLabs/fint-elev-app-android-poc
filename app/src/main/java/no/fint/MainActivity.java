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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.github.kobakei.materialfabspeeddial.FabSpeedDial;

public class MainActivity extends AppCompatActivity {
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Starts LogInActivity is not logged inn
        //hard coded to be true, but set to false to start intent
        SharedPreferences mainActivitySharedPreferences = getSharedPreferences(FintStudentAppSharedPreferences.sharedPreferencesMainKey, MODE_PRIVATE);
        if (!mainActivitySharedPreferences.getBoolean(FintStudentAppSharedPreferences.isLoggedIn, false)) {
            Intent logInIntent = new Intent(this, LogInActivity.class);
            startActivity(logInIntent);
        }
        String brukernavn = getIntent().getExtras().getString("Brukernavn");
        student = getStudentData(brukernavn);

        School school = new School("01", "Skien videregående skole");
        final Student newStudent = new Student("Anders", "Johansen", "15.10.2002", "123987", school, R.drawable.student_profile_picture);

        LinearLayout linearLayoutStudentProof = findViewById(R.id.student_proof_text_linear_layout);
        final ImageView studentProfilePicture = findViewById(R.id.front_page_student_picture);
        studentProfilePicture.setImageResource(newStudent.getPhotoId());
        linearLayoutStudentProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(1500);
                rotate.setInterpolator(new LinearInterpolator());
                studentProfilePicture.startAnimation(rotate);
            }
        });
        TextView studentNameTextView = findViewById(R.id.main_student_name_text_view);
        studentNameTextView.setText(String.format("%s %s", newStudent.getFirstName(), newStudent.getLastName()));
        TextView studentBirthDateTextView = findViewById(R.id.main_student_birth_date_text_view);
        studentBirthDateTextView.setText(newStudent.getBirthDate());
        TextView studentScoolName = findViewById(R.id.main_student_school_text_view);
        studentScoolName.setText(newStudent.getSchool().getSchoolName());

        studentProfilePicture.setClipToOutline(true);

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
                    intent.putExtra("student", newStudent);
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

    private Student getStudentData(String brukernavn) {
        String URL = "";
        JsonArrayRequest hentBarnehager = new JsonArrayRequest(Request.Method.GET,
                URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

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
        queue.add(hentBarnehager);

        return null;
    }

    private void getFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,
                fragment).addToBackStack(null).commit();
    }
}
