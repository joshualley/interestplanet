package com.example.interestplanet.service;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.example.interestplanet.R;
import com.google.firebase.storage.FirebaseStorage;

import java.io.ByteArrayOutputStream;
import java.util.function.Consumer;

/**
 * handle the image
 */
public class ImageService {

    public interface Callback {
        public void onResult(boolean isSuccess, String path);
    }
    public static FirebaseStorage storage = FirebaseStorage.getInstance();


    public static Bitmap zoom(Bitmap bitmap, double newWidth, double newHeight) {
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        // calculate the rate of scale
        float scale = ((float) newHeight) / height;
        if (width < height) {
            scale = ((float) newWidth) / width;
        }
        // scale the image
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);
    }

    public static void upload(String userId, String fn, Bitmap bitmap, Callback callback) {
        String path = String.format("images/%s/%s", userId, fn);
        // convert the bitmap to bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        // upload
        storage.getReference().child(path).putBytes(data)
                .addOnFailureListener(exception -> {
                    Log.e("upload", "failure: " + path, exception);
                    callback.onResult(false, path);
                })
                .addOnSuccessListener(snapshot -> {
                    Log.i("upload", "success: " + path);
                    callback.onResult(true, path);
                });
    }

    public static void download(String path, Consumer<byte[]> action) {
        final long ONE_MEGABYTE = 1024 * 1024;
        storage.getReference().child(path).getBytes(ONE_MEGABYTE)
                .addOnFailureListener(exception -> {
                    Log.e("download", "failure: " + path, exception);
                    action.accept(null);
                })
                .addOnSuccessListener(action::accept);
    }

    public static void showBottomDialog(Context context, Resources resources, String buttonText, View.OnClickListener l) {
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_bottom_dialog, null);
        // initialize the dialog
        bottomDialog.setContentView(root);
        Window dialogWindow = bottomDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width = resources.getDisplayMetrics().widthPixels;
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f;
        dialogWindow.setAttributes(lp);

        Button btn = root.findViewById(R.id.modify_pic_btn);
        btn.setText(buttonText);
        btn.setOnClickListener(view -> {
            if (l != null) {
                l.onClick(view);
            }
            bottomDialog.dismiss();
        });
        bottomDialog.show();
    }

    public static void loadStorageImageTo(Context context, String path, ImageView iv) {
        download(path, bytes -> {
            if (bytes == null || context == null) return;
            Glide.with(context).load(bytes).into(iv);
        });
    }

    public static void loadStorageImageTo(Activity activity, String path, ImageView iv) {
        download(path, bytes -> {
            if (bytes == null || activity.isDestroyed()) return;
            Glide.with(activity).load(bytes).into(iv);
        });
    }

}
