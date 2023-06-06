package com.example.social_gift.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.social_gift.R;
import com.example.social_gift.dao.Dao;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity {

    private ImageButton btn_back;
    private EditText actualPassword, newPassword, twoNewPassword;
    private String name, lastname, image, email;
    private Button save;
    private Dao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        dao = new Dao(getApplicationContext());

        btn_back = findViewById(R.id.btn_back);
        actualPassword = findViewById(R.id.actualPassword);
        newPassword = findViewById(R.id.newPassword);
        twoNewPassword = findViewById(R.id.twoNewPassword);
        save = findViewById(R.id.btn_save_password);

        btn_back.setOnClickListener(v -> {
            finish();
        });

        save.setOnClickListener(view -> {
            if (validateFields()) {
                getUserDetails();
            }
        });
    }

    private boolean validateFields() {
        String actualPass = actualPassword.getText().toString().trim();
        String newPass = newPassword.getText().toString().trim();
        String confirmPass = twoNewPassword.getText().toString().trim();

        if (TextUtils.isEmpty(actualPass)) {
            actualPassword.setError("Please enter your current password");
            return false;
        }

        if (TextUtils.isEmpty(newPass)) {
            newPassword.setError("Please enter a new password");
            return false;
        }

        if (TextUtils.isEmpty(confirmPass)) {
            twoNewPassword.setError("Please confirm your new password");
            return false;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void getUserDetails() {
        int userId = dao.loadIdSharedPreferences(getApplicationContext());
        dao.getMyUser(userId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    name = response.getString("name");
                    lastname = response.getString("last_name");
                    email = response.getString("email");
                    image = response.getString("image");
                    updatePassword();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChangePasswordActivity.this, "Error retrieving user details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePassword() {
        dao.editUser(name, lastname, email, newPassword.getText().toString(), image, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(ChangePasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChangePasswordActivity.this, "Error updating password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
