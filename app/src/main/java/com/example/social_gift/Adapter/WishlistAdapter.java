package com.example.social_gift.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.social_gift.R;
import com.example.social_gift.model.WishList;
import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder>{

    private List<WishList> lists;
    private Context context;

    private LayoutInflater layoutInflater;
    WishlistAdapter.OnItemClick itemClick;

    public interface OnItemClick{
        void onItemClickInterface(WishList wishList);
    }

    public WishlistAdapter(List<WishList> lists, Context context, WishlistAdapter.OnItemClick onItemClick) {
        this.lists = lists;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.itemClick = onItemClick;
    }


    @NonNull
    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_wishlist, parent, false);
        return new WishlistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewHolder holder, int position) {
        WishList wishList = lists.get(position);
        holder.textName.setText(context.getResources().getString(R.string.wishListName, wishList.getName()));
        holder.textDescription.setText(context.getResources().getString(R.string.wishListDescription, wishList.getDescription()));
        holder.textStartDate.setText(context.getResources().getString(R.string.wishListInitialDate, wishList.getCreation_date()));
        holder.textEndDate.setText(context.getResources().getString(R.string.wishListEndDate, wishList.getEnd_date()));
        holder.numGift.setText(context.getResources().getString(R.string.wishListNumberGift, wishList.getListGifts().size()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Notificar el evento de clic al listener
                if (itemClick != null) {
                    itemClick.onItemClickInterface(wishList);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textDescription, textEndDate,textStartDate,numGift;
        View itemView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.txtNombre);
            textDescription = itemView.findViewById(R.id.txtDescripcion);
            textStartDate = itemView.findViewById(R.id.txtFechaCreacion);
            textEndDate = itemView.findViewById(R.id.txtFechaFinal);
            numGift = itemView.findViewById(R.id.txtNumeroRegalos);
            this.itemView=itemView;
        }
    }
}
