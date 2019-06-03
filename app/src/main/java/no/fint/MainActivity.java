package no.fint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Starter LoggInnActivity om brukern ikke er logget inn, men er satt til true nå, for å
        //starte sett if til false
        SharedPreferences mainActivitySharedPreferences = getSharedPreferences(FintElevAppSharedPreferences.sharedPreferencesHovednokkel, MODE_PRIVATE);
        if (mainActivitySharedPreferences.getBoolean(FintElevAppSharedPreferences.erElevInnlogget, false)){
            Intent loggInnIntent = new Intent(this, LoggInnActivity.class);
            startActivity(loggInnIntent);
        }
        ImageView studentProfilBilde = findViewById(R.id.forside_student_bilde);
        studentProfilBilde.setClipToOutline(true);
    }
}
