package com.longdo.android.api;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
        RelativeLayout rl = setupRootView(parent.getContext());
        TouchImageView tiv = setupImageView(parent.getContext());
        ProgressBar pb = setupProgressBar(parent.getContext());
        TextView tvTile = new TextView(parent.getContext());
        TextView tvAuthor = new TextView(parent.getContext());
        TextView tvDate = new TextView(parent.getContext());
        LinearLayout ll = setupInfoBox(parent.getContext(),tvTile,tvAuthor,tvDate);

        rl.addView(pb);
        rl.addView(tiv);
        rl.addView(ll);

        return new ViewHolder(rl,tiv,tvTile,tvAuthor,tvDate,pb);
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

    private LinearLayout setupInfoBox(Context context, TextView tvTitle, TextView tvAuthor, TextView tvDate) {
        LinearLayout ll = new LinearLayout(context);
        LinearLayout llAuthorDate = new LinearLayout(context);

        // container
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ll.setLayoutParams(rlParams);
        ll.setOrientation(LinearLayout.VERTICAL);

        // sub-container
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        llAuthorDate.setOrientation(LinearLayout.HORIZONTAL);
        llAuthorDate.setLayoutParams(llParams);

        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, Resources.getSystem().getDisplayMetrics());

        // title
        llParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        llParams.setMargins(px,px,px,px/2);
        tvTitle.setLayoutParams(llParams);
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        tvTitle.setTypeface(null, Typeface.BOLD);

        // date
        llParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        llParams.setMargins(px,0,px,px);
        tvDate.setTextColor(Color.LTGRAY);
        tvDate.setLayoutParams(llParams);

        // author
        llParams = new LinearLayout.LayoutParams(0, RelativeLayout.LayoutParams.WRAP_CONTENT);
        llParams.setMargins(px,0,px,px);
        llParams.weight = 1;
        tvAuthor.setGravity(Gravity.END);
        tvAuthor.setTextColor(Color.LTGRAY);
        tvAuthor.setLayoutParams(llParams);
        tvAuthor.setMaxLines(1);
        tvAuthor.setEllipsize(TextUtils.TruncateAt.END);

        // setup
        llAuthorDate.addView(tvDate);
        llAuthorDate.addView(tvAuthor);
        ll.addView(tvTitle);
        ll.addView(llAuthorDate);

        return ll;
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
