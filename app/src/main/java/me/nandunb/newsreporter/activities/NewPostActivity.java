package me.nandunb.newsreporter.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import me.nandunb.newsreporter.R;
import me.nandunb.newsreporter.models.Draft;
import me.nandunb.newsreporter.models.Post;

public class NewPostActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private ProgressDialog pDialog;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    static final String TAG = "NR_DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance("gs://news-reporter-4fd27.appspot.com").getReference();

        pDialog = new ProgressDialog(NewPostActivity.this);

        capturePhoto(null);
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Draft")
                .setMessage("Do you want to save this as a draft?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        addDraft();
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        NewPostActivity.super.onBackPressed();
                    }}).show();

    }

    public void capturePhoto(View view) {

        Intent capturePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        capturePhotoIntent.putExtra(android.provider.MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if(capturePhotoIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(capturePhotoIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imagePreview = findViewById(R.id.new_image_preview);

            imagePreview.setImageBitmap(imageBitmap);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED) {
            this.finish();
            Intent intent = new Intent(this, FeedActivity.class);
            startActivity(intent);
        }
    }

    public void addPost(View view) {

        pDialog.setMessage("Creating new post...");
        pDialog.show();

        FirebaseUser user = mAuth.getCurrentUser();

        final String email = user.getEmail();
        final String displayName = user.getDisplayName();
        final String uid = user.getUid();

        TextView txtCaption = findViewById(R.id.txtCaption);
        final String caption = txtCaption.getText().toString();

        final String fileName = "images/"+email+"_"+new Date().toString()+".jpg";

        StorageReference photoStorageRef = mStorageRef.child(fileName);

        ImageView imagePreview = findViewById(R.id.new_image_preview);

        imagePreview.setDrawingCacheEnabled(true);
        imagePreview.buildDrawingCache();

        Bitmap bitmap = imagePreview.getDrawingCache();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = photoStorageRef.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String photoUrl = taskSnapshot.getDownloadUrl().toString();

                addDatabaseRecord(uid, email, displayName, photoUrl, caption);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pDialog.dismiss();
                Log.w(TAG, "Post created: failure");
                Toast.makeText(NewPostActivity.this, "Could not create new post!", Toast.LENGTH_LONG);
            }
        });


    }

    public void addDatabaseRecord(String uid, String email, String displayName, String photoUrl, String caption){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("post");

        Post post = new Post(email, displayName, photoUrl, caption);

        String postId = uid+"-"+ post.getCreatedOn().getTime();

        Log.d(TAG, "postId:"+postId);

        ref.child(postId).setValue(post);

        //Back to news feed view
        Intent intent = new Intent(NewPostActivity.this, FeedActivity.class);
        startActivity(intent);

        pDialog.dismiss();

    }

    public void addDraft(){
        pDialog.setMessage("Saving draft...");
        pDialog.show();

        final FirebaseUser user = mAuth.getCurrentUser();

        TextView txtCaption = findViewById(R.id.txtCaption);
        final String caption = txtCaption.getText().toString();

        final String fileName = "images/"+new Date().toString()+".jpg";

        StorageReference photoStorageRef = mStorageRef.child(fileName);

        ImageView imagePreview = findViewById(R.id.new_image_preview);

        imagePreview.setDrawingCacheEnabled(true);
        imagePreview.buildDrawingCache();

        Bitmap bitmap = imagePreview.getDrawingCache();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = photoStorageRef.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String photoUrl = taskSnapshot.getDownloadUrl().toString();

                addDraftRecord(user, photoUrl, caption);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pDialog.dismiss();
                Log.w(TAG, "Post created: failure");
                Toast.makeText(NewPostActivity.this, "Could not create new post!", Toast.LENGTH_LONG);
            }
        });

    }


    public void addDraftRecord(FirebaseUser user, String photoUrl, String caption){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("drafts");

        Draft draft = new Draft(photoUrl, caption);

        String postId = String.format("%d", draft.getCreatedOn().getTime());

        Log.d(TAG, "postId:"+postId);

        ref.child(user.getUid()).child(postId).setValue(draft);

        //Back to news feed view
        Intent intent = new Intent(NewPostActivity.this, FeedActivity.class);
        startActivity(intent);

        pDialog.dismiss();

    }

}
