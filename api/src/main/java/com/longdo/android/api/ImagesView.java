package com.longdo.android.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ImagesView extends RecyclerView {

    private ImagesViewAdapter adapter;
    private List<ImageData> imagesList;

    public ImagesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.BLACK);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false);
        setLayoutManager(mLayoutManager);
        setItemAnimator(new DefaultItemAnimator());

        imagesList = new ArrayList<>();
        adapter = new ImagesViewAdapter(context, imagesList);
        setAdapter(adapter);
    }

    public void addImage(Bitmap bm, String title, String owner, String date){
        imagesList.add(new ImageData(bm,title,owner,date));
        adapter.notifyDataSetChanged();
    }

    public void addImage(String url, String title, String owner, String date){
        imagesList.add(new ImageData(url,title,owner,date));
        adapter.notifyDataSetChanged();
    }

    public void addImage(int resourceId, String title, String owner, String date){
        imagesList.add(new ImageData(resourceId,title,owner,date));
        adapter.notifyDataSetChanged();
    }

    public void addImage(Drawable drawable, String title, String owner, String date){
        imagesList.add(new ImageData(drawable,title,owner,date));
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        View v = findChildViewUnder(e.getX(),e.getY());
        ImagesViewAdapter.ViewHolder vh = (ImagesViewAdapter.ViewHolder) getChildViewHolder(v);
        return super.onInterceptTouchEvent(e) && e.getPointerCount() == 1 && !vh.tiv.isZoomed() && imagesList.size() > 1;
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        int targetPosition = getChildAdapterPosition(findChildViewUnder(getScrollX(),getScaleY()));
        if(velocityX > 0){
            targetPosition++;
        }

        int newPosition = Math.min(imagesList.size()-1,Math.max(0,targetPosition));
        smoothScrollToPosition(newPosition);

        return true;
    }

    @Override
    public void smoothScrollToPosition(int position) {
        /* custom scroll behavior goes here */
        // int[] itemPosition = new int[2];
        // ImagesViewAdapter.ViewHolder vh = (ImagesViewAdapter.ViewHolder) findViewHolderForAdapterPosition(position);
        // View v = vh.v;
        // v.getLocationInWindow(itemPosition);
        // scrollBy(itemPosition[0],itemPosition[0]);

        super.smoothScrollToPosition(position);
    }
}
