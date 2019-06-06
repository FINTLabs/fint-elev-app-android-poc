package no.fint;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.github.kobakei.materialfabspeeddial.FabSpeedDial;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Starts LogInActivity is not logged inn
        //hard coded to be true, but set to false to start intent
        SharedPreferences mainActivitySharedPreferences = getSharedPreferences(FintStudentAppSharedPreferences.sharedPreferencesMainKey, MODE_PRIVATE);
        if (mainActivitySharedPreferences.getBoolean(FintStudentAppSharedPreferences.isLoggedIn, false)) {
            Intent logInIntent = new Intent(this, LogInActivity.class);
            startActivity(logInIntent);
        }
        final ImageView studentProfilePicture = findViewById(R.id.front_page_student_picture);
        studentProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(1500);
                rotate.setInterpolator(new LinearInterpolator());
                studentProfilePicture.startAnimation(rotate);
            }
        });

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
                if (label.getText().equals("Skoleskyss")) { //Need to find a better solution than this if-statement
                    BusCardFragment busCardFragment = BusCardFragment.newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    fragmentTransaction.add(R.id.fragment_container,
                            busCardFragment).addToBackStack(null).commit();
                }
                if (label.getText().equals("Bibliotekskort")) { //Need to find a better solution than this if-statement
                    LibraryCardFragment libraryCardFragment = LibraryCardFragment.newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    fragmentTransaction.add(R.id.fragment_container,
                            libraryCardFragment).addToBackStack(null).commit();
                }
                if (label.getText().equals("Åpne dører")) { //Need to find a better solution than this if-statement
                    Intent doorIntent = new Intent(getApplicationContext(), UnlockDoorsListActivity.class);
                    getApplication().startActivity(doorIntent);
                }
            }
        });
    }
}
