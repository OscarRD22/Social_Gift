package com.example.social_gift.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.social_gift.Adapter.DecorationBoxAdapter;
import com.example.social_gift.Adapter.WishlistAdapter;
import com.example.social_gift.R;
import com.example.social_gift.activities.EditListActivity;
import com.example.social_gift.activities.LoginActivity;
import com.example.social_gift.activities.ProfileActivity;
import com.example.social_gift.dao.Dao;
import com.example.social_gift.dao.DaoImage;
import com.example.social_gift.model.User;
import com.example.social_gift.model.WishList;
import com.example.social_gift.model.WishListGift;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    TextView editProfile, name, email;
    Button btnLogOut;
    ArrayList<WishList> wishlist;
    RecyclerView recyclerViewMyLists;
    ImageView imgUser;
    ImageButton imageButton;
    Dao dao;
    DaoImage daoImage;
    User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        editProfile = view.findViewById(R.id.editProfileFrag);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        name = view.findViewById(R.id.profileName);
        email = view.findViewById(R.id.emailProfile);
        imgUser = view.findViewById(R.id.profile_picture);
        recyclerViewMyLists = view.findViewById(R.id.RecyclerViewMyLists);
        dao = new Dao(requireContext());
        daoImage = new DaoImage();
        addData();
        cargarListas();

        return view;
    }

    private void addData() {
        dao.getMyUser(dao.loadIdSharedPreferences(requireContext()), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Glide.with(requireContext()).load(response.getString("image")).apply(RequestOptions.circleCropTransform()).into(imgUser);
                    name.setText(response.getString("name") + " " + response.getString("last_name"));
                    email.setText(response.getString("email"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireActivity(), "ERROR: Problemas al recibir el usuario", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //btn Editar Perfil
        editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);

            // Inicio la activity
            startActivity(intent);
        });


        //btn Log Out
        btnLogOut.setOnClickListener(v -> {
            dao.deleteTokenSharedPreferences(requireContext());
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            // Inicio la activity
            startActivity(intent);
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

    private void moveInfoToAdapter(ArrayList<WishList> wishlist) {
        if (wishlist.isEmpty()) {
            recyclerViewMyLists.setVisibility(View.GONE);
        } else {
           // noLists.setVisibility(View.GONE);
            recyclerViewMyLists.setVisibility(View.VISIBLE);

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
            recyclerViewMyLists.addItemDecoration(decorationBoxAdapter);
            recyclerViewMyLists.setHasFixedSize(true);
            recyclerViewMyLists.setLayoutManager(new LinearLayoutManager(requireActivity()));
            recyclerViewMyLists.setAdapter(wishlistAdapter);
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        addData();
    }
}
