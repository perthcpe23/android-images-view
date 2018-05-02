package com.longdo.android.api;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
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
        TextView tvTile = new TextView(parent.getContext());
        TextView tvAuthor = new TextView(parent.getContext());
        TextView tvDate = new TextView(parent.getContext());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rl.setLayoutParams(params);
        tiv.setLayoutParams(params);

        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        pb.setLayoutParams(params);

        int viewId = 99;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            viewId = View.generateViewId();
        }

        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, Resources.getSystem().getDisplayMetrics());
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.setMargins(px,0,px,px);
        tvAuthor.setLayoutParams(params);
        tvAuthor.setTextColor(Color.LTGRAY);
        tvAuthor.setId(viewId);

        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.setMargins(px,0,px,px);
        tvDate.setGravity(Gravity.END);
        tvDate.setLayoutParams(params);
        tvDate.setTextColor(Color.LTGRAY);
        tvDate.setId(viewId);
        tvDate.setLayoutParams(params);

        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ABOVE,viewId);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.setMargins(px,px,px,px/2);
        tvTile.setLayoutParams(params);
        tvTile.setTextColor(Color.WHITE);
        tvTile.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        tvTile.setTypeface(null, Typeface.BOLD);

        rl.addView(pb);
        rl.addView(tiv);
        rl.addView(tvTile);
        rl.addView(tvAuthor);
        rl.addView(tvDate);

        return new ViewHolder(rl,tiv,tvTile,tvAuthor,tvDate,pb);
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
            holder.tvTitle.setText(data.title);
            holder.tvTitle.setVisibility(View.VISIBLE);
        }
        else{
            holder.tvTitle.setVisibility(View.GONE);
        }

        if(data.owner != null) {
            holder.tvAuthor.setText(data.owner);
            holder.tvAuthor.setVisibility(View.VISIBLE);
        }
        else{
            holder.tvAuthor.setVisibility(View.GONE);
        }

        if(data.date != null) {
            holder.tvDate.setText(data.date);
            holder.tvDate.setVisibility(View.VISIBLE);
        }
        else{
            holder.tvDate.setVisibility(View.GONE);
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
        TextView tvTitle;
        TextView tvAuthor;
        TextView tvDate;

        ViewHolder(View itemView, TouchImageView tiv,TextView tvTitle,TextView tvAuthor,TextView tvDate,ProgressBar pb) {
            super(itemView);

            this.v = itemView;
            this.tiv = tiv;
            this.pb = pb;
            this.tvTitle = tvTitle;
            this.tvAuthor = tvAuthor;
            this.tvDate = tvDate;

        }
    }
}
