package no.fint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogInActivity extends AppCompatActivity {
    EditText userNameEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logg_inn);

        userNameEditText = findViewById(R.id.user_login_email_input_field);
        passwordEditText = findViewById(R.id.user_login_password_input_field);
        Button logInButton = findViewById(R.id.user_login_button);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                mainActivityIntent.putExtra("Brukernavn", userNameEditText.getText());
                mainActivityIntent.putExtra("Password", passwordEditText.getText());
                SharedPreferences.Editor editor = getSharedPreferences(FintStudentAppSharedPreferences.sharedPreferencesMainKey, MODE_PRIVATE).edit();
                editor.putBoolean(FintStudentAppSharedPreferences.isLoggedIn, true);
                editor.apply();
                startActivity(mainActivityIntent);
            }
        });
    }
}
