package com.example.social_gift.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.social_gift.Adapter.DecorationBoxAdapter;
import com.example.social_gift.Adapter.WishlistAdapter;
import com.example.social_gift.R;
import com.example.social_gift.activities.EditListActivity;
import com.example.social_gift.dao.Dao;
import com.example.social_gift.model.WishList;
import com.example.social_gift.model.WishListGift;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListsFragment extends Fragment {

    private ArrayList<WishList> wishlist;
    private RequestQueue rq;
    private RecyclerView list_recycler;
    private TextView noLists;
    private Button createNewList;
    private EditText nameEditText, descriptionEditText;

    private Dao dao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lists, container, false);
        rq = Volley.newRequestQueue(requireContext());
        dao = new Dao(requireContext());
        createNewList = view.findViewById(R.id.createNewList);
        list_recycler = view.findViewById(R.id.list_recycler);
        noLists = view.findViewById(R.id.noLists);

        cargarListas();

        createNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });
        return view;
    }


    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("CREAR NUEVA LISTA");

        // Mostrar el diseño personalizado para el diálogo
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_new_list, null);
        builder.setView(dialogView);

        nameEditText = dialogView.findViewById(R.id.nameEditText);
        descriptionEditText = dialogView.findViewById(R.id.descriptionEditText);

        // Obtener los valores actuales de la lista y establecerlos en los campos de edición
        String currentName = "Nombre de la lista";
        String currentDescription = "Descripción de la lista";
        nameEditText.setText(currentName);
        descriptionEditText.setText(currentDescription);

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Obtener los nuevos valores de nombre y descripción
                String newName = nameEditText.getText().toString();
                String newDescription = descriptionEditText.getText().toString();

                // Realizar las acciones necesarias con los nuevos valores (por ejemplo, actualizar la lista)
                createNewList(newName, newDescription, dao);
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancelar la edición, no realizar ninguna acción
            }
        });

        // Mostrar el diálogo emergente
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createNewList(String name, String description, Dao dao) {
        // Crear una nueva instancia de la lista con los datos proporcionados
        WishList newList = new WishList(0, name, description, 0, "", "");

        // Llamar al método createWishList para enviar la solicitud de creación de la lista al servidor
        dao.createWishList(newList, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // La lista se ha creado correctamente
                Toast.makeText(requireActivity(), "La lista se ha creado correctamente", Toast.LENGTH_SHORT).show();
                cargarListas();
                // Realiza las acciones necesarias después de la creación exitosa
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Se produjo un error al crear la lista
                Toast.makeText(requireActivity(), "ERROR: Error al crear la lista", Toast.LENGTH_SHORT).show();

                // Maneja el error de acuerdo a tus necesidades
            }
        });
    }


    private void cargarListas() {
        dao.getMyLists(response -> {
            wishlist = new ArrayList<>();
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);
                    WishList list = new WishList(jsonObject.getInt("id"), jsonObject.getString("name"),
                            jsonObject.getString("description"), jsonObject.getInt("user_id"),
                            jsonObject.getString("creation_date"), jsonObject.getString("end_date"));
                    JSONArray jsonArray = jsonObject.getJSONArray("gifts");
                    ArrayList<WishListGift> wishListGiftArrayList = new ArrayList<>();
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObjectGift = jsonArray.getJSONObject(j);
                        WishListGift wishListGift = new WishListGift(jsonObjectGift.getInt("id"),
                                jsonObjectGift.getInt("wishlist_id"), jsonObjectGift.getString("product_url"),
                                jsonObjectGift.getInt("priority"), jsonObjectGift.getInt("booked"));
                        wishListGiftArrayList.add(wishListGift);
                    }
                    list.setListGifts(wishListGiftArrayList);
                    wishlist.add(list);
                }
                moveInfoToAdapter(wishlist);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            // Handle error response
        });
    }

    private void moveInfoToAdapter(ArrayList<WishList>wishlist) {
        if (wishlist.isEmpty()) {
            list_recycler.setVisibility(View.GONE);
        } else {
            noLists.setVisibility(View.GONE);
            list_recycler.setVisibility(View.VISIBLE);

            WishlistAdapter wishlistAdapter = new WishlistAdapter(wishlist, requireActivity(), new WishlistAdapter.OnItemClick() {
                @Override
                public void onItemClickInterface(WishList wishList) {
                    Intent intent = new Intent(requireActivity(), EditListActivity.class);
                    intent.putExtra("wishlist", wishList);
                    startActivity(intent);
                }
            }
                    // Handle wishlist item click
            );

            int spanCount = 1, spacing = 3;
            DecorationBoxAdapter decorationBoxAdapter = new DecorationBoxAdapter(spanCount, spacing);
            list_recycler.addItemDecoration(decorationBoxAdapter);
            list_recycler.setHasFixedSize(true);
            list_recycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
            list_recycler.setAdapter(wishlistAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarListas();
    }
}
