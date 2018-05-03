package com.longdo.android.imagesview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.longdo.android.api.ImagesView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImagesView iv = findViewById(R.id.images_view);

        Bitmap bm = BitmapFactory.decodeResource(getResources(),android.R.drawable.ic_lock_silent_mode);
        iv.addImage("https://map.longdo.com/mmmap/images/pin_mark.png","Added by URL","perth.s28@gmail.com","24 Jun 2018");
        iv.addImage(android.R.drawable.ic_delete,"Added by Resource ID","John","23 Aug 2017");
        iv.addImage(getResources().getDrawable(android.R.drawable.ic_dialog_alert),"Added by Drawable","Duke","1 Mar 2001");
        iv.addImage(bm,"added by Bitmap","Jimmy","1 Mar 2001");
    }
}
