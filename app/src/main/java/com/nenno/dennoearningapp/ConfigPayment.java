package com.nenno.dennoearningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class ConfigPayment extends AppCompatActivity {

    dialog_setup_payment dialog_setup_payment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_payment);
        dialog_setup_payment = new dialog_setup_payment(ConfigPayment.this);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        TextView pageTitle = findViewById(R.id.pg_titulo);
        TextView txt_setup_pix = findViewById(R.id.txt_setup_pix);
        TextView txt_setup_paypal = findViewById(R.id.txt_setup_paypal);
        ImageView voltar = findViewById(R.id.ic_voltar);
        ImageView goSetupPix = findViewById(R.id.go_setup_pix);
        ImageView goSetupPaypal = findViewById(R.id.go_setup_paypal);
        GradientTxt(pageTitle, txt_setup_paypal, txt_setup_pix);

        voltar.setOnClickListener(v -> {
            startActivity(new Intent(ConfigPayment.this, configs.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            ConfigPayment.this.finish();
        });
        goSetupPaypal.setOnClickListener(v -> {
            dialog_setup_payment.builddg(false, true);
        });
        goSetupPix.setOnClickListener(v -> {
            dialog_setup_payment.builddg(true, false);
        });
    }

    @Override
    protected void onPause() {
        if (dialog_setup_payment.dialogo_Setup != null){
            dialog_setup_payment.dialogo_Setup.dismiss();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (dialog_setup_payment.dialogo_Setup != null){
            dialog_setup_payment.dialogo_Setup.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ConfigPayment.this, configs.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        ConfigPayment.this.finish();
        super.onBackPressed();
    }

    public void GradientTxt(final TextView _view, final TextView _view1, final TextView _view2){
        Shader myShader = new LinearGradient(
                0, 0, 0, 100,
                Color.rgb(254, 228, 188), Color.rgb(242, 206, 127),
                Shader.TileMode.CLAMP );
        _view.getPaint().setShader( myShader );
        _view1.getPaint().setShader( myShader ) ;
        _view2.getPaint().setShader( myShader );
    }
    private void myLittleToast(final String pconteudo){
        Context context = ConfigPayment.this;
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View toastL = inflater.inflate(R.layout.custom_toast, null);
        LinearLayout lin1 = toastL.findViewById(R.id.toast_bg);
        lin1.setBackgroundResource(R.drawable.mytoast);
        TextView content = toastL.findViewById(R.id.conteudo);
        content.setText(pconteudo);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastL);
        toast.show();
    }
}