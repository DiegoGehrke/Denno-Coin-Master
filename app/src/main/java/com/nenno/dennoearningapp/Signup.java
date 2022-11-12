package com.nenno.dennoearningapp;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class Signup extends AppCompatActivity implements View.OnClickListener {

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final FirebaseDatabase FbD = FirebaseDatabase.getInstance();

    private LinearLayout btn_signup, btn_login;
    private EditText e_mail, password, confirm_pass;
    private FirebaseAuth firebaseAuth;
    private HashMap<String, Object> map = new HashMap<>();
    private final DatabaseReference userInfo = firebaseDatabase.getReference("userInfo");
    private HashMap<String, Object> mapa = new HashMap<>();
    private final DatabaseReference publico = FbD.getReference("publico");
    private TextView textView8;
    private Double coinValue;
    LoadingDialog customloading;
    private SharedPreferences showRefSP;
    private SharedPreferences showCoinSP;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);




        customloading = new LoadingDialog(this);
        TextView title = findViewById(R.id.textView3);
        e_mail = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        confirm_pass = findViewById(R.id.editTextTextPassword2);
        btn_signup = findViewById(R.id.btn_signup);
        btn_login = findViewById(R.id.btn_login);
        TextView txt_Signup = findViewById(R.id.txt_signup1);
        TextView txt_Login = findViewById(R.id.txtlogin1);
        textView8 = findViewById(R.id.textView8);
        GradientTxt(title);
        GradientTxt(txt_Signup);
                GradientTxt(txt_Login);
        btn_signup.setOnClickListener(this);
        btn_login.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Signup.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        Signup.this.finish();
    }


    private void registerUser() {

        coinValue = 0.0;

        String email = e_mail.getText().toString().trim();

        String pass = password.getText().toString().trim();

        if (TextUtils.isEmpty(e_mail.getText())) {

            e_mail.setError((getString(R.string.field_empty)));

        } else{

            if (TextUtils.isEmpty(password.getText())) {

                password.setError((getString(R.string.field_empty)));
            } else{

                if (!confirm_pass.getText().toString().equals(password.getText().toString())){

                    confirm_pass.setError((getString(R.string.different_pass)));

                } else {

                   customloading.Show();

                    firebaseAuth.createUserWithEmailAndPassword(email, pass)

                            .addOnCompleteListener(this, task -> {

                                if (task.isSuccessful()) {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    String code = "abcdef";
                                    assert user != null;
                                    String userKey = user.getUid();

                                    customloading.dialog.dismiss();

                                    createRandomCode(8);

                                    Calendar calendar = Calendar.getInstance();

                                    map = new HashMap<>();
                                    mapa = new HashMap<>();

                                    map.put("E-mail", email);

                                    map.put("UID", userKey);

                                    map.put("Referral Code", textView8.getText().toString());

                                    map.put("UserID", new SimpleDateFormat("mmssMs").format(calendar.getTime()));

                                    map.put("UserCoins", coinValue);
                                    mapa.put("Code", textView8.getText().toString());
                                    mapa.put("Uid", userKey);
                                    mapa.put("realUid", userKey);
                                    publico.child(mapa.get("Code").toString()).updateChildren(mapa);


                                    userInfo.child(userKey).updateChildren(map);
                                    showRefSP = PreferenceManager
                                            .getDefaultSharedPreferences(Signup.this);
                                    SharedPreferences.Editor editor = showRefSP.edit();
                                    editor.putString("show", "true");
                                    editor.apply();


                                    startActivity(new Intent(Signup.this, Home.class));
                                    Signup.this.finish();
                                    mapa.clear();
                                    map.clear();

                                } else {

                                    customloading.dialog.dismiss();
                                    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
                                    String errorCode = ((FirebaseAuthException) Objects.requireNonNull(task.getException())).getErrorCode();

                                    switch (errorCode) {

                                        case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                            myBigToast(getString(R.string.ERROR_CREDENTIAL_ALREADY_IN_USE));
                                            break;

                                        case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                            myBigToast(getString(R.string.ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL));
                                            break;

                                        case "ERROR_WEAK_PASSWORD":
                                            password.setError(getString(R.string.ERROR_WEAK_PASSWORD));
                                            password.requestFocus();
                                            password.setText("");
                                            confirm_pass.setText("");
                                            break;

                                        case "ERROR_INVALID_EMAIL":
                                            e_mail.setError(getString(R.string.ERROR_INVALID_EMAIL));
                                            e_mail.requestFocus();
                                            e_mail.setText("");
                                            break;

                                        case "ERROR_MISSING_EMAIL":
                                            e_mail.setError(getString(R.string.ERROR_MISSING_EMAIL));
                                            e_mail.requestFocus();
                                            break;
                                    }
                                }
                            });
                }
            }
        }


    }

    public void myBigToast(final String gconteudo){
        Context context = Signup.this;
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

    public void createRandomCode(int codeLength) {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new SecureRandom();
        for (int i = 0; i < codeLength; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        textView8.setText(sb);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_signup){
            registerUser();
        }
        if (v == btn_login){
                startActivity(new Intent(Signup.this, login.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                this.finish();
                overridePendingTransition(0, 0);

        }
    }}