package com.nenno.dennoearningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;



public class launch_activity extends AppCompatActivity {

    successDialog successDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        FirebaseApp.initializeApp(this);
        successDialog = new successDialog(this);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        TextView textTask = findViewById(R.id.textView2);

        ConnectivityManager cm = (ConnectivityManager) launch_activity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(launch_activity.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    launch_activity.this.finish();
                } else {
                    startActivity(new Intent(launch_activity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    launch_activity.this.finish();
                }
            } else {
                startActivity(new Intent(launch_activity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                launch_activity.this.finish();
            }
        } else {
            mySuperToast(getString(R.string.no_net));
            finishAndRemoveTask();
        }

        GradientTxt(textTask);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

    }

    public void GradientTxt(final TextView _view){
        Shader myShader = new LinearGradient(
                0, 0, 0, 100,
                Color.rgb(254, 228, 188), Color.rgb(242, 206, 127),
                Shader.TileMode.CLAMP );
        _view.getPaint().setShader( myShader );
    }

    private void mySuperToast(final String bconteudo) {
        Context context = launch_activity.this;
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View toastL = inflater.inflate(R.layout.custom_toast, null);
        LinearLayout lin1 = toastL.findViewById(R.id.toast_bg);
        lin1.setBackgroundResource(R.drawable.toast_error);
        TextView content = toastL.findViewById(R.id.conteudo);
        content.setText(bconteudo);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastL);
        toast.show();
    }

}