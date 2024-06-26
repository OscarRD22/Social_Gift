package com.example.social_gift.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.social_gift.Adapter.FriendAdapter;
import com.example.social_gift.R;
import com.example.social_gift.activities.SolicitudesActivity;
import com.example.social_gift.dao.Dao;
import com.example.social_gift.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

    private RecyclerView RecyclerViewMyFriendsLists;
    private Dao dao;
    private FriendAdapter friendAdapter;
    private ArrayList<User> friendslist;
    TextView textSolicitudes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_booking, container, false);

        dao = new Dao(requireContext());
        textSolicitudes = view.findViewById(R.id.textSolicitudes);

        // Obtén la referencia al RecyclerView desde tu diseño XML
        RecyclerViewMyFriendsLists = view.findViewById(R.id.RecyclerViewMyFriendsLists);
        cargarAmigos(dao);
        getSolicitudes();
        textSolicitudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SolicitudesActivity.class);
                // Inicio la activity
                startActivity(intent);
            }
        });
        return view;
    }


    private void getSolicitudes() {
        dao.getAllRequests(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                textSolicitudes.setText(requireContext().getResources().getString(R.string.numSolicitudes, response.length()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

    }

    private void cargarAmigos(Dao dao) {

        dao.getAllFriends(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                friendslist = new ArrayList<>(); // Inicializa la lista de amigos

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
                        friendslist.add(user);
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
        // Crea una instancia de UserAdapter con la lista de usuarios
        friendAdapter = new FriendAdapter(friendslist, requireContext());
        // Configura el RecyclerView con el UserAdapter
        RecyclerViewMyFriendsLists.setAdapter(friendAdapter);
        RecyclerViewMyFriendsLists.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarAmigos(dao);
        getSolicitudes();
    }
}
