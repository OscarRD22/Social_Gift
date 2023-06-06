package com.example.social_gift.Adapter;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class DecorationBoxAdapter extends RecyclerView.ItemDecoration {
    private int spanCount;
    private int spacing;

    public DecorationBoxAdapter(int spanCount, int spacing) {
        this.spanCount = spanCount;
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);

        int width = parent.getWidth() - (spacing * 2);
        int height = width / spanCount;
        view.getLayoutParams().width = width;
        //view.getLayoutParams().height = height;
        outRect.bottom = spacing;
    }
}
