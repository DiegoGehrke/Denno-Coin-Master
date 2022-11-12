package com.nenno.dennoearningapp;

import static android.graphics.Color.parseColor;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

public class LoadingDialog {
    Context context;
    Dialog dialog;
    ImageView pgb;


    public LoadingDialog(Context context) {
        this.context = context;
    }
    public void Show() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_loading);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pgb = dialog.findViewById(R.id.progressBar);
        Glide.with(context).load(R.drawable.loadingcustom).into(pgb);
        dialog.show();
    }
    public void Hide() {
        dialog.dismiss();
    }
}
