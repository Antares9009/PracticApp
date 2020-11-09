package com.antares.practicapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserActivity extends AppCompatActivity {

    private static final int MY_CAMERA_REQUEST_CODE = 10;
    @BindView(R.id.btn_camera)
    ImageView btn_camera;
    @BindView(R.id.img_preview)
    ImageView img_preview;
    TextView txtEmail;
    ImageView btnLogout;

    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);

        txtEmail = findViewById(R.id.txtEmail);
        btnLogout = findViewById(R.id.btnLogout);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            txtEmail.setText(user.getEmail());
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @OnClick(R.id.btn_camera)
    void onCaptureImageClick(){
        //Check permission
        Dexter.withContext(this)
                .withPermissions(Arrays.asList(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                ))
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if(multiplePermissionsReport.areAllPermissionsGranted())
                        {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(MediaStore.Images.Media.TITLE, "New Picture");
                            contentValues.put(MediaStore.Images.Media.DESCRIPTION,"From your camera");
                            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                            startActivityForResult(intent,MY_CAMERA_REQUEST_CODE);
                        }else{
                            Toast.makeText(UserActivity.this,"Debe aceptar todos los permisos", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MY_CAMERA_REQUEST_CODE){
            if(resultCode== Activity.RESULT_OK){
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                    img_preview.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}