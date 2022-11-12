package com.nenno.dennoearningapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class Withdraw extends AppCompatActivity {

    private boolean pagapal = false;
    private boolean pix_brasil = false;
    private boolean cinco = false;
    private boolean dez = false;
    private boolean vinte = false;
    private int progresso = 5;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase withdrawls = FirebaseDatabase.getInstance();
    DatabaseReference requests = withdrawls.getReference("Withdraw Requests");
    String userID;
    String userKey;
    Double menos;
    String pixKey;
    String pixTypeKey;
    String paypalEmail;
    String fullNamePIX;
    String FullNamePayPal;
    dialog_setup_payment dialog_setup_payment;
    Boolean error = false;
    successDialog successDialog;

    private final HashMap<String, Object> mapa = new HashMap<>();
    private final HashMap<String, Object> map = new HashMap<>();
    private double userCoins = 0;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference userInfo = firebaseDatabase.getReference();
    DatabaseReference abcd = requests.child("Uid");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        successDialog = new successDialog(this);

        dialog_setup_payment = new dialog_setup_payment(Withdraw.this);
        ImageView volte = findViewById(R.id.ic_voltar);
        LinearLayout paypal = findViewById(R.id.paypal);
        LinearLayout pix = findViewById(R.id.pix_bra);
        LinearLayout btn_withdraw = findViewById(R.id.btn_withdraw);
        LinearLayout quantia1 = findViewById(R.id.quantia1);
        LinearLayout quantia2 = findViewById(R.id.quantia2);
        LinearLayout quantia3 = findViewById(R.id.quantia3);
        TextView fiveDol = findViewById(R.id.fiveDol);
        TextView tenDol = findViewById(R.id.tenDol);
        TextView twentyDol = findViewById(R.id.twentyDol);
        TextView title = findViewById(R.id.pg_titulo);
        TextView desc1 = findViewById(R.id.desc_txt1);
        TextView desc2 = findViewById(R.id.desc_txt2);
        TextView withdrawtxt = findViewById(R.id.withdraw_txt);
        TextView txt_paypal = findViewById(R.id.text_paypal);
        TextView txt_pix = findViewById(R.id.textView13);
        btn_withdraw.setAlpha(1.0f);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        abcd.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                    if (dataSnapshot.child("Status01").toString().equals("Analysis")){
                        btn_withdraw.setAlpha(0.5f);
                    }
                }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.child("Status01").toString().equals("Analysis")){
                    btn_withdraw.setAlpha(0.5f);
                }
            }


            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        GradientTxt(fiveDol, tenDol, twentyDol, title, desc1, desc2, withdrawtxt, txt_paypal, txt_pix);
        volte.setOnClickListener(v -> {
                 startActivity(new Intent(Withdraw.this, configs.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    Withdraw.this.finish();
        });
        paypal.setOnClickListener(v -> {
            pagapal = true;
            if (pix_brasil){
                pix.setBackgroundResource(R.drawable.background_option);
                paypal.setBackgroundResource(R.drawable.background_pick);
                pix_brasil = false;
            }
            else {
                paypal.setBackgroundResource(R.drawable.background_pick);

            }
        });
        pix.setOnClickListener(v -> {
            pix_brasil = true;
            if (pagapal){
                paypal.setBackgroundResource(R.drawable.background_option);
                pix.setBackgroundResource(R.drawable.background_pick);
                pagapal = false;
            }
            else {
                pix.setBackgroundResource(R.drawable.background_pick);
            }

        });
        btn_withdraw.setOnClickListener(v -> {
            if (pix_brasil){
                if (cinco){
                    addRequest(5000.00);
                }else if (dez){
                    addRequest(10000.00);
                }else if (vinte) {
                    addRequest(20000.00);
                }
            }
            else {
                if (pagapal){
                    if (cinco){
                        addRequest(5000.00);
                    }else if (dez){
                        addRequest(10000.00);
                    }else if (vinte) {
                        addRequest(20000.00);
                    }
                }
                else {
                mySuperToast("Selecione um metodo");
                }
            }

        });
        quantia1.setOnClickListener(v -> {
            cinco = true;
            if (dez || vinte){
                quantia1.setBackgroundResource(R.drawable.big_amount_option_selected);
                quantia2.setBackgroundResource(R.drawable.big_amount_option);
                quantia3.setBackgroundResource(R.drawable.big_amount_option);
                dez = false;
                vinte = false;
            }
            else {
                quantia1.setBackgroundResource(R.drawable.big_amount_option_selected);
                quantia2.setBackgroundResource(R.drawable.big_amount_option);
                quantia3.setBackgroundResource(R.drawable.big_amount_option);
            }
        });
        quantia2.setOnClickListener(v -> {
            dez = true;
            if (cinco || vinte){
                quantia1.setBackgroundResource(R.drawable.big_amount_option);
                quantia2.setBackgroundResource(R.drawable.big_amount_option_selected);
                quantia3.setBackgroundResource(R.drawable.big_amount_option);
                cinco = false;
                vinte = false;
            }
            else {
                quantia1.setBackgroundResource(R.drawable.big_amount_option);
                quantia2.setBackgroundResource(R.drawable.big_amount_option_selected);
                quantia3.setBackgroundResource(R.drawable.big_amount_option);
            }
        });
        quantia3.setOnClickListener(v -> {
            vinte = true;
            if (cinco || dez){
                quantia1.setBackgroundResource(R.drawable.big_amount_option);
                quantia2.setBackgroundResource(R.drawable.big_amount_option);
                quantia3.setBackgroundResource(R.drawable.big_amount_option_selected);
                cinco = false;
                dez = false;
            }
            else {
                quantia1.setBackgroundResource(R.drawable.big_amount_option);
                quantia2.setBackgroundResource(R.drawable.big_amount_option);
                quantia3.setBackgroundResource(R.drawable.big_amount_option_selected);
            }
        });
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userInfo = firebaseDatabase.getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        userKey = user.getUid();
        userInfo.child("userInfo").child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long userCins = dataSnapshot.child("UserCoins").getValue(Long.class);
                if (userCins != null) {
                    userCoins = userCins;
                }
                userID = dataSnapshot.child("UserID").getValue(String.class);
                if (dataSnapshot.hasChild("PIX key")){
                    pixKey = dataSnapshot.child("PIX key").getValue(String.class);
                }
                if (dataSnapshot.hasChild("Real name PIX")){
                    fullNamePIX = dataSnapshot.child("Real name PIX").getValue(String.class);
                }
                if (dataSnapshot.hasChild("Pix key type")){
                    pixTypeKey = dataSnapshot.child("Pix key type").getValue(String.class);
                }
                if (dataSnapshot.hasChild("Paypal email")){
                    paypalEmail = dataSnapshot.child("Paypal email").getValue(String.class);
                }
                if (dataSnapshot.hasChild("Real name PayPal")){
                    FullNamePayPal = dataSnapshot.child("Real name PayPal").getValue(String.class);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mySuperToast(error.toString());
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Withdraw.this, configs.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        Withdraw.this.finish();
        super.onBackPressed();
    }
    public void GradientTxt(final TextView _view, final TextView _view1, final TextView _view2, final TextView _view3,
                            final TextView _view4, final TextView _view5, final TextView _view6, final TextView _view7, final TextView _view8){
        Shader myShader = new LinearGradient(
                0, 0, 0, 100,
                Color.rgb(254, 228, 188), Color.rgb(242, 206, 127),
                Shader.TileMode.CLAMP );
        _view.getPaint().setShader( myShader );
        _view1.getPaint().setShader( myShader ) ;
        _view2.getPaint().setShader( myShader );
        _view3.getPaint().setShader( myShader );
        _view4.getPaint().setShader( myShader );
        _view5.getPaint().setShader( myShader );
        _view6.getPaint().setShader( myShader );
        _view7.getPaint().setShader( myShader );
        _view8.getPaint().setShader( myShader );
    }
    private void mySuperToast(final String bconteudo) {
        Context context = Withdraw.this;
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
    public void conta(double valor){
        double resultado;
        resultado = userCoins - valor;
        mySuperToast(getString(R.string.you_need_more) + " " + (Math.abs(resultado)) + "0 " + getString(R.string.Denno_coin) + "!");
    }


    public void addRequest(HashMap<String, Object> mapinha, double descontar){
        Calendar calendar = Calendar.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        userKey = user.getUid();
        mapa.put("AccEmail", user.getEmail());
        mapa.put("Id", userID);
        mapa.put("Uid", user.getUid());
        mapa.put("Current", String.valueOf(calendar.getTimeInMillis()));
        mapa.put("Date", new SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(calendar.getTime()));
        mapa.put("Status01", "Analysis");
        mapa.put("Status02", "Approved");
        menos = userCoins - descontar;
        map.put("UserCoins", menos);
        userInfo.child("userInfo").child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userInfo.child("userInfo").child(userKey).updateChildren(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        requests.child(String.valueOf(calendar.getTimeInMillis()).concat(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())).updateChildren(mapinha);
        requests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    public void addRequest(Double value) {
        Calendar calendar = Calendar.getInstance();
       if (pix_brasil){
           if (userCoins >= value){
                if (pixKey != null && pixTypeKey != null && fullNamePIX != null){
                    mapa.put("Method", "PIX");
                    mapa.put("PIX Key", pixKey);
                    mapa.put("PIX Key Type", pixTypeKey);
                    mapa.put("Recipient name PIX", fullNamePIX);
                    addRequest(mapa, value);
                    successDialog.showSuccessMsg(getString(R.string.withdraw_request_title) , getString(R.string.withdraw_resquest_sucess), true);
                }else {
                    mySuperToast(getString(R.string.setup_missing));
                }
           }else{
               conta(value);
           }
       }else{
           if (pagapal){
               if (userCoins >= value){
                    if (paypalEmail != null && FullNamePayPal != null){
                        mapa.put("Method", "PayPal");
                        mapa.put("PayPal Email", paypalEmail);
                        mapa.put("Recipient name PayPal", FullNamePayPal);
                        addRequest(mapa, value);
                       successDialog.showSuccessMsg(getString(R.string.withdraw_request_title), getString(R.string.withdraw_resquest_sucess), true);
                    }else {
                        mySuperToast(getString(R.string.setup_missing));
                    }
               }else {
                   conta(value);
               }
           }else {
               mySuperToast(getString(R.string.information_missing));
           }
       }
    }
}




