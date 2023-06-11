package com.example.social_gift.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.social_gift.Adapter.UserAdapter;
import com.example.social_gift.R;
import com.example.social_gift.dao.Dao;
import com.example.social_gift.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FriendsFragment extends Fragment {
    private ArrayList<User> userlist;
    private RequestQueue rq;
    private RecyclerView rv1;
    EditText edtSearchUser;
    ArrayList<User> userArrayList;
    Dao dao;
    UserAdapter userAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        edtSearchUser = (EditText) view.findViewById(R.id.edtSearchUser);
        userlist = new ArrayList<>();
        rq = Volley.newRequestQueue(requireContext());
        dao = new Dao(requireContext());
        // Obtén la referencia al RecyclerView desde tu diseño XML
        rv1 = view.findViewById(R.id.rv1);
        cargarAmigos(dao);
        setEdtSearchUser();
        return view;
    }

    private void setEdtSearchUser() {
        edtSearchUser.setSingleLine(true);
        edtSearchUser.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER || keyCode == EditorInfo.IME_NULL) {
                    hideKeyboard();
                    if (edtSearchUser.getText().toString().isEmpty()) {
                        cargarAmigos(dao);
                    } else {
                        searchUser(edtSearchUser.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });
        edtSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                cargarAmigos(dao);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //showGiftsBySearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchUser(edtSearchUser.getText().toString());
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager manager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(edtSearchUser.getWindowToken(), 0);
    }

    private void searchUser(String name) {
        dao.searchUsers(name, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                userArrayList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        User user = new User(jsonObject.getInt("id"), jsonObject.getString("name"), jsonObject.getString("last_name"), jsonObject.getString("email"), jsonObject.getString("image"));
                        userArrayList.add(user);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
                userAdapter.setUserList(userArrayList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }



    private void cargarAmigos(Dao dao) {
        dao.getAllUsers(
                new Response.Listener<JSONArray>() {
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
                                userlist.add(user);
                            }

                            // Luego de cargar los amigos, actualiza el RecyclerView
                            actualizarRecyclerView();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error de la solicitud
                    }
                }
        );
    }

    private void actualizarRecyclerView() {
        // Crea una instancia de UserAdapter con la lista de usuarios
        userAdapter = new UserAdapter(userlist, requireContext());
        // Configura el RecyclerView con el UserAdapter
        rv1.setAdapter(userAdapter);
        rv1.setLayoutManager(new LinearLayoutManager(requireContext()));
    }


}