package com.example.social_gift.Adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
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
import com.example.social_gift.activities.ProfileActivity;
import com.example.social_gift.dao.Dao;
import com.example.social_gift.model.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> userList;
    private Context context;
    Dao dao;
    public UserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        dao = new Dao(context);
        this.context = context;
    }

    public void setUserList(ArrayList<User> userList){
        this.userList = userList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textName.setText(user.getName());
        holder.textEmail.setText(user.getEmail());
        Glide.with(context).load(user.getImage()).error(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_launcher, null)).apply(RequestOptions.circleCropTransform()).into(holder.imgFoto);        // Configurar otros elementos de diseño según tus necesidades

        holder.buttonFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    User user = userList.get(position);
                  int userId = user.getId();
                    dao.enviarSolicitudAmistad(userId, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Solicitud de amistad enviada exitosamente
                            Toast.makeText(context, "SOLICITUD ENVIADA CON ÉXITO", Toast.LENGTH_SHORT).show();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Se produjo un error al enviar la solicitud de amistad
                            Toast.makeText(context, "YA HAS ENVIADO UNA SOLICITUD A ESTE USUARIO", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
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
        Button buttonFollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textEmail = itemView.findViewById(R.id.textEmail);
            imgFoto = itemView.findViewById(R.id.imgFoto);
            buttonFollow = itemView.findViewById(R.id.buttonFollow);
        }



    }
}

