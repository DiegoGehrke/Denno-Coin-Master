package com.nenno.dennoearningapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;

import android.os.Bundle;


import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userinfos extends AppCompatActivity {

    private TextView userEmail;
    private TextView userID;
    FirebaseAuth firebaseAuth;
    String userKey;
    resetPass resetPass;
    successDialog successDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfos);
        FirebaseApp.initializeApp(userinfos.this);
        firebaseAuth = FirebaseAuth.getInstance();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        userEmail = findViewById(R.id.txt_user_email);
        resetPass = new resetPass(this);
        successDialog = new successDialog(this);
        TextView screenTitle = findViewById(R.id.txt_title);
        userID = findViewById(R.id.txt_user_id);
        TextView txtViewTransactionHistory = findViewById(R.id.txt_transaction);
        TextView txtChangePass = findViewById(R.id.txt_change_pass);
        ImageView goView = findViewById(R.id.transaction_go);
        ImageView goChange = findViewById(R.id.change_pass_go);
        ImageView goRef = findViewById(R.id.referral_go);
        TextView txtRef = findViewById(R.id.txt_ref);
        ImageView back = findViewById(R.id.ic_back1);
        LinearLayout history = findViewById(R.id.history);
        history.setVisibility(View.GONE);
        GradientTxt(screenTitle);
        GradientTxt(userID);
        GradientTxt(userEmail);
        GradientTxt(txtViewTransactionHistory);
        GradientTxt(txtRef);
        GradientTxt(txtChangePass);
        goView.setOnClickListener(v -> {
            //VIEW TRANSACTION HISTORY
        });
        goChange.setOnClickListener(v -> {
            //SHOW DIALOG
            resetPass.build();
        });
        goRef.setOnClickListener(v -> {
           successDialog.showSuccessMsg(getString(R.string.coming_soon) ,getString(R.string.coming_soon), false);
        });
        back.setOnClickListener(v -> {
            startActivity(new Intent(userinfos.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            userinfos.this.finish();
        });
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDb = mDatabase.getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        userKey = user.getUid();

        mDb.child("userInfo").child(userKey).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userIDstr = dataSnapshot.child("UserID").getValue(String.class);
                String userEmailstr = dataSnapshot.child("E-mail").getValue(String.class);
                userID.setText("ID: " + userIDstr);
                userEmail.setText("E-mail: " + userEmailstr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(userinfos.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        userinfos.this.finish();
        super.onBackPressed();
    }
    private void GradientTxt(final TextView _view){
        Shader myShader = new LinearGradient(
                0, 0, 0, 100,
                Color.rgb(254, 228, 188), Color.rgb(242, 206, 127),
                Shader.TileMode.CLAMP );
        _view.getPaint().setShader( myShader );
    }
}