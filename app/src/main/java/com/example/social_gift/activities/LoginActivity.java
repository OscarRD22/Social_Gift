package com.example.social_gift.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.social_gift.R;
import com.example.social_gift.dao.Dao;
import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {

    private EditText userName, password;
    private TextView createAcountTextView, createNewPasswordTextView;
    private Button btn_login;
    private Dao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new Dao(getApplicationContext());

        if (dao.loadTokenSharedPreferences(getApplicationContext()) != null) {
            startMainActivity();
        } else {
            setContentView(R.layout.activity_login);

            userName = findViewById(R.id.usernameEdit);
            password = findViewById(R.id.passwordEditText);
            btn_login = findViewById(R.id.btn_register);
            createNewPasswordTextView = findViewById(R.id.changePassword);
            createAcountTextView = findViewById(R.id.createAcountTextView);

            btn_login.setOnClickListener(v -> {
                login();
                hideKeyboard();
            });

            createNewPasswordTextView.setOnClickListener(v -> {
                Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                hideKeyboard();
            });

            createAcountTextView.setOnClickListener(v -> {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                hideKeyboard();
            });
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        finish();
        startActivity(intent);
    }

    private void login() {
        String username = userName.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        dao.loginUser(username, userPassword, response -> {
            try {
                String accessToken = response.getString("accessToken");
                dao.saveTokenSharedPreferences(accessToken, getApplicationContext());
                getUserId(username);
                startMainActivity();
                Toast.makeText(LoginActivity.this, "LOGIN CORRECTO", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                Toast.makeText(LoginActivity.this, "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(LoginActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
        });
    }

    private void getUserId(String email) {
        dao.getUserId(email, response -> {
            try {
                if (response.length() > 0) {
                    int id = response.getJSONObject(0).getInt("id");
                    dao.saveIdSharedPreferences(id, getApplicationContext());
                    dao.savePasswordSharedPreferences(password.getText().toString(), getApplicationContext());
                } else {
                    Toast.makeText(LoginActivity.this, "No se encontró ningún usuario con ese email", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(LoginActivity.this, "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(LoginActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}
