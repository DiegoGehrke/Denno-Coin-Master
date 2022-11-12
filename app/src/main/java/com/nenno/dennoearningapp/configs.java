package com.nenno.dennoearningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class configs extends AppCompatActivity {

    private ImageView switchRef;
    private ImageView switchCoin;
    private SharedPreferences showRefSP;
    private SharedPreferences showCoinSP;
    Boolean hideCoin;
    successDialog successDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configs);
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            ImageView back = findViewById(R.id.backicone);
            ImageView goSetup = findViewById(R.id.go_setup);
            switchRef = findViewById(R.id.switch_onoff);
        successDialog = new successDialog(this);
            switchCoin = findViewById(R.id.switch_coin_onoff);
            TextView title = findViewById(R.id.activity_title);
            TextView setupMethod = findViewById(R.id.txt_set_up_payment_method);
            TextView showRef = findViewById(R.id.txt_show_referral);
            TextView showCoin = findViewById(R.id.txt_show_coins);
            ImageView goWithdraw = findViewById(R.id.go_withdraw);
            TextView requestWithdraw = findViewById(R.id.txt_request_withdraw);
            showRefSP = getSharedPreferences("showRefSP", Activity.MODE_PRIVATE);
            GradientTxt(title);
            GradientTxt(setupMethod);
            GradientTxt(showRef);
            GradientTxt(showCoin);
            GradientTxt(requestWithdraw);
        showRefSP = PreferenceManager
                .getDefaultSharedPreferences(this);
        String name = showRefSP.getString("show", "");
        if (name.equals("true")){
            switchCoin.setBackgroundResource(R.drawable.ic_switch_on);
            hideCoin = true;
        }
        else {
            switchCoin.setBackgroundResource(R.drawable.ic_switch_off);
            hideCoin = false;
        }

            switchCoin.setOnClickListener(v -> {
                SharedPreferences.Editor editor = showRefSP.edit();
                if (hideCoin){
                    editor.putString("show", "false");
                    hideCoin = false;
                    editor.apply();
                    switchCoin.setBackgroundResource(R.drawable.ic_switch_off);
                }
                else {
                    editor.putString("show", "true");
                    hideCoin = true;
                    editor.apply();
                    switchCoin.setBackgroundResource(R.drawable.ic_switch_on);
                }

            });
            back.setOnClickListener(v -> {
                startActivity(new Intent(configs.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                configs.this.finish();
            });
            goWithdraw.setOnClickListener(v -> {
                startActivity(new Intent(configs.this, Withdraw.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                configs.this.finish();
            });
            goSetup.setOnClickListener(v ->{
                startActivity(new Intent(configs.this, ConfigPayment.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                configs.this.finish();
            });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(configs.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        configs.this.finish();
        super.onBackPressed();
    }

    public void GradientTxt(final TextView _view){
        Shader myShader = new LinearGradient(
                0, 0, 0, 100,
                Color.rgb(254, 228, 188), Color.rgb(242, 206, 127),
                Shader.TileMode.CLAMP );
        _view.getPaint().setShader( myShader );
    }
}