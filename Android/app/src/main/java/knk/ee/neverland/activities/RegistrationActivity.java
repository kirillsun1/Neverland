package knk.ee.neverland.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import knk.ee.neverland.R;
import knk.ee.neverland.api.RegistrationData;
import knk.ee.neverland.exceptions.LoginFailedException;
import knk.ee.neverland.exceptions.RegistrationFailedException;
import knk.ee.neverland.fakeapi.FakeAPISingleton;

public class RegistrationActivity extends AppCompatActivity {
    public static final int SUCCESSFUL_REGISTRATION = 1;

    private AutoCompleteTextView loginBox;
    private EditText passwordBox;
    private EditText firstNameBox;
    private EditText secondNameBox;
    private EditText emailBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        loginBox = findViewById(R.id.registration_login);
        passwordBox = findViewById(R.id.registration_password);
        firstNameBox = findViewById(R.id.registration_first_name);
        secondNameBox = findViewById(R.id.registration_second_name);
        emailBox = findViewById(R.id.registration_email);

        findViewById(R.id.registration_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    RegistrationData registrationData = makeRegistrationData();
                    register(registrationData);
                    finishNow(login(registrationData.getLogin(), registrationData.getPassword()));
                } catch (RegistrationFailedException | LoginFailedException e) {
                    showFailMessage(e.getMessage());
                }
            }
        });
    }

    private RegistrationData makeRegistrationData() {
        RegistrationData registrationData = new RegistrationData();

        registrationData.setLogin(loginBox.getText().toString());
        registrationData.setPassword(passwordBox.getText().toString());
        registrationData.setFirstName(firstNameBox.getText().toString());
        registrationData.setSecondName(secondNameBox.getText().toString());
        registrationData.setEmail(emailBox.getText().toString());

        return registrationData;
    }

    private void register(RegistrationData registrationData) throws RegistrationFailedException {
        FakeAPISingleton.getAuthInstance().registerAccount(registrationData);
    }

    private String login(String login, String pass) throws LoginFailedException {
        return FakeAPISingleton.getAuthInstance().attemptLogin(login, pass);
    }

    private void showFailMessage(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    private void finishNow(String key) {
        Intent intent = new Intent();
        intent.putExtra("key", key);
        setResult(SUCCESSFUL_REGISTRATION, intent);
        finish();
    }
}
