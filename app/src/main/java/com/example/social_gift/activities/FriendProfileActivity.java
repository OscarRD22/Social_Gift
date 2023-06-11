package com.example.social_gift.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.social_gift.Adapter.DecorationBoxAdapter;
import com.example.social_gift.Adapter.WishlistAdapter;
import com.example.social_gift.R;
import com.example.social_gift.dao.Dao;
import com.example.social_gift.model.User;
import com.example.social_gift.model.WishList;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.social_gift.model.WishListGift;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FriendProfileActivity extends AppCompatActivity {
    private TextView textName, textListName;
    private TextView textEmail;
    private ImageView imgProfile;
    private RecyclerView recyclerViewLists;
    private ArrayList<WishList> wishlist;
    private Dao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        // Obtén el ID del usuario del intent
        int userId = getIntent().getIntExtra("userId", 0);

        // Inicializa las vistas
        textName = findViewById(R.id.profileFriendName);
        textEmail = findViewById(R.id.profileFriendEmail);
        imgProfile = findViewById(R.id.profile_friend_picture);
        recyclerViewLists = findViewById(R.id.RecyclerViewFriendsLists);
        textListName = findViewById(R.id.textView8);
        // Configura el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewLists.setLayoutManager(layoutManager);

        // Inicializa el objeto Dao
        dao = new Dao(this);

        // Carga los datos del amigo
        loadFriendData(userId);
        getFriendWishlists(userId);
    }

    private void loadFriendData(int userId) {
        dao.getUserInfo(userId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String name = response.getString("name");
                    String lastName = response.getString("last_name");
                    String email = response.getString("email");
                    String image = response.getString("image");

                    // Actualiza las vistas con los datos del amigo
                    textName.setText(name + " " + lastName);
                    textEmail.setText(email);
                    Glide.with(FriendProfileActivity.this)
                            .load(image)
                            .apply(RequestOptions.circleCropTransform())
                            .into(imgProfile);
                    textListName.setText(getResources().getString(R.string.listasDE, name + " " + lastName));

                    // Obtiene las listas de deseos del amigo
                    getFriendWishlists(userId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FriendProfileActivity.this, "Error al cargar los datos del amigo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFriendWishlists(int userId) {
        dao.getFriendWishlists(userId,response -> {
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
            recyclerViewLists.setVisibility(View.GONE);
        } else {
            //noLists.setVisibility(View.GONE);
            recyclerViewLists.setVisibility(View.VISIBLE);

            WishlistAdapter wishlistAdapter = new WishlistAdapter(wishlist, this, new WishlistAdapter.OnItemClick() {
                @Override
                public void onItemClickInterface(WishList wishList) {
                    Intent intent = new Intent(getApplicationContext(), EditListActivity.class);
                    intent.putExtra("wishlist", wishList);
                    startActivity(intent);
                }
            }
                    // Handle wishlist item click
            );

            int spanCount = 1, spacing = 3;
            DecorationBoxAdapter decorationBoxAdapter = new DecorationBoxAdapter(spanCount, spacing);
            recyclerViewLists.addItemDecoration(decorationBoxAdapter);
            recyclerViewLists.setHasFixedSize(true);
            recyclerViewLists.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewLists.setAdapter(wishlistAdapter);
        }
    }
}
