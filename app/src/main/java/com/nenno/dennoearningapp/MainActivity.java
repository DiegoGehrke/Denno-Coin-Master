package com.nenno.dennoearningapp;

import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;

import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;



public class MainActivity extends AppCompatActivity {


    private TextView tocontinue;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        TextView welcome = findViewById(R.id.welcome);

        tocontinue = findViewById(R.id.toContinue);

        TextView txt_continue = findViewById(R.id.txt_continue);

        GradientTxt(welcome);

        GradientTxt(tocontinue);

        GradientTxt(txt_continue);

        LinearLayout letsgo = findViewById(R.id.letsgo);

        letsgo.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Signup.class));
            this.finish();
        });

        text = getString(R.string.to_continue_you_confirm_that_read_our_privacy_policy_and_the_terms_of_use);

        if (text.equals("To continue you confirm that you read our privacy policy and the terms of use.")){

            spannableStrEn();

        } else if (text.equals("Ao continuar, você confirma que leu nossa política de privacidade e os termos de uso.")){

            spannableStrPT();

        } else if (text.equals("Para continuar, confirma que ha leído nuestra política de privacidad y los términos de uso.")){
                spannableStrES();
        }
        if (firebaseAuth.getCurrentUser() != null){

            startActivity(new Intent(MainActivity.this, Home.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION));
            this.finish();
        }
        else if (text.equals("Pour continuer, vous confirmez avoir lu notre politique de confidentialité et les conditions d'utilisation.")){
            spannableStrFR();
        }

        }


    private void spannableStrEn(){
        SpannableString policy = new SpannableString(text);
        ClickableSpan click1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = "https://pages.flycricket.io/denno-coin-master-0/terms.html";
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(urlIntent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#F2CE7F"));
                ds.setUnderlineText(true);
            }
        };
        ClickableSpan click2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = "https://pages.flycricket.io/denno-coin-master-0/terms.html";
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(urlIntent);
            }
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#F2CE7F"));
                ds.setUnderlineText(true);
            }
        };
        policy.setSpan(click1, 42, 56, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        policy.setSpan(click2, 65, 77, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tocontinue.setText(policy);
        tocontinue.setMovementMethod(LinkMovementMethod.getInstance());
        tocontinue.setHighlightColor(Color.parseColor("#964B00"));
    }
    private void spannableStrPT(){
        SpannableString policy = new SpannableString(text);
        ClickableSpan click1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = "https://pages.flycricket.io/denno-coin-master-0/terms.html";
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(urlIntent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#F2CE7F"));
                ds.setUnderlineText(true);
            }
        };
        ClickableSpan click2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = "https://pages.flycricket.io/denno-coin-master-0/terms.html";
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(urlIntent);
            }
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#F2CE7F"));
                ds.setUnderlineText(true);
            }
        };
        policy.setSpan(click1, 42, 65, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        policy.setSpan(click2, 71, 84, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tocontinue.setText(policy);
        tocontinue.setMovementMethod(LinkMovementMethod.getInstance());
        tocontinue.setHighlightColor(Color.parseColor("#964B00"));
    }
    private void spannableStrES(){
        SpannableString policy = new SpannableString(text);
        ClickableSpan click1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = "https://pages.flycricket.io/denno-coin-master-0/terms.html";
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(urlIntent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#F2CE7F"));
                ds.setUnderlineText(true);
            }
        };
        ClickableSpan click2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = "https://pages.flycricket.io/denno-coin-master-0/terms.html";
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(urlIntent);
            }
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#F2CE7F"));
                ds.setUnderlineText(true);
            }
        };

        policy.setSpan(click1, 46, 68, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        policy.setSpan(click2, 75, 90, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tocontinue.setText(policy);
        tocontinue.setMovementMethod(LinkMovementMethod.getInstance());
        tocontinue.setHighlightColor(Color.parseColor("#964B00"));
    }
    private void spannableStrFR(){
        SpannableString policy = new SpannableString(text);
        ClickableSpan click1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = "https://pages.flycricket.io/denno-coin-master-0/privacy.html";
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(urlIntent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#F2CE7F"));
                ds.setUnderlineText(true);
            }
        };
        ClickableSpan click2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = "https://pages.flycricket.io/denno-coin-master-0/terms.html";
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(urlIntent);
            }
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#F2CE7F"));
                ds.setUnderlineText(true);
            }
        };

        policy.setSpan(click1, 46, 74, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        policy.setSpan(click2, 82, 106, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tocontinue.setText(policy);
        tocontinue.setMovementMethod(LinkMovementMethod.getInstance());
        tocontinue.setHighlightColor(Color.parseColor("#964B00"));
    }
    private void GradientTxt(final TextView _view){
        Shader myShader = new LinearGradient(
                0, 0, 0, 100,
                Color.rgb(254, 228, 188), Color.rgb(242, 206, 127),
                Shader.TileMode.CLAMP );
        _view.getPaint().setShader( myShader );
    }
}
