package me.nandunb.newsreporter;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressDialog pDialog;

    static final String TAG = "NR_DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        pDialog = new ProgressDialog(SignUpActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            updateUI(currentUser);
        }
    }

    public void updateUI(FirebaseUser user){
        Intent intent = new Intent(this, NewsFeedActivity.class);
        intent.putExtra("displayName", user.getDisplayName());
        intent.putExtra("email", user.getEmail());
        startActivity(intent);
    }

    public void signUp(View view) {

        EditText txtEmail = findViewById(R.id.user_signup_email);
        EditText txtDisplayName = findViewById(R.id.user_signup_display_name);
        EditText txtPassword = findViewById(R.id.user_signup_password);

        String email = txtEmail.getText().toString();
        final String displayName = txtDisplayName.getText().toString();
        String password = txtPassword.getText().toString();

        //Validation
        String error_msg = null;

        if(email.isEmpty()){
            error_msg = "Please enter an email";
        }else if(displayName.isEmpty()) {
            error_msg = "Please enter a display name";
        }else if(password.isEmpty()){
            error_msg = "Please enter a password";
        }

        if(error_msg != null){
            Toast.makeText(SignUpActivity.this, error_msg, Toast.LENGTH_LONG);
            return;
        }

        pDialog.setMessage("Creating your account...");
        pDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmailAndPassword: successful");
                            final FirebaseUser currentUser = mAuth.getCurrentUser();

                            //Set display name
                            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(displayName)
                                    .build();
                            currentUser.updateProfile(request)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(SignUpActivity.this,
                                                    "Sign Up successful!", Toast.LENGTH_SHORT);
                                            pDialog.dismiss();
                                            updateUI(currentUser);
                                        }
                                    });
                        }else {
                            pDialog.dismiss();
                            Log.w(TAG, "Sign Up failed");
                            Toast.makeText(SignUpActivity.this, "Sign Up failed!", Toast.LENGTH_SHORT);
                        }
                    }
                });


    }


    public void goToLogin(View view){
        Intent loginIntent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(loginIntent);
    }
}
