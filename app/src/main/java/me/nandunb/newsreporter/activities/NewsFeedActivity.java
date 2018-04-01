package me.nandunb.newsreporter.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import me.nandunb.newsreporter.NewsFeedFragment;
import me.nandunb.newsreporter.R;

public class NewsFeedActivity extends FragmentActivity implements NewsFeedFragment.OnFragmentInteractionListener {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference ref;

    static final String TAG = "NR_DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_newsfeed);


        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            goToLogin();
        }

    }

    public void signOut(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Do you really want to sign out?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        mAuth.signOut();
                        goToLogin();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void addNewPost(View view) {
        Intent intent = new Intent(this, NewPostActivity.class);
        startActivity(intent);
    }


    private void goToLogin(){
        Intent intent = new Intent(NewsFeedActivity.this, SignInActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
