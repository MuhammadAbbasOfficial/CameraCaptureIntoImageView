package com.example.cameraintoimageview;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button camera;
    Uri imageUri;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        camera = findViewById(R.id.camera);



        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(MainActivity.this)
                        .cameraOnly()
 /*                       .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)//Final image size will be less than 1 MB(Optional)
                        .cameraOnly()
                        .maxResultSize(1080, 1080)*/	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
        }
    });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK)
        {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
            Log.e("TAG", "onActivityResult: image was lauched ___________________________________________________" );
        }
    }

    private Uri createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        /*File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);*/
        File storageDir = new File(getApplicationContext().getFilesDir(), imageFileName);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".png",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
//        assert image != null;
        return FileProvider.getUriForFile(getApplicationContext(),
                "com.example.cameraintoimageview.fileProvider",
                image);
    }


    ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    imageView.setImageURI(imageUri);
                }
            });




}