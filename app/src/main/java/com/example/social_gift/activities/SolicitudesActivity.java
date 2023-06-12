package com.example.social_gift.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.social_gift.Adapter.FriendAdapter;
import com.example.social_gift.Adapter.SolicitudAdapter;
import com.example.social_gift.R;
import com.example.social_gift.dao.Dao;
import com.example.social_gift.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SolicitudesActivity extends AppCompatActivity {

    private RecyclerView RecyclerViewMySolicitudesLists;
    TextView noSolicitudes;
    private Dao dao;
    private SolicitudAdapter solicitudAdapter;
    private ArrayList<User> friendsRequestList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes);
        dao = new Dao(this);
        friendsRequestList = new ArrayList<>(); // Inicializa la lista de amigos
        RecyclerViewMySolicitudesLists = findViewById(R.id.RecyclerViewMySolicitudesLists);
        noSolicitudes = findViewById(R.id.noSolicitudes);
        cargarAmigos(dao);
    }

    private void cargarAmigos(Dao dao) {
        dao.getAllRequests(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject userJson = response.getJSONObject(i);
                        int id = userJson.getInt("id");
                        String name = userJson.getString("name");
                        String lastName = userJson.getString("last_name");
                        String email = userJson.getString("email");
                        String image = userJson.getString("image");

                        // Crea una instancia de User con los datos obtenidos
                        User user = new User(id, name, lastName, email, image);
                        // Agrega el usuario a la lista
                        friendsRequestList.add(user);
                    }

                    // Luego de cargar los amigos, actualiza el RecyclerView
                    actualizarRecyclerView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Maneja el error de la solicitud
            }
        });
    }

    private void actualizarRecyclerView() {
        if(friendsRequestList.isEmpty()){
            noSolicitudes.setVisibility(View.VISIBLE);
            RecyclerViewMySolicitudesLists.setVisibility(View.GONE);
        }else{
            noSolicitudes.setVisibility(View.GONE);
            RecyclerViewMySolicitudesLists.setVisibility(View.VISIBLE);
            // Crea una instancia de UserAdapter con la lista de usuarios
            solicitudAdapter = new SolicitudAdapter(friendsRequestList,RecyclerViewMySolicitudesLists,noSolicitudes,this);
            // Configura el RecyclerView con el UserAdapter
            RecyclerViewMySolicitudesLists.setAdapter(solicitudAdapter);
            RecyclerViewMySolicitudesLists.setLayoutManager(new LinearLayoutManager(this));
        }

    }
}