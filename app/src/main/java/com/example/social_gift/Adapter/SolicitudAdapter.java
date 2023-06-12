package com.example.social_gift.Adapter;

import android.content.Context;
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
import com.example.social_gift.dao.Dao;
import com.example.social_gift.model.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SolicitudAdapter extends RecyclerView.Adapter<SolicitudAdapter.ViewHolder>{
    private List<User> userList;
    private Context context;

    RecyclerView RecyclerViewMySolicitudesLists;
    TextView noSolicitudes;
    Dao dao;
    public SolicitudAdapter(List<User> userList,RecyclerView RecyclerViewMySolicitudesLists,TextView noSolicitudes,Context context) {
        this.userList = userList;
        dao = new Dao(context);
        this.RecyclerViewMySolicitudesLists = RecyclerViewMySolicitudesLists;
        this.noSolicitudes = noSolicitudes;
        this.context = context;
    }

    public void setUserList(ArrayList<User> userList){
        this.userList = userList;
    }
    @NonNull
    @Override
    public SolicitudAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_request, parent, false);
        return new SolicitudAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitudAdapter.ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textName.setText(user.getName());
        holder.textEmail.setText(user.getEmail());
        Glide.with(context).load(user.getImage()).error(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_launcher, null)).apply(RequestOptions.circleCropTransform()).into(holder.imgFoto);        // Configurar otros elementos de diseño según tus necesidades

        //Agregar amigo

        holder.buttonAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dao.acceptFriendSolicitud(user.getId(), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        notifyItemRemoved(userList.indexOf(user));
                        notifyItemRangeChanged(userList.indexOf(user),getItemCount());
                        userList.remove(user);
                        Toast.makeText (context, "You accept " +user.getEmail()+"'s friend request", Toast. LENGTH_SHORT).show();
                        if(userList.isEmpty()){
                            RecyclerViewMySolicitudesLists.setVisibility(View.GONE);
                            noSolicitudes.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast. makeText (context, "Error: "+error.getMessage (), Toast. LENGTH_SHORT).show();
                    }
                });
            }
        });

//Eliminar amigo
        holder.buttonDeleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dao.deleteFriendSolicitud(user.getId(), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        notifyItemRemoved(userList.indexOf(user));
                        notifyItemRangeChanged(userList.indexOf(user),getItemCount());
                        userList.remove(user);
                        Toast.makeText (context, "You declined " +user.getEmail()+"'s friend request", Toast. LENGTH_SHORT).show();
                        if(userList.isEmpty()){
                            RecyclerViewMySolicitudesLists.setVisibility(View.GONE);
                            noSolicitudes.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast. makeText (context, "Error: "+error.getMessage (), Toast. LENGTH_SHORT).show();
                    }
                });
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
        Button buttonAddFriend,buttonDeleteFriend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textEmail = itemView.findViewById(R.id.textEmail);
            imgFoto = itemView.findViewById(R.id.imgFoto);
            buttonAddFriend = itemView.findViewById(R.id.buttonFriendAdd);
            buttonDeleteFriend = itemView.findViewById(R.id.buttonFriendDelete);


        }



    }

}
