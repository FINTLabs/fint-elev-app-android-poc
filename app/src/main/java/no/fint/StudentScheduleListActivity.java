package no.fint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StudentScheduleListActivity extends AppCompatActivity {

    Date date;
    Calendar calendar;
    TextView studentScheduleDate;
    StudentScheduleRecyclerViewAdapter subjectRecyclerViewAdapter;
    ArrayList<Subject> subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_for_subjects_student_schedule);
        getSupportActionBar().setTitle("Timeplan");
        studentScheduleDate = findViewById(R.id.subject_schedule_date);
        date = new Date();
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        studentScheduleDate.setText(String.format("%s-%s-%s",
                DateFormat.format("dd", date),
                DateFormat.format("MM", date),
                DateFormat.format("yyyy", date)));
        subjects = new ArrayList<>();
        createSubjects();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_for_subjects_student_schedule);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        subjectRecyclerViewAdapter = new StudentScheduleRecyclerViewAdapter(subjects, this);
        recyclerView.setAdapter(subjectRecyclerViewAdapter);
        Button dateAdd = findViewById(R.id.button_add_date_schedular);
        dateAdd.setOnClickListener(returnDate(1));
        Button dateDecrease = findViewById(R.id.button_decrease_date_schedular);
        dateDecrease.setOnClickListener(returnDate(-1));
    }

    private View.OnClickListener returnDate(final int i) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, i);
                date = calendar.getTime();
                studentScheduleDate.setText(String.format("%s-%s-%s",
                        DateFormat.format("dd", date),
                        DateFormat.format("MM", date),
                        DateFormat.format("yyyy", date)));
                subjects.clear();
                createSubjects();
                subjectRecyclerViewAdapter.notifyDataSetChanged();
            }
        };
    }

    private void createSubjects() {
        for (int i = 0; i < 3; i++) {
            String subject, teacher, startTime, endTime;
            double random = Math.random();
            if (random < 0.33) {
                subject = "Norsk";
                teacher = "Henrik Ibset";
            } else if (random > 0.66) {
                subject = "Matte";
                teacher = "Albert Tostein";
            } else {
                subject = "Engelsk";
                teacher = "Petter Solberg";
            }
            startTime = (i == 0) ? "08:30" : (i == 1) ? "10:30" : "12:30";
            endTime = (i == 0) ? "10:00" : (i == 1) ? "12:00" : "14:00";
            subjects.add(Subject.builder().name(subject).startTime(startTime).endTime(endTime).teacherName(teacher).build());
        }
    }
}
