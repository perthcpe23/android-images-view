package com.longdo.android.api;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class ImagesViewAdapter extends RecyclerView.Adapter<ImagesViewAdapter.ViewHolder> {

    private List<ImageData> list;
    private Context context;

    ImagesViewAdapter(Context context, List<ImageData> list){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelativeLayout rl = new RelativeLayout(parent.getContext());
        TouchImageView tiv = new TouchImageView(parent.getContext());
        ProgressBar pb = new ProgressBar(parent.getContext());
        TextView tv = new TextView(parent.getContext());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rl.setLayoutParams(params);
        tiv.setLayoutParams(params);

        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        pb.setLayoutParams(params);

        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, Resources.getSystem().getDisplayMetrics());
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.setMargins(px,px,px,px);
        tv.setLayoutParams(params);
        tv.setTextColor(Color.WHITE);

        rl.addView(pb);
        rl.addView(tiv);
        rl.addView(tv);

        return new ViewHolder(rl,tiv,tv,pb);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ImageData data = list.get(position);

        holder.tiv.resetZoom();

        if(data.image instanceof Bitmap){
            holder.tiv.setImageBitmap((Bitmap) data.image);
            holder.pb.setVisibility(View.GONE);
        }
        else if(data.image instanceof Integer){
            holder.tiv.setImageResource((Integer) data.image);
            holder.pb.setVisibility(View.GONE);
        }
        else if(data.image instanceof Drawable){
            holder.tiv.setImageDrawable((Drawable) data.image);
            holder.pb.setVisibility(View.GONE);
        }
        else if(data.image instanceof String){
            Bitmap bm = ImageCache.load(context,(String) data.image);

            if(bm != null && !bm.isRecycled()) {
                holder.tiv.setImageBitmap(bm);
                holder.tiv.resetZoom();
            }
            else{
                ImageDownloader imd = new ImageDownloader(context, data, this, holder, position);
                imd.execute();
            }
        }

        if(data.title != null) {
            holder.tv.setText(data.title);
            holder.tv.setVisibility(View.VISIBLE);
        }
        else{
            holder.tv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ProgressBar pb;
        TouchImageView tiv;
        TextView tv;

        ViewHolder(View itemView, TouchImageView tiv,TextView tv,ProgressBar pb) {
            super(itemView);

            this.tiv = tiv;
            this.pb = pb;
            this.tv = tv;
        }
    }
}
