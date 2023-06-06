package com.example.social_gift.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.social_gift.R;
import com.example.social_gift.dao.Dao;
import com.example.social_gift.dao.DaoImage;
import com.example.social_gift.model.User;

import org.json.JSONException;

import java.io.File;

public class ProfileActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_GALLERY = 1;

    TextView changePassw;
    EditText usernameEdit, lastNameEdit, emailEditText;
    Button btn_editUser;
    ImageButton btn_back, btn_edit_profile_picture_button;
    String imageUrl;
    Dao dao;
    ImageView imgProfile;
    User user;
    DaoImage daoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        dao = new Dao(getApplicationContext());
        daoImage = new DaoImage();
        changePassw = findViewById(R.id.change_password);
        btn_back = findViewById(R.id.imageButtonBack);
        btn_editUser = findViewById(R.id.btn_editUser);
        btn_edit_profile_picture_button = findViewById(R.id.edit_profile_picture_button);
        emailEditText = findViewById(R.id.emailEditText);
        lastNameEdit = findViewById(R.id.lastNameEdit);
        usernameEdit = findViewById(R.id.usernameEdit);
        imgProfile = findViewById(R.id.edit_profile_picture);
        getData();

        btn_back.setOnClickListener(v -> finish());

        btn_editUser.setOnClickListener(v -> updateData());

        btn_edit_profile_picture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                someActivityResultLauncher.launch(intent);
            }
        });

        changePassw.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, ChangePasswordActivity.class)));
    }

    private void updateData() {
        dao.editUser(usernameEdit.getText().toString(), lastNameEdit.getText().toString(),
                emailEditText.getText().toString(), dao.loadPasswordSharedPreferences(getApplicationContext()), user.getImage(),
                response -> {
                    Log.wtf("HOLAAAA", "HOLAAAA" + user.getImage());
                    Toast.makeText(ProfileActivity.this, "DATOS ACTUALIZADOS", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> Toast.makeText(ProfileActivity.this, "ERROR", Toast.LENGTH_SHORT).show());
    }

    private void getData() {
        dao.getMyUser(dao.loadIdSharedPreferences(getApplicationContext()),
                response -> {
                    try {
                        usernameEdit.setHint(response.getString("name"));
                        lastNameEdit.setHint(response.getString("last_name"));
                        emailEditText.setHint(response.getString("email"));
                        imageUrl = response.getString("image");
                        Glide.with(getApplicationContext())
                                .load(response.getString("image"))
                                .error(ResourcesCompat.getDrawable(getApplicationContext().getResources(), R.drawable.ic_launcher, null))
                                .apply(RequestOptions.circleCropTransform())
                                .into(imgProfile);
                        user = new User();
                        user.setName(response.getString("name"));
                        user.setEmail(response.getString("email"));
                        user.setLast_name(response.getString("last_name"));
                        user.setImage(response.getString("image"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                });
    }

    private final ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        String path = getFileFromUri(selectedImage);
                        Glide.with(getApplicationContext()).load(path).apply(RequestOptions.circleCropTransform()).into(imgProfile);
                        daoImage.uploadFile(new File(path), new DaoImage.DaoRepositoryImagesListener() {
                            @Override
                            public void onSuccess(String url) {
                                ProfileActivity.this.setImage(url);
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("Error", error);
                            }
                        });

                    }
                }
            });

    private String getFileFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        } else {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
    }

    public void setImage(String url) {
        Log.wtf("HOLAAA", "HOLAAAA" + url);
        user.setImage(url);
    }
}
