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
        iv.addImage("https://map.longdo.com/mmmap/images/pin_mark.png");
        iv.addImage(android.R.drawable.ic_delete);
        iv.addImage(getResources().getDrawable(android.R.drawable.ic_dialog_alert));
        iv.addImage(bm);
    }
}
