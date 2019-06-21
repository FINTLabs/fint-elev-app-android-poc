package no.fint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class ScoreCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_card);
        getSupportActionBar().setTitle("Karakterkort");

        Intent intent = getIntent();
        final Student student = intent.getParcelableExtra("student");

        TextView underTitelText = findViewById(R.id.score_card_under_title);
        underTitelText.setText(String.format("%s %s", student.getFirstName(), student.getLastName()));

        TextView scoreCardAbsenceDays = findViewById(R.id.absence_score_card_days);
        scoreCardAbsenceDays.setText(String.format("%s", student.getAbsenceDays()));

        TextView scoreCardAbsenceHours = findViewById(R.id.absence_score_card_hours);
        scoreCardAbsenceHours.setText(String.format("%s", student.getAbsenceHours()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}
