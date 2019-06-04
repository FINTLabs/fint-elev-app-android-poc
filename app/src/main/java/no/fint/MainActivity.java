package no.fint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
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

        FloatingActionButton miniFab0 = fabSpeedDialMeny.getMiniFab(0);
        FloatingActionButton miniFab1 = fabSpeedDialMeny.getMiniFab(1);
        FloatingActionButton miniFab2 = fabSpeedDialMeny.getMiniFab(2);
        FloatingActionButton miniFab3 = fabSpeedDialMeny.getMiniFab(3);
        final int id = miniFab0.getId();

        fabSpeedDialMeny.addOnMenuItemClickListener(new FabSpeedDial.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(FloatingActionButton miniFab, @Nullable TextView label, int itemId) {
                System.out.println(itemId);
                System.out.println(id);
                if (itemId == id){
                    BussKortFragment bussKortFragment = BussKortFragment.newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    fragmentTransaction.add(R.id.fragment_container,
                            bussKortFragment).addToBackStack(null).commit();
                }
            }
        });
    }
}
