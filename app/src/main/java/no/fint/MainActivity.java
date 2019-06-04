package no.fint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import io.github.kobakei.materialfabspeeddial.FabSpeedDial;

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


        final FabSpeedDial fabSpeedDialMeny = findViewById(R.id.fab_menu);
    }
}
