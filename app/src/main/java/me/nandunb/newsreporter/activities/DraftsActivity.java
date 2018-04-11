package me.nandunb.newsreporter.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import me.nandunb.newsreporter.R;
import me.nandunb.newsreporter.helpers.DraftAdapter;
import me.nandunb.newsreporter.helpers.NewsAdapter;
import me.nandunb.newsreporter.models.Draft;
import me.nandunb.newsreporter.models.Post;

public class DraftsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private DraftAdapter adapter;
    private List<Draft> draftList;


    private FirebaseDatabase mDatabase;
    private DatabaseReference ref;


    static final String TAG = "NR_DRAFT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drafts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.drafts_title);

        mDatabase = FirebaseDatabase.getInstance();

        ref = mDatabase.getReference("drafts");

        recyclerView = (RecyclerView) findViewById(R.id.drafts_recycler_view);

        draftList = new ArrayList<>();
        adapter = new DraftAdapter(this, draftList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);


        mAuth = FirebaseAuth.getInstance();

        retrieve();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_drafts, menu);
        return true;
    }


    private void retrieve(){

        Log.d(TAG+" retrieve()", "Retrieving data.");

        FirebaseUser user = mAuth.getCurrentUser();

        Log.d(TAG, "UserID "+ user.getUid());

        Query query = ref.child(user.getUid()).orderByChild("createdOn");

        query.addChildEventListener(new ChildEventListener() {
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

        Log.d(TAG+" fetchData()", snapshot.getKey());

        Draft draft = snapshot.getValue(Draft.class);
        draftList.add(draft);

        adapter.notifyDataSetChanged();

    }

    private void edit(View view){

    }

}
