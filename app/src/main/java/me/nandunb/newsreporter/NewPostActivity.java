package me.nandunb.newsreporter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NewPostActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        mAuth = FirebaseAuth.getInstance();
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
}
