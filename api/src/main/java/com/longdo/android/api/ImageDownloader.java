package com.longdo.android.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloader extends AsyncTask<String,Void,Bitmap> {

    private Context context;
    private ImageData data;
    private ImagesViewAdapter.ViewHolder vh;
    private int position;
    private RecyclerView.Adapter adapter;

    public ImageDownloader(Context context, ImageData data, RecyclerView.Adapter adapter, ImagesViewAdapter.ViewHolder vh, int position){
        this.context = context;
        this.data = data;
        this.adapter = adapter;
        this.vh = vh;
        this.position = position;
    }

    @Override
    protected void onPreExecute() {
        vh.pb.setVisibility(View.VISIBLE);
        vh.tiv.setVisibility(View.GONE);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            String url = (String) data.image;
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setInstanceFollowRedirects(true);
            Bitmap bm = BitmapFactory.decodeStream(conn.getInputStream());
            conn.getInputStream().close();

            if(bm == null){
                bm = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
            }

            return bm;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        ImageCache.save(context, (String) data.image,bitmap);
        vh.pb.setVisibility(View.GONE);
        vh.tiv.setVisibility(View.VISIBLE);
        this.adapter.notifyItemChanged(position);
    }
}
