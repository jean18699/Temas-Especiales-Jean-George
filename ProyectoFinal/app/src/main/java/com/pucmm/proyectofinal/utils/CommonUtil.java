package com.pucmm.proyectofinal.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;


import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.networksync.FirebaseNetwork;
import com.pucmm.proyectofinal.networksync.NetResponse;

import java.io.ByteArrayOutputStream;
import java.util.function.Consumer;
import java.util.function.Function;

public class CommonUtil {

    private static final String TAG = "CommonUtil";

    public static void popupMenu(@NonNull Context context, @NonNull View view, @NonNull VoidListener manager, @NonNull VoidListener delete) {
        PopupMenu popup = new PopupMenu(context, view);
        popup.inflate(R.menu.action_menu);
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_manager:
                    manager.accept();
                    return true;
                case R.id.action_delete:
                    delete.accept();
                    return true;
                default:
                    return false;
            }
        });
        popup.show();

    }

    public static void downloadImage(@NonNull String photo, @NonNull ImageView imageView) {
        if (photo != null && !photo.isEmpty()) {
            FirebaseNetwork.obtain().download(photo, new NetResponse<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    imageView.setImageBitmap(response);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });
        }
    }

    public static Uri getImageUri(@NonNull Context context, @NonNull Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, null, null);
        return Uri.parse(path);
    }

    public static void alertDialog(@NonNull Context context, @NonNull String title, @NonNull String message, @NonNull VoidListener consumer) {
        new AlertDialog.Builder(context).setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", (dialog, which) -> consumer.accept())
                .setNegativeButton("No", (dialog, which) -> dialog.cancel()).show();

    }


}
