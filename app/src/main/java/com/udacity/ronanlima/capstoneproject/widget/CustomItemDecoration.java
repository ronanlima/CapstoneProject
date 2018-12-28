package com.udacity.ronanlima.capstoneproject.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * created by ronanlima
 *
 * @since december/2018
 */
public class CustomItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;

    public CustomItemDecoration(Drawable mDrawable) {
        this.mDivider = mDrawable;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (state.getItemCount() - 1 == parent.getChildAdapterPosition(view)) {
            outRect.right = mDivider.getIntrinsicHeight();
        }
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int dividerRight = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            mDivider.setBounds(0, 0, dividerRight, 0);
            mDivider.draw(canvas);
        }
    }
}
