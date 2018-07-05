package com.longdo.android.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

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
        RelativeLayout rl = setupRootView(parent.getContext());
        TouchImageView tiv = setupImageView(parent.getContext());
        ProgressBar pb = setupProgressBar(parent.getContext());

        rl.addView(pb);
        rl.addView(tiv);

        return new ViewHolder(rl,tiv, pb);
    }

    private RelativeLayout setupRootView(Context context) {
        RelativeLayout rl = new RelativeLayout(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rl.setLayoutParams(params);

        return rl;
    }

    private TouchImageView setupImageView(Context context) {
        TouchImageView tiv = new TouchImageView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        tiv.setLayoutParams(params);

        return tiv;
    }

    private ProgressBar setupProgressBar(Context context) {
        ProgressBar pb = new ProgressBar(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        pb.setLayoutParams(params);

        return pb;
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

        View v;
        ProgressBar pb;
        TouchImageView tiv;

        ViewHolder(View itemView, TouchImageView tiv, ProgressBar pb) {
            super(itemView);

            this.v = itemView;
            this.tiv = tiv;
            this.pb = pb;

        }
    }
}
