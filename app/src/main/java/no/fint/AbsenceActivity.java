package no.fint;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

public class AbsenceActivity extends AppCompatActivity {

    TextView absenceRegisteredTextView;
    Student student;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absence);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        student = intent.getParcelableExtra("student");

        final Button notSchoolToday = findViewById(R.id.button_not_school_today);
        notSchoolToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAbsence(1, 0);
                notSchoolToday.setEnabled(false);
                notSchoolToday.setText("Takk for at du sier fra!");
            }
        });

        absenceRegisteredTextView = findViewById(R.id.text_view_absence_registered);
        absenceRegisteredTextView.setText(String.format("%s dager og %s timer", student.getAbsenceDays(), student.getAbsenceHours()));

        fab = findViewById(R.id.fab_add_abcense);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    fab.setRotation(45);
                    getFragment(AddAbsenceFragment.newInstance());
                } else {
                    fab.setRotation(0);
                    onBackPressed();
                }

            }
        });
    }

    public void updateAbsence(int days, int hours) {
        student.setAbsenceHours(hours + student.getAbsenceHours());
        student.setAbsenceDays(days + student.getAbsenceDays());
        absenceRegisteredTextView.setText(String.format("%s dager og %s timer", student.getAbsenceDays(), student.getAbsenceHours()));
        Toast.makeText(this
                , String.format("%s dager og %s timer lagt til i fravær",
                        days,
                        hours
                )
                , Toast.LENGTH_LONG
        ).show();
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
