package com.example.social_gift.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.social_gift.R;
import com.example.social_gift.dao.Dao;
import com.example.social_gift.model.WishList;

import org.json.JSONObject;

import java.util.Calendar;

public class EditListActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextDescription;
    WishList wishList;
    private Button buttonSave, buttonDelete;
    Dao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);
        dao = new Dao(getApplicationContext());
        wishList = (WishList) getIntent().getSerializableExtra("wishlist");


        // Obtener referencias de los elementos de interfaz de usuario
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);

        // Obtener los valores iniciales de la lista
        String initialName = getIntent().getStringExtra("name");
        String initialDescription = getIntent().getStringExtra("description");
        long initialStartDate = getIntent().getLongExtra("startDate", 0);
        long initialEndDate = getIntent().getLongExtra("endDate", 0);

        // Establecer los valores iniciales en los elementos de interfaz de usuario
        editTextName.setHint(wishList.getName());
        editTextDescription.setHint(wishList.getDescription());

        Calendar calendarStartDate = Calendar.getInstance();
        calendarStartDate.setTimeInMillis(initialStartDate);

        Calendar calendarEndDate = Calendar.getInstance();
        calendarEndDate.setTimeInMillis(initialEndDate);


        // Configurar el botón Guardar para guardar los cambios
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName;
                String newDescription;
                // Obtener los valores editados
                if(editTextName.getText().toString().isEmpty()){
                    newName = editTextName.getHint().toString();
                }else{
                    newName = editTextName.getText().toString();
                }

                if(editTextDescription.getText().toString().isEmpty()){
                    newDescription = editTextDescription.getHint().toString();
                }else{
                    newDescription = editTextDescription.getText().toString();
                }
                dao.editWishList(wishList.getId(), newName, newDescription, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });



            }
        });

        // Configurar el botón Guardar para guardar los cambios
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dao.deleteWishList(wishList.getId(), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        finish(); // Cerrar la actividad actual
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            }
        });
    }
}
