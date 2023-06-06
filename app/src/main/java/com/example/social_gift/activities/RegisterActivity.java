package com.example.social_gift.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.social_gift.R;
import com.example.social_gift.dao.Dao;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    EditText userName, lastName, email, password, passwordConfirm;
    Button btn_register;
    ImageButton btn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dao dao = new Dao(getApplicationContext());
        setContentView(R.layout.activity_register);

        userName = findViewById(R.id.usernameEdit);
        lastName = findViewById(R.id.lastNameEdit);
        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);
        passwordConfirm = findViewById(R.id.passwordEditText2);
        btn_back = findViewById(R.id.btn_back);


        btn_register = findViewById(R.id.btn_register);


        btn_back.setOnClickListener(v -> {
            finish();
        });


        btn_register.setOnClickListener(v -> {
            register(dao);
        });
    }


    public void register(Dao dao) {
        dao.registerUser(userName.getText().toString(), lastName.getText().toString(), email.getText().toString(), password.getText().toString(), "https://balandrau.salle.url.edu/i3/repositoryimages/photo/47601a8b-dc7f-41a2-a53b-19d2e8f54cd0.png", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Comprobar si ha ido bien o mal

                //Abro siguiente pantalla
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                //No back screen
                finish();
                startActivity(intent);
                Toast.makeText(RegisterActivity.this, "USUARIO REGISTRADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "ERROR USUARIO NO REGISTRADO", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

