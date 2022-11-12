package com.nenno.dennoearningapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;

import android.os.Bundle;

import android.view.Window;
import android.view.WindowManager;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class referral extends AppCompatActivity {

    TextView title, contagemReferidos, nada, code, urRefs;
    ImageView voltar;
    FirebaseAuth firebaseAuth;
    String userKey;
    ListView listView;
    private HashMap<String, Object> map = new HashMap<>();
    private final ArrayList<HashMap<String, Object>> listMap = new ArrayList<>();
    private final ArrayList<String> myArrayList = new ArrayList<>();
    DatabaseReference DataRef;
    FirebaseDatabase mDatabase;
    DatabaseReference databaseReference;
    String value;
    String uid;
    EditText editTextTextPersonName2;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);
        FirebaseApp.initializeApp(referral.this);
        firebaseAuth = FirebaseAuth.getInstance();
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            code = findViewById(R.id.personal_code);
            title = findViewById(R.id.textView);
            nada = findViewById(R.id.textView6);
            contagemReferidos = findViewById(R.id.txt_my_reffs);
            urRefs = findViewById(R.id.title_txt_your_refs);
            voltar = findViewById(R.id.imageView);
        editTextTextPersonName2 = findViewById(R.id.editTextTextPersonName2);
        button = findViewById(R.id.button);
            GradientTxt(code);
            GradientTxt(title);
            GradientTxt(nada);
            GradientTxt(contagemReferidos);
            GradientTxt(urRefs);
        listView = findViewById(R.id.friend_list);
            ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<>(referral.this, android.R.layout.simple_list_item_1, myArrayList);
            listView.setAdapter(myArrayAdapter);
            mDatabase = FirebaseDatabase.getInstance();
            DataRef = FirebaseDatabase.getInstance().getReference("publico");
        databaseReference = mDatabase.getReference("userInfo");
        button.setOnClickListener(v -> {
            DataRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                    value = dataSnapshot.child("Code").getValue(String.class);
                    uid = dataSnapshot.child("realUid").getValue(String.class);
                    if (editTextTextPersonName2.equals(value)) {
                        DataRef.orderByChild(value).equalTo(value).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (dataSnapshot.getKey().equals(value)) {
                                    if (dataSnapshot.child("realUid").exists()) {
                                        map = new HashMap<>();
                                        map.put("UserCoins", +10.0);
                                        databaseReference.child(uid).updateChildren(map);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                    myArrayList.add(value);
                    myArrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    myArrayAdapter.notifyDataSetChanged();
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
        });


            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            DatabaseReference mDb = mDatabase.getReference();
            FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        userKey = user.getUid();
            voltar.setOnClickListener(v -> {
                startActivity(new Intent(referral.this, userinfos.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                referral.this.finish();
            });

            mDb.child("userInfo").child(userKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String userCode = dataSnapshot.child("Referral Code").getValue(String.class);
                    code.setText(userCode);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("userInfo");

        Query query = reference.orderByChild("UserID").equalTo("UserID");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                //Do something with the individual node here`enter code here`
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String userName = ds.child("UID").getValue(String.class);
                    urRefs.setText(userName);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(referral.this, userinfos.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        referral.this.finish();
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