package com.udacity.ronanlima.capstoneproject.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Class that represents imagemCapa from project on firebase database
 *
 * @author ronanlima
 */
public class ProportionThreeTwoImageView extends AppCompatImageView {
    public ProportionThreeTwoImageView(Context context) {
        super(context);
    }

    public ProportionThreeTwoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProportionThreeTwoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int threeTwoHeight = MeasureSpec.getSize(widthMeasureSpec) * 2 / 3;
        int threeTwoHeightSpec = MeasureSpec.makeMeasureSpec(threeTwoHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, threeTwoHeightSpec);
    }
}
