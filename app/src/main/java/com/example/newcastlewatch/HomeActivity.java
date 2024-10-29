package com.example.newcastlewatch;

import static com.example.newcastlewatch.R.id.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.newcastlewatch.ml.Newcastle1;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.example.newcastlewatch.R;
import com.example.newcastlewatch.Classifier;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {


    ImageView camera, gallery, imageView;
    TextView diagnosisName, confidencePercentage;
    int imageSize = 224;
    private Classifier classifier;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        camera = findViewById(R.id.icon_camera);
        gallery = findViewById(R.id.add_image);
        diagnosisName = findViewById(R.id.diagnosis_name);
        confidencePercentage = findViewById(R.id.confidence_percentage);
        imageView = findViewById(R.id.image_added);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setSelectedItemId(home);

        bottomNavigationView.setOnItemSelectedListener(item -> {

             int itemId = item.getItemId();

             if (itemId == R.id.home) {
                 return true;
             } else if (itemId == R.id.history) {
                 startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                 overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                 finish();
                 return true;
             } else if (itemId == R.id.tips) {
                 startActivity(new Intent(getApplicationContext(), TipsActivity.class));
                 overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                 finish();
                 return true;
             }
             return false;
        });




        try {
//           Initialize the TFLite model
            classifier = new Classifier(this);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing TFLite model", e);
        }


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 12);
            }
        });

        // Request camera permission if not granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 11);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 12 && resultCode == RESULT_OK && data != null) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            int dimension = Math.min(image.getWidth(), image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
            imageView.setImageBitmap(image);

            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            classifyImage(image);
        } else if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            } catch (IOException e) {
                e.printStackTrace();
                diagnosisName.setText("Error loading image!");
                confidencePercentage.setText("");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void classifyImage(Bitmap image) {
        Classifier.Result result = classifier.classify(image);



        String[] classes = {"Not fecal image","Healthy","Newcastle","Unhealthy"};
        diagnosisName.setText(classes[result.getIndex()]);
        confidencePercentage.setText(String.format("%.1f%%", result.getConfidence() * 100));


    }

    @Override
    protected void onDestroy() {
        if (classifier != null) {
            classifier.close();
        }
        super.onDestroy();
    }
}
