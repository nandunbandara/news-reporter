package me.nandunb.newsreporter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class NewPostActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    static final String TAG = "NR_DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance("gs://news-reporter-4fd27.appspot.com").getReference();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

    }


    public void capturePhoto(View view) {

        Intent capturePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(capturePhotoIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(capturePhotoIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imagePreview = findViewById(R.id.imagePreview);

            imagePreview.setImageBitmap(imageBitmap);
        }
    }

    public void addPost(View view) {

        FirebaseUser user = mAuth.getCurrentUser();

        final String email = user.getEmail();
        final String displayName = user.getDisplayName();

        TextView txtCaption = findViewById(R.id.txtCaption);
        final String caption = txtCaption.getText().toString();

        final String fileName = "images/"+email+"_"+new Date().toString()+".jpg";

        StorageReference photoStorageRef = mStorageRef.child(fileName);

        ImageView imagePreview = findViewById(R.id.imagePreview);

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

                addDatabaseRecord(email, displayName, photoUrl, caption);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Post created: failure");
                Toast.makeText(NewPostActivity.this, "Could not create new post!", Toast.LENGTH_SHORT);
            }
        });


    }

    public void addDatabaseRecord(String email, String displayName, String photoUrl, String caption){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("post");

        Post post = new Post(email, displayName, photoUrl, caption);

        ref.setValue(post);

        Toast.makeText(NewPostActivity.this, "New post created!", Toast.LENGTH_SHORT);

    }
}
