package me.nandunb.newsreporter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void signUp(View view) {
    }

    public void goToLogin(View view){
        Intent loginIntent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(loginIntent);
    }
}
