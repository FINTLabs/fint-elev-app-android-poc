package no.fint;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.lang.reflect.Field;

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

    public static void setNumberPickerColor(NumberPicker numberPicker, int color) {

        try {
            Field selectorWheelPaintField = numberPicker.getClass()
                    .getDeclaredField("mSelectorWheelPaint");
            selectorWheelPaintField.setAccessible(true);
            ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
        } catch (NoSuchFieldException e) {
            Log.w("numberPickerTextColor", e);
        } catch (IllegalAccessException e) {
            Log.w("numberPickerTextColor", e);
        } catch (IllegalArgumentException e) {
            Log.w("numberPickerTextColor", e);
        }

        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText)
                ((EditText) child).setTextColor(color);
        }
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(numberPicker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        numberPicker.invalidate();
    }

}
