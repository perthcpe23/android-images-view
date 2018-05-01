package com.longdo.android.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;

/**
 * Created by perth on 19/4/2560.
 */

public class ImageCache {

    public static void save(Context context, String key, Bitmap bm){
        if(bm == null){
            return;
        }

        try {
            key = md5(key);
            File dir = context.getCacheDir();
            bm.compress(Bitmap.CompressFormat.JPEG, 75, new FileOutputStream(new File(dir,key)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap load(Context context, String key){
        key = md5(key);

        File dir = context.getCacheDir();
        File file = new File(dir,key);

        if(!file.exists()){
            return null;
        }

        try {
            return BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                StringBuilder h = new StringBuilder(Integer.toHexString(0xFF & aMessageDigest));
                while (h.length() < 2)
                    h.insert(0, "0");
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }
}
