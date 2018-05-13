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

In app module, edit build.gradle using the following code to add .aar to dependencies
````yaml
repositories {
    flatDir {
        dirs 'libs'
    }
}

dependencies {
    ...
    implementation(name: 'android-images-view_v1.0.5.aar', ext: 'aar')
    ...
}
````

Java source code:
````java
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
````

Contributed by Longdo Developer Team (http://www.longdo.com)
