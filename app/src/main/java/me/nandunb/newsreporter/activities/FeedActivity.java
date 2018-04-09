package me.nandunb.newsreporter.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.nandunb.newsreporter.*;
import me.nandunb.newsreporter.helpers.NewsAdapter;
import me.nandunb.newsreporter.models.Post;

public class FeedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<Post> postsList;

    private FirebaseDatabase mDatabase;
    private DatabaseReference ref;


    static final String TAG = "NR_DEBUG";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setTitle(R.string.news_feed_title);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FeedActivity.this, NewPostActivity.class);
                startActivity(intent);
            }
        });

        mDatabase = FirebaseDatabase.getInstance();

        ref = mDatabase.getReference("post");

        recyclerView = (RecyclerView) findViewById(R.id.cards_recycler_view);

        postsList = new ArrayList<>();
        adapter = new NewsAdapter(this, postsList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);



        mDrawerLayout = findViewById(R.id.drawer_layout);

        mAuth = FirebaseAuth.getInstance();

        setNavigationViewListener();

        retrieve();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            goToLogin();
            return;
        }

//        TextView username = findViewById(R.id.nav_user_name);
//
//        String displayName = currentUser.getDisplayName();
//
//        if(displayName == null | displayName.isEmpty()){
//            username.setText(R.string.unnamed);
//        }
//
//        username.setText(currentUser.getDisplayName());

    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieve();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        retrieve();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){

            case R.id.nav_logout: {

                signOut();
                break;

            }

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    public void signOut() {
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

    private void goToLogin(){
        Intent intent = new Intent(FeedActivity.this, SignInActivity.class);
        startActivity(intent);
    }

    private void retrieve(){
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void fetchData(DataSnapshot snapshot){

        Log.d(TAG, snapshot.getKey());

        Post post = snapshot.getValue(Post.class);
        postsList.add(post);

        Log.d(TAG, post.getEmail());

        adapter.notifyDataSetChanged();

    }
}
