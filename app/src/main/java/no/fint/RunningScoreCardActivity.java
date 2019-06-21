package no.fint;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RunningScoreCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_score_card);

        getSupportActionBar().setTitle("Karakterer");

        Intent intent = getIntent();
        final Student student = intent.getParcelableExtra("student");
        TextView underTitelText = findViewById(R.id.score_card_under_title);
        underTitelText.setText(String.format("%s %s",student.getFirstName(),student.getLastName()));
        Button toScoreCardActivity = findViewById(R.id.to_score_card_button);
        toScoreCardActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RunningScoreCardActivity.this, ScoreCardActivity.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });
    }
}
