package me.nandunb.newsreporter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "NR_DEBUG";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Check if the user is logged in and go to feed
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            updateUI(currentUser);
        }
    }

    public void signIn(View view) {

        EditText emailText = findViewById(R.id.user_email);
        EditText passwordText = findViewById(R.id.user_password);

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(SignInActivity.this, "Enter email and password!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmailAndPassword: success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(SignInActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    updateUI(user);
                }else{
                    Log.w(TAG, "signInWithEmailAndPassword: failure");
                    Toast.makeText(SignInActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void updateUI(FirebaseUser user){
        Intent intent = new Intent(this, NewsFeedActivity.class);
        intent.putExtra("displayName", user.getDisplayName());
        intent.putExtra("email", user.getEmail());
        startActivity(intent);
    }
}
