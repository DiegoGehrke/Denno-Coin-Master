package com.nenno.dennoearningapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class guessanimalgame extends AppCompatActivity implements IUnityAdsInitializationListener {

    private ProgressBar showprogress;
    private TextView sgsrestantes;
    private TextView nome1;
    private TextView nome2;
    private TextView nome3;
    private TextView nome4;
    private LinearLayout btn1, btn2, btn3, btn4, watchunlock;
    private ImageView animal;
    private Double myCoin = 0.0;
    private Double soma = 0.0;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference userInfo = firebaseDatabase.getReference("userInfo");
    private final Timer _timer = new Timer();
    private int progresso = 60;

    private Dialog dialoga;
    private SharedPreferences sharedPreferences;
    private final String unityGameID = "4932075";
    private final Boolean testMode = false;
    private final String adUnitId = "Rewarded_Android";
    LinearLayout primeirosbtn;
    LinearLayout outrosbtn;
    FirebaseAuth firebaseAuth;
    String userKey;
    LinearLayout supimg;

    private final IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
        @Override
        public void onUnityAdsAdLoaded(String placementId) {
            UnityAds.show(guessanimalgame.this, adUnitId, new UnityAdsShowOptions(), showListener);
        }

        @Override
        public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
            watchunlock.setEnabled(true);
            UnityAds.load(adUnitId, (IUnityAdsLoadListener) showListener);
            Log.e("UnityAdsExample", "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
        }
    };

    private final IUnityAdsShowListener showListener = new IUnityAdsShowListener() {
        @Override
        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
            watchunlock.setEnabled(true);

            Log.e("UnityAdsExample", "Unity Ads failed to show ad for " + placementId + " with error: [" + error + "] " + message);
        }

        @Override
        public void onUnityAdsShowStart(String placementId) {
            Log.v("UnityAdsExample", "onUnityAdsShowStart: " + placementId);
        }

        @Override
        public void onUnityAdsShowClick(String placementId) {
            Log.v("UnityAdsExample", "onUnityAdsShowClick: " + placementId);
        }

        @Override
        public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
            Log.v("UnityAdsExample", "onUnityAdsShowComplete: " + placementId);
            if (state.equals(UnityAds.UnityAdsShowCompletionState.COMPLETED)) {
                // Reward the user for watching the ad to completion
                primeiroAnimal();
                startTimer();
                watchunlock.setVisibility(View.GONE);
                showprogress.setVisibility(View.VISIBLE);
                sgsrestantes.setVisibility(View.VISIBLE);
                primeirosbtn.setAlpha(1.0F);
                outrosbtn.setAlpha(1.0F);
                supimg.setAlpha(1.0F);
            } else {
                // Do not reward the user for skipping the ad
                startActivity(new Intent(guessanimalgame.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                guessanimalgame.this.finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guessanimalgame);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        FirebaseApp.initializeApp(this);

        showprogress = findViewById(R.id.progresso123);
        TextView titulo = findViewById(R.id.textView9);
        sgsrestantes = findViewById(R.id.txt_secs_left);
        ImageView voltar = findViewById(R.id.imageView4);
        showprogress.setVisibility(View.INVISIBLE);
        sgsrestantes.setVisibility(View.INVISIBLE);
        primeirosbtn = findViewById(R.id.primeirosbotoes);
        outrosbtn = findViewById(R.id.outrosbotoes);
        primeirosbtn.setAlpha(0.5F);
        outrosbtn.setAlpha(0.5F);
        supimg = findViewById(R.id.suporteimagem);
        supimg.setAlpha(0.5F);
        nome1 = findViewById(R.id.nome1);
        nome2 = findViewById(R.id.nome2);
        nome3 = findViewById(R.id.nome3);
        nome4 = findViewById(R.id.nome4);
        TextView txtwatch = findViewById(R.id.txtwatch);
        TextView nome1 = findViewById(R.id.nome1);
        TextView nome2 = findViewById(R.id.nome2);
        TextView nome3 = findViewById(R.id.nome3);
        TextView nome4 = findViewById(R.id.nome4);
        @SuppressLint("CutPasteId") TextView txt_secs_left = findViewById(R.id.txt_secs_left);
        btn1 = findViewById(R.id.primeiroBotao);
        btn2 = findViewById(R.id.segundobotao);
        btn3 = findViewById(R.id.terceirobotao);
        btn4 = findViewById(R.id.quartobotao);
        watchunlock = findViewById(R.id.unlock123);
        animal = findViewById(R.id.mostrarAnimais);
        GradientTxt(titulo, txtwatch, nome2, nome3, nome4, nome1, txt_secs_left);
        sharedPreferences = getSharedPreferences("name", Activity.MODE_PRIVATE);
        voltar.setOnClickListener(v -> {

            startActivity(new Intent(guessanimalgame.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            guessanimalgame.this.finish();
        });
        watchunlock.setOnClickListener(v -> {
            watchunlock.setEnabled(false);
            UnityAds.initialize(getApplicationContext(), unityGameID, testMode, guessanimalgame.this);
        });
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mDatabaseRefence = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = mDatabaseRefence.getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        String userKey = user.getUid();
        databaseReference.child("userInfo").child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myCoin = dataSnapshot.child("UserCoins").getValue(Double.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(guessanimalgame.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        guessanimalgame.this.finish();
        super.onBackPressed();
    }

    public void GradientTxt(final TextView _view, final TextView _view1, final TextView _view2, final TextView _view3, final TextView _view4, final TextView _view5, final TextView _view6){
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
    }

    private void startTimer(){
        progresso = 30;
        TimerTask ttask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    progresso--;
                    sgsrestantes.setText(String.valueOf(progresso).concat(" ".concat(getString(R.string.seconds_left))));
                    showprogress.setProgress(progresso);
                    if ((progresso == 0) || (progresso < 1)) {
                        _timer.cancel();
                        startActivity(new Intent(guessanimalgame.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        finish();
                    }
                });
            }
        };
        _timer.scheduleAtFixedRate(ttask, 100, 900);
    }

    private void info() {
        Context context = guessanimalgame.this;
        dialoga = new Dialog(context);
        dialoga.setContentView(R.layout.reset_pass_dialog);
        dialoga.setCancelable(false);
        dialoga.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText getEmail = dialoga.findViewById(R.id.getEmail);
        LinearLayout send = dialoga.findViewById(R.id.btn_send);
        TextView msg = dialoga.findViewById(R.id.txt_msg);
        TextView title = dialoga.findViewById(R.id.titulo);
        LinearLayout cancel = dialoga.findViewById(R.id.btn_cancel);
        TextView txt_send = dialoga.findViewById(R.id.txt_send);
        TextView txt_cancel = dialoga.findViewById(R.id.txt_cancel);
        getEmail.setVisibility(View.GONE);
        txt_send.setVisibility(View.GONE);
        title.setText(context.getResources().getString(R.string.titulo_infogame2));
        msg.setText(context.getResources().getString(R.string.msg_infogame2));
        send.setVisibility(View.GONE);
        txt_cancel.setText(getResources().getString(R.string.entendi));
        cancel.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("show", "false").apply();
            primeiroAnimal();
            startTimer();
            dialoga.dismiss();
        });
        dialoga.show();
    }

    private void addCoin(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("UserCoins", myCoin + soma );
        FirebaseUser user = firebaseAuth.getCurrentUser();

        assert user != null;
        userKey = user.getUid();
        userInfo.child(userKey).updateChildren(map);
        map.clear();
        userInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                startActivity(new Intent(guessanimalgame.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                guessanimalgame.this.finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                myLittleToast(error.getMessage());
                startActivity(new Intent(guessanimalgame.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                guessanimalgame.this.finish();
            }
        });
    }

    private void myLittleToast(final String pconteudo){
        Context context = guessanimalgame.this;
        View toastL = View.inflate(this ,R.layout.custom_toast, null);
        LinearLayout lin1 = toastL.findViewById(R.id.toast_bg);
        lin1.setBackgroundResource(R.drawable.mytoast);
        TextView content = toastL.findViewById(R.id.conteudo);
        content.setText(pconteudo);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastL);
        toast.show();
    }

    private void primeiroAnimal(){

        animal.setImageResource(R.drawable.ic_hen);
        nome1.setText(getResources().getString(R.string.galo));
        nome2.setText(getResources().getString(R.string.galinha));
        nome3.setText(getResources().getString(R.string.peru));
        nome4.setText(getResources().getString(R.string.pássaro));
     btn1.setOnClickListener(v -> segundoAnimal());
        btn2.setOnClickListener(v -> {
            myLittleToast("+16.6 " + getString(R.string.Denno_coin));
          soma = soma + 16.6;
            segundoAnimal();

        });
        btn3.setOnClickListener(v -> segundoAnimal());
        btn4.setOnClickListener(v -> segundoAnimal());
    }

    private void segundoAnimal(){

        animal.setImageResource(R.drawable.ic_giraffe);
        nome1.setText(getResources().getString(R.string.zebra));
        nome2.setText(getResources().getString(R.string.Ocapi));
        nome3.setText(getResources().getString(R.string.gazela));
        nome4.setText(getResources().getString(R.string.girafa));
        btn1.setOnClickListener(v -> terceiroAnimal());
        btn2.setOnClickListener(v -> terceiroAnimal());
        btn3.setOnClickListener(v -> terceiroAnimal());
        btn4.setOnClickListener(v -> {
           soma = soma + 16.6;
            myLittleToast("+16.6 " + getString(R.string.Denno_coin));
            terceiroAnimal();

        });
    }

    private void terceiroAnimal(){

        animal.setImageResource(R.drawable.ic_crab);
        nome1.setText(getResources().getString(R.string.caranguejo));
        nome2.setText(getResources().getString(R.string.lagosta));
        nome3.setText(getResources().getString(R.string.camarão));
        nome4.setText(getResources().getString(R.string.krill));
        btn1.setOnClickListener(v -> {
            soma = soma + 16.6;
            myLittleToast("+16.6 " + getString(R.string.Denno_coin));
            quartoAnimal();

        });
        btn2.setOnClickListener(v -> quartoAnimal());
        btn3.setOnClickListener(v -> quartoAnimal());
        btn4.setOnClickListener(v -> quartoAnimal());
    }

    private void quartoAnimal(){

        animal.setImageResource(R.drawable.ic_lobster);
        nome1.setText(getResources().getString(R.string.lagosta));
        nome2.setText(getResources().getString(R.string.caranguejo));
        nome3.setText(getResources().getString(R.string.crustáceo));
        nome4.setText(getResources().getString(R.string.lula));
        btn1.setOnClickListener(v -> {
            soma = soma + 16.6;
            myLittleToast("+16.6 " + getString(R.string.Denno_coin));
            quintoAnimal();

        });
        btn2.setOnClickListener(v -> quintoAnimal());
        btn3.setOnClickListener(v -> quintoAnimal());
        btn4.setOnClickListener(v -> quintoAnimal());
    }

    private void quintoAnimal(){

        animal.setImageResource(R.drawable.ic_horse);
        nome1.setText(getResources().getString(R.string.cachorro));
        nome2.setText(getResources().getString(R.string.asno));
        nome3.setText(getResources().getString(R.string.cavalo));
        nome4.setText(getResources().getString(R.string.alpaca));
        btn1.setOnClickListener(v -> sextoAnimal());
        btn2.setOnClickListener(v -> sextoAnimal());
        btn3.setOnClickListener(v -> {
            soma = soma + 16.6;
            myLittleToast("+16.6 " + getString(R.string.Denno_coin));
            sextoAnimal();

        });
        btn4.setOnClickListener(v -> sextoAnimal());
    }

    private void sextoAnimal(){

        animal.setImageResource(R.drawable.ic_dog);
        nome1.setText(getResources().getString(R.string.cavalo));
        nome2.setText(getResources().getString(R.string.lobo_guará));
        nome3.setText(getResources().getString(R.string.raposa));
        nome4.setText(getResources().getString(R.string.cachorro));
        btn1.setOnClickListener(v -> setimoAnimal());
        btn2.setOnClickListener(v -> setimoAnimal());
        btn3.setOnClickListener(v -> setimoAnimal());
        btn4.setOnClickListener(v -> {
            soma = soma + 16.6;
            myLittleToast("+16.6 " + getString(R.string.Denno_coin));
            setimoAnimal();
        });
    }

    private void setimoAnimal(){

        animal.setImageResource(R.drawable.ic_cat);
        nome1.setText(getResources().getString(R.string.onça));
        nome2.setText(getResources().getString(R.string.gato));
        nome3.setText(getResources().getString(R.string.leopardo));
        nome4.setText(getResources().getString(R.string.ginette));
        btn1.setOnClickListener(v -> oitavoAnimal());
        btn2.setOnClickListener(v -> {
            soma = soma + 16.6;
            myLittleToast("+16.6 " + getString(R.string.Denno_coin));
            oitavoAnimal();

        });
        btn3.setOnClickListener(v -> oitavoAnimal());
        btn4.setOnClickListener(v -> oitavoAnimal());
    }

    private void oitavoAnimal(){

        animal.setImageResource(R.drawable.ic_bee);
        nome1.setText(getResources().getString(R.string.abelha));
        nome2.setText(getResources().getString(R.string.cigarra));
        nome3.setText(getResources().getString(R.string.leopardo));
        nome4.setText(getResources().getString(R.string.pernilongo));
        btn1.setOnClickListener(v -> {
            soma = soma + 16.6;
            myLittleToast("+16.6 " + getString(R.string.Denno_coin));
            nonoAnimal();

        });
        btn2.setOnClickListener(v -> nonoAnimal());
        btn3.setOnClickListener(v -> nonoAnimal());
        btn4.setOnClickListener(v -> nonoAnimal());
    }

    private void nonoAnimal(){

        animal.setImageResource(R.drawable.ic_whale);
        nome1.setText(getResources().getString(R.string.orca));
        nome2.setText(getResources().getString(R.string.boto));
        nome3.setText(getResources().getString(R.string.baleia));
        nome4.setText(getResources().getString(R.string.golfinho));
        btn1.setOnClickListener(v -> decimoAnimal());
        btn2.setOnClickListener(v -> decimoAnimal());
        btn3.setOnClickListener(v -> {
            soma = soma + 16.6;
            myLittleToast("16.6 " + getString(R.string.Denno_coin));
            decimoAnimal();

        });
        btn4.setOnClickListener(v -> decimoAnimal());
    }

    private void decimoAnimal(){

        animal.setImageResource(R.drawable.ic_dolphin);
        nome1.setText(getResources().getString(R.string.baleia));
        nome2.setText(getResources().getString(R.string.orca));
        nome3.setText(getResources().getString(R.string.golfinho));
        nome4.setText(getResources().getString(R.string.boto));
        btn1.setOnClickListener(v -> decimoPrimeiroAnimal());
        btn2.setOnClickListener(v -> decimoPrimeiroAnimal());
        btn3.setOnClickListener(v -> {
            soma = soma + 16.6;
            myLittleToast("16.6 " + getString(R.string.Denno_coin));
            decimoPrimeiroAnimal();
        });
        btn4.setOnClickListener(v -> decimoPrimeiroAnimal());
    }

    private void decimoPrimeiroAnimal(){

        animal.setImageResource(R.drawable.ic_squirrel);
        nome1.setText(getResources().getString(R.string.hamster));
        nome2.setText(getResources().getString(R.string.esquilo));
        nome3.setText(getResources().getString(R.string.chinchila));
        nome4.setText(getResources().getString(R.string.gerbil));
        btn1.setOnClickListener(v -> decimoSegundoAnimal());
        btn2.setOnClickListener(v -> {
            soma = soma + 16.6;
            myLittleToast("16.6 " + getString(R.string.Denno_coin));
            decimoSegundoAnimal();

        });
        btn3.setOnClickListener(v -> decimoSegundoAnimal());
        btn4.setOnClickListener(v -> decimoSegundoAnimal());
    }

    private void decimoSegundoAnimal(){

        nome2.setTextSize(14);
        nome2.setPadding(0, 8, 0, 0);
        animal.setImageResource(R.drawable.ic_snake);
        nome1.setText(getResources().getString(R.string.cobra));
        nome2.setText(getResources().getString(R.string.anfisbenos));
        nome3.setText(getResources().getString(R.string.lagarto));
        nome4.setText(getResources().getString(R.string.crocodilo));
        btn1.setOnClickListener(v -> {
            soma = soma + 16.6;
            myLittleToast("+16.6 " + getString(R.string.Denno_coin));
            addCoin();
        });
        btn2.setOnClickListener(v -> addCoin());
        btn3.setOnClickListener(v -> addCoin());
        btn4.setOnClickListener(v -> addCoin());
    }

    @Override
    public void onInitializationComplete() {
        DisplayRewardedAd();
    }

    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError unityAdsInitializationError, String s) {

    }
    public void DisplayRewardedAd () {
        UnityAds.load(adUnitId, loadListener);
    }
}