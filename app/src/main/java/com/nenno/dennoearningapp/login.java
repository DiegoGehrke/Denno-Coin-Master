package com.nenno.dennoearningapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;


public class login extends AppCompatActivity implements View.OnClickListener {

    private EditText emailfield, passfield;
    private LinearLayout btn_login, btn_signup;
    private FirebaseAuth firebaseAuth;

    private TextView txtforgotpass;
    resetPass resetPass;
    failureDialog failureDialog;
    successDialog successDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        resetPass = new resetPass(this);
        successDialog = new successDialog(this);
        failureDialog = new failureDialog(this);
        TextView txtlogin = findViewById(R.id.textView4);
        txtforgotpass = findViewById(R.id.textView5);
        TextView txt_login = findViewById(R.id.txt_login);
        TextView txt_signup = findViewById(R.id.txt_signup);
        GradientTxt(txt_login);
        GradientTxt(txt_signup);
        GradientTxt(txtlogin);
        GradientTxt(txtforgotpass);
        btn_signup = findViewById(R.id.signup_btn);
        btn_login = findViewById(R.id.login_btn);
        emailfield = findViewById(R.id.editTextTextEmailAddress2);
        passfield = findViewById(R.id.editTextTextPassword3);

        btn_login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        txtforgotpass.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(login.this, Signup.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        login.this.finish();
    }

    private void usuarioLogar() {
        String email = emailfield.getText().toString().trim();
        String pass = passfield.getText().toString().trim();
        if (TextUtils.isEmpty(emailfield.getText())) {
            emailfield.setError((getString(R.string.field_empty)));
        } else{
            if (TextUtils.isEmpty(passfield.getText())) {
                passfield.setError((getString(R.string.field_empty)));
            } else{
                    firebaseAuth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    SharedPreferences showRefSP;
                                    showRefSP = PreferenceManager
                                            .getDefaultSharedPreferences(this);
                                    SharedPreferences.Editor editor = showRefSP.edit();
                                    editor.putString("show", "true");
                                    editor.apply();
                                    startActivity(new Intent(login.this, Home.class));
                                    this.finish();

                                } else {
                                    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
                                    String errorCode = ((FirebaseAuthException) Objects.requireNonNull(task.getException())).getErrorCode();

                                    switch (errorCode) {

                                        case "ERROR_INVALID_CREDENTIAL":
                                            myBigToast(getString(R.string.ERROR_INVALID_CREDENTIAL));
                                            break;


                                        case "ERROR_WRONG_PASSWORD":
                                            passfield.setError(getString(R.string.ERROR_WRONG_PASSWORD));
                                            passfield.requestFocus();
                                            passfield.setText("");
                                            break;

                                        case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                            emailfield.requestFocus();
                                            emailfield.setError(getString(R.string.ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL));
                                            emailfield.setText("");
                                            break;

                                        case "ERROR_USER_DISABLED":
                                            myBigToast(getString(R.string.ERROR_USER_DISABLED));
                                            break;


                                        case "ERROR_USER_NOT_FOUND":
                                            myBigToast(getString(R.string.ERROR_USER_NOT_FOUND));
                                            break;
                                    }
                                }
                            });
            }
        }}
    public void myBigToast(final String gconteudo){
        Context context = login.this;
        LayoutInflater inflater = getLayoutInflater();
        View toastL = inflater.inflate(R.layout.custom_toast, null);
        LinearLayout lin1 = toastL.findViewById(R.id.toast_bg);
        lin1.setBackgroundResource(R.drawable.bigtoast1);
        TextView content = toastL.findViewById(R.id.conteudo);
        content.setText(gconteudo);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastL);
        toast.show();
    }
    private void GradientTxt(final TextView _view){
        Shader myShader = new LinearGradient(
                0, 0, 0, 100,
                Color.rgb(254, 228, 188), Color.rgb(242, 206, 127),
                Shader.TileMode.CLAMP );
        _view.getPaint().setShader( myShader );
    }
    public void onClick(View v) {
        if (v == btn_signup){
            startActivity(new Intent(login.this, Signup.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            this.finish();
        }
        if (v == btn_login){
               usuarioLogar();

        }
        if (v == txtforgotpass){
            resetPass.build();
        }

    }}



