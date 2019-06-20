package no.fint;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AbsenceActivity extends AppCompatActivity {

    TextView absenceRegisteredTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        Student student = intent.getParcelableExtra("student");

        LinearLayout linearLayoutStudentProof = findViewById(R.id.absence_proof_text_linear_layout);
        final ImageView studentProfilePicture = findViewById(R.id.absece_page_student_picture);
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
        TextView studentNameTextView = findViewById(R.id.absence_student_name_text_view);
        studentNameTextView.setText(String.format("%s %s", student.getFirstName(), student.getLastName()));
        TextView studentSchoolName = findViewById(R.id.absence_student_school_text_view);
        studentSchoolName.setText(student.getSchool().getSchoolName());
        studentProfilePicture.setClipToOutline(true);

        absenceRegisteredTextView = findViewById(R.id.text_view_absence_registered);
        absenceRegisteredTextView.setText(String.format("%s dager og %s timer", student.getAbsenceDays(), student.getAbsenceHours()));

        FloatingActionButton fab = findViewById(R.id.fab_add_abcense);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragment(AddAbsenceFragment.newInstance());
            }
        });
    }
    private void getFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.add(R.id.absence_fragment_container,
                fragment).addToBackStack(null).commit();
    }

}
