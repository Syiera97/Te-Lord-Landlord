package com.example.te_lord.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.te_lord.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText userMail, userPassword;
    private Button LoginButton;
    private ProgressBar login_progressBar;
    private FirebaseAuth mAuth;
    private Intent HomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userMail = findViewById(R.id.login_email);
        userPassword = findViewById(R.id.login_password);
        LoginButton = findViewById(R.id.Login_button);
        login_progressBar = findViewById(R.id.login_progressBar);
        mAuth = FirebaseAuth.getInstance();
//        HomeActivity = new Intent (this, com.example.firstattempt.Activities.MainMenu.class);
        HomeFragment = new Intent(this, DashboardActivity.class);

        login_progressBar.setVisibility(View.INVISIBLE);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_progressBar.setVisibility(View.VISIBLE);
                LoginButton.setVisibility(View.INVISIBLE);

                final String mail = userMail.getText().toString();
                final String password = userPassword.getText().toString();

                if(mail.isEmpty() ||password.isEmpty()){
                    showMessage("Please Verify All Field");
                    LoginButton.setVisibility(View.VISIBLE);
                    login_progressBar.setVisibility(View.INVISIBLE);
                }
                else{
                    signIn(mail,password);
                }
            }
        });
    }
    private void signIn(String mail, String password) {
        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    login_progressBar.setVisibility(View.INVISIBLE);
                    LoginButton.setVisibility(View.VISIBLE);
                    updateUI();
                }
                else{
                    showMessage(task.getException().getMessage());
                    LoginButton.setVisibility(View.VISIBLE);
                    login_progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void updateUI() {
        Intent DashboardActivity = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(DashboardActivity);
        finish();
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(),text, Toast.LENGTH_LONG).show();
    }
}
