# android-images-view
Multiple images view on Android

Build on top of https://github.com/MikeOrtiz/TouchImageView.

You can download .arr at https://github.com/perthcpe23/android-images-view/tree/master/aar

Basic usage<br/>
XML layout source code:
````xml
<com.longdo.android.api.ImagesView
    android:id="@+id/images_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
````

Java source code:
````java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ImagesView iv = findViewById(R.id.images_view);

    Bitmap bm = BitmapFactory.decodeResource(getResources(),android.R.drawable.ic_lock_silent_mode);
    iv.addImage("https://map.longdo.com/mmmap/images/pin_mark.png","added by URL");
    iv.addImage(android.R.drawable.ic_delete,"added by Resource ID");
    iv.addImage(getResources().getDrawable(android.R.drawable.ic_dialog_alert),"added by Drawable");
    iv.addImage(bm,"added by Bitmap");
}
````

Contributed by Longdo Developer Team (http://www.longdo.com)
