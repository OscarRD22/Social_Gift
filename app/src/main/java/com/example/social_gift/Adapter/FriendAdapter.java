package com.example.social_gift.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.social_gift.R;
import com.example.social_gift.activities.FriendProfileActivity;
import com.example.social_gift.activities.LoginActivity;
import com.example.social_gift.activities.RegisterActivity;
import com.example.social_gift.dao.Dao;
import com.example.social_gift.model.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>{
    private List<User> userList;
    private Context context;
    Dao dao;
    Button buttonViewProfile;
    public FriendAdapter(List<User> userList, Context context) {
        this.userList = userList;
        dao = new Dao(context);
        this.context = context;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
        //notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_user, parent, false);
        return new FriendAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendAdapter.ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textName.setText(user.getName());
        holder.textEmail.setText(user.getEmail());
        Glide.with(context).load(user.getImage()).error(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_launcher, null)).apply(RequestOptions.circleCropTransform()).into(holder.imgFoto);        // Configurar otros elementos de diseño según tus necesidades

        holder.buttonViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = userList.get(position);
                int userId = user.getId();

                Intent intent = new Intent(context, FriendProfileActivity.class);
                intent.putExtra("userId", userId); // Pasa el ID del usuario a la actividad FriendProfileActivity
                context.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textEmail;
        ImageView imgFoto;
        Button buttonViewProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textEmail = itemView.findViewById(R.id.textEmail);
            imgFoto = itemView.findViewById(R.id.imgFoto);
            buttonViewProfile = itemView.findViewById(R.id.buttonViewProfile);
        }


    }

}
