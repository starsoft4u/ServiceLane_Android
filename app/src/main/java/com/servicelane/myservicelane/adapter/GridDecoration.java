package com.servicelane.myservicelane.adapter;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridDecoration extends RecyclerView.ItemDecoration {
    private int skip;
    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridDecoration(int spanCount, int spacing, boolean includeEdge) {
        this(spanCount, spacing, includeEdge, 0);
    }

    public GridDecoration(int spanCount, int spacing, boolean includeEdge, int skip) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
        this.skip = skip;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position

        if (position < skip) {
            outRect.top = 0;
            outRect.left = 0;
            outRect.right = 0;
            outRect.bottom = 0;
        } else {
            int column = (position - skip) % spanCount; // item column
            int row = (position - skip) / spanCount;

            if (includeEdge) {
                outRect.top = row == 0 ? spacing : 0;
                outRect.bottom = spacing;
                outRect.left = column == 0 ? spacing : spacing / 2;
                outRect.right = column == spanCount - 1 ? spacing : spacing / 2;
            } else {
                outRect.top = row == 0 ? 0 : spacing;
                outRect.bottom = 0;
                outRect.left = column == 0 ? 0 : spacing / 2;
                outRect.right = column == spanCount - 1 ? 0 : spacing / 2;
            }
        }
    }
}
