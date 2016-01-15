package com.wordpress.omanandj.popularmovies.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by ojha on 10/01/16.
 *
 * Ref: http://blog.lovelyhq.com/setting-listview-height-depending-on-the-items/
 */
public class FixedHeightListView extends ListView
{
    public FixedHeightListView(Context context)
    {
        super(context);
    }

    public FixedHeightListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public FixedHeightListView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        ListAdapter listAdapter = getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, this);
                item.measure(widthMeasureSpec, MeasureSpec.UNSPECIFIED);
                totalItemsHeight += item.getMeasuredHeight();
            }

            int totalDividersHeight = getDividerHeight() * (numberOfItems - 1);

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), (totalItemsHeight + totalDividersHeight));

        }
        else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
