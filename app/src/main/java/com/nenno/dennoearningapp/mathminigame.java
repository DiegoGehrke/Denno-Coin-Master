package com.nenno.dennoearningapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class mathminigame extends AppCompatActivity implements IUnityAdsInitializationListener {

    private TextView question;
    private TextView rewarddescc;
    private TextView timer_txt;
    private Boolean second = false;
    private Boolean third = false;
    private Boolean fourth = false;
    private Boolean fifth = false;
    private Boolean sixth = false;
    private LinearLayout ans1;
    private LinearLayout ans2;
    private LinearLayout ans3;
    private LinearLayout ans4;
    private final Timer _timer = new Timer();
    private int progresso = 60;
    private ProgressBar timerProgress;
    private FirebaseAuth firebaseAuth;
    private Double myCoins;
    private Double soma = 0.0;
    private TextView txt_ans1, txt_ans2, txt_ans3, txt_ans4;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference userInfo = firebaseDatabase.getReference("userInfo");
    private final String unityGameID = "4932075";
    private final Boolean testMode = false;
    private final String adUnitId = "Rewarded_Android";
    LinearLayout thewhatchadbtn;
    LinearLayout oneone;
    LinearLayout twotwo;
    LinearLayout blackboard;

    private final IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
        @Override
        public void onUnityAdsAdLoaded(String placementId) {
            UnityAds.show(mathminigame.this, adUnitId, new UnityAdsShowOptions(), showListener);
        }

        @Override
        public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
            thewhatchadbtn.setEnabled(true);
            UnityAds.load(adUnitId, (IUnityAdsLoadListener) showListener);
            Log.e("UnityAdsExample", "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
        }
    };

    private final IUnityAdsShowListener showListener = new IUnityAdsShowListener() {
        @Override
        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
            thewhatchadbtn.setEnabled(true);

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

                thewhatchadbtn.setVisibility(View.GONE);
                myLittleToast("O Tempo está correndo!");
                timerProgress.setVisibility(View.VISIBLE);
                timer_txt.setVisibility(View.VISIBLE);
                oneone.setAlpha(1.0F);
                twotwo.setAlpha(1.0F);
                blackboard.setAlpha(1.0F);
                loadFirstQuest();
                initializeLogic();
            } else {
                // Do not reward the user for skipping the ad
                startActivity(new Intent(mathminigame.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                mathminigame.this.finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mathminigame);
        initialize();
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


    }

    private void initialize() {
        question = findViewById(R.id.question);
        rewarddescc = findViewById(R.id.mycoins_txt);
        ans1 = findViewById(R.id.ans1);
                ans2 = findViewById(R.id.ans2);
                ans3 = findViewById(R.id.ans3);
                ans4 = findViewById(R.id.ans4);
                oneone = findViewById(R.id.oneone);
                twotwo = findViewById(R.id.twotwo);
              oneone.setAlpha(0.5F);
              twotwo.setAlpha(0.5F);
        timer_txt = findViewById(R.id.timer_txt);
        timerProgress = findViewById(R.id.timerProgress);
        timer_txt.setVisibility(View.INVISIBLE);
        timerProgress.setVisibility(View.INVISIBLE);
        ImageView backic = findViewById(R.id.backic1);
        TextView gameName = findViewById(R.id.thegmname);
        TextView whatchadtxt = findViewById(R.id.whatchadtxt);
        thewhatchadbtn = findViewById(R.id.thewhatchadbtn);
        txt_ans1 = findViewById(R.id.ANS1);
        txt_ans2 = findViewById(R.id.ANS2);
        txt_ans3 = findViewById(R.id.ANS3);
        txt_ans4 = findViewById(R.id.ANS4);
        GradientTxt(gameName);
        GradientTxt(timer_txt);
        GradientTxt(rewarddescc);
        GradientTxt(question);
        GradientTxt(txt_ans1);
        GradientTxt(txt_ans2);
        GradientTxt(txt_ans3);
        GradientTxt(txt_ans4);
        GradientTxt(whatchadtxt);
        blackboard = findViewById(R.id.blackboard);
        blackboard.setAlpha(0.5F);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mDatabaseRefence = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = mDatabaseRefence.getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        String userKey = user.getUid();
        databaseReference.child("userInfo").child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myCoins = dataSnapshot.child("UserCoins").getValue(Double.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        thewhatchadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thewhatchadbtn.setEnabled(false);
                UnityAds.initialize(getApplicationContext(), unityGameID, testMode, mathminigame.this);
            }
        });

        backic.setOnClickListener(v -> {
            addCoin();
            startActivity(new Intent(mathminigame.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
           mathminigame.this.finish();
        });

    }
    private void initializeLogic() {
    progresso = 60;
        TimerTask ttask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    progresso--;
                    timer_txt.setText(String.valueOf(progresso).concat(" ".concat(getString(R.string.seconds_left))));
                    timerProgress.setProgress(progresso);
                    if ((progresso == 0) || (progresso < 1)) {
                        _timer.cancel();
                        startActivity(new Intent(mathminigame.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        finish();
                    }
                });
            }
        };
    _timer.scheduleAtFixedRate(ttask, 100, 900);
    }
    @Override
    protected void onDestroy() {
        addCoin();
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(mathminigame.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        mathminigame.this.finish();
        super.onBackPressed();
    }
    private void addCoin(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("UserCoins", myCoins + soma );
        userInfo.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).updateChildren(map);
        map.clear();
        startActivity(new Intent(mathminigame.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        finish();
    }
    @SuppressLint("SetTextI18n")
    private void loadFirstQuest(){
            txt_ans1.setText("128");
            txt_ans2.setText("117");
            txt_ans3.setText("137");
            txt_ans4.setText("138");
            rewarddescc.setText("+16.6 ".concat(getString(R.string.will_earn)));
            question.setText("41 + 97 = ?");
            ans1.setOnClickListener(v -> {
                second = true;
                loadSecondQuest();

            });
            ans2.setOnClickListener(v -> {
                second = true;
                loadSecondQuest();

            });
            ans3.setOnClickListener(v -> {
                second = true;
                loadSecondQuest();

            });
            ans4.setOnClickListener(v -> {
                second = true;
                loadSecondQuest();
                soma = soma + 16.6;
                myLittleToast("+16.6 " + getString(R.string.Denno_coin));
            });
        }

    @SuppressLint("SetTextI18n")
    private void loadSecondQuest(){
        if (second) {
            txt_ans1.setText("57");
            txt_ans2.setText("42");
            txt_ans3.setText("56");
            txt_ans4.setText("67");
            rewarddescc.setText("+16.6 ".concat(getString(R.string.will_earn)));
            question.setText("15 + 42 = ?");
            ans1.setOnClickListener(v -> {
                third = true;
                loadThirdQuest();
                soma = soma + 16.6;
                myLittleToast("+16.6 " + getString(R.string.Denno_coin));
            });
            ans2.setOnClickListener(v -> {
                third = true;
                loadThirdQuest();
            });
            ans3.setOnClickListener(v -> {
                third = true;
                loadThirdQuest();
            });
            ans4.setOnClickListener(v -> {
                third = true;
                loadThirdQuest();
            });
        }
    }
    @SuppressLint("SetTextI18n")
    private void loadThirdQuest(){
        if (third) {
            txt_ans1.setText("24");
            txt_ans2.setText("16");
            txt_ans3.setText("13");
            txt_ans4.setText("14");
            rewarddescc.setText("+16.6 ".concat(getString(R.string.will_earn)));
            question.setText("26 ÷ 2 = ?");
            ans1.setOnClickListener(v -> {
                loadFourthQuest();
                fourth = true;
            });
            ans2.setOnClickListener(v -> {
                loadFourthQuest();
                fourth = true;
            });
            ans3.setOnClickListener(v -> {
                fourth = true;
                loadFourthQuest();
                soma = soma + 26.6;

                myLittleToast("+16.6 " + getString(R.string.Denno_coin));
            });
            ans4.setOnClickListener(v -> loadFourthQuest());
        }
    }
    @SuppressLint("SetTextI18n")
    private void loadFourthQuest(){
        if (fourth) {
            txt_ans1.setText("18");
            txt_ans2.setText("6");
            txt_ans3.setText("13");
            txt_ans4.setText("12");
            rewarddescc.setText("+25 ".concat(getString(R.string.will_earn)));
            question.setText("36 ÷ 6 = ?");
            ans1.setOnClickListener(v -> {
                fifth = true;
                loadFifthQuest();
            });
            ans2.setOnClickListener(v -> {
                fifth = true;
                loadFifthQuest();
                soma = soma + 25.0;

                myLittleToast("+25 " + getString(R.string.Denno_coin));
            });
            ans3.setOnClickListener(v -> {
                fifth = true;
                loadFifthQuest();
            });
            ans4.setOnClickListener(v -> {
                fifth = true;
                loadFifthQuest();
            });
        }
    }
    @SuppressLint("SetTextI18n")
    private void loadFifthQuest(){
        txt_ans1.setText("268");
        txt_ans2.setText("234");
        txt_ans3.setText("335");
        txt_ans4.setText("134");
        if (fifth) {
            rewarddescc.setText("+16.6 ".concat(getString(R.string.will_earn)));
            question.setText("67 x 4 = ?");
            ans1.setOnClickListener(v -> {
                sixth = true;
                loadSixthQuest();
                soma = soma + 16.6;

                myLittleToast("+16.6 " + getString(R.string.Denno_coin));
            });
            ans2.setOnClickListener(v -> {
                sixth = true;
                loadSixthQuest();
            });
            ans3.setOnClickListener(v -> {
                sixth = true;
                loadSixthQuest();
            });
            ans4.setOnClickListener(v -> {
                sixth = true;
                loadSixthQuest();
            });
        }
    }
    @SuppressLint("SetTextI18n")
    private void loadSixthQuest() {
        if (sixth) {
            txt_ans1.setText("460");
            txt_ans2.setText("550");
            txt_ans3.setText("440");
            txt_ans4.setText("450");
            rewarddescc.setText("+16.6 ".concat(getString(R.string.will_earn)));
            question.setText("5 x 90 = ?");
            ans1.setOnClickListener(v -> addCoin());
            ans2.setOnClickListener(v -> addCoin());
            ans3.setOnClickListener(v -> addCoin());
            ans4.setOnClickListener(v -> {
                soma = soma + 16.6;
                addCoin();
                myLittleToast("+16.6 " + getString(R.string.Denno_coin));
            });
        }
    }
    private void myLittleToast(final String pconteudo){
        Context context = mathminigame.this;
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
    private void GradientTxt(final TextView _view){
        Shader myShader = new LinearGradient(
                0, 0, 0, 100,
                Color.rgb(254, 228, 188), Color.rgb(242, 206, 127),
                Shader.TileMode.CLAMP );
        _view.getPaint().setShader( myShader );
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
