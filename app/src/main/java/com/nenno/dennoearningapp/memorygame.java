package com.nenno.dennoearningapp;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class memorygame extends AppCompatActivity implements IUnityAdsInitializationListener {
    private final Timer _timer = new Timer();
    private Double myCoins;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference userInfo = firebaseDatabase.getReference("userInfo");
    private double click = 0;
    private double genRanNum = 0;
    private double genPos = 0;
    private double genCardType = 0;
    private String cardType = "";
    private boolean gameStart = false;
    private HashMap<String, Object> map = new HashMap<>();
    private HashMap<String, Object> cardVarMap = new HashMap<>();

    private final ArrayList<String> clickedCard = new ArrayList<>();
    private final ArrayList<String> matchedCardList = new ArrayList<>();
    private final ArrayList<String> gen_cardList = new ArrayList<>();

    private LinearLayout button_start;
    private ImageView card1;
    private ImageView card2;
    private ImageView card3;
    private ImageView card4;
    private ImageView card5;
    private ImageView card6;
    private ImageView card7;
    private ImageView card8;
    private ImageView card9;
    private ImageView card10;
    private ImageView card11;
    private ImageView card12;
    private ImageView card13;
    private ImageView card14;
    private ImageView card15;
    private ImageView card16;
    String rewardAd = "Rewarded_Android";

    private TimerTask timer1;
    private TextView timertxt;
    private int progresso = 60;
    private ProgressBar timerProgress;
    private TimerTask ttaskk;
    private final Timer timer = new Timer();
    private LinearLayout thewhatchadbtn;
    private Double total = 0.0;
    private final String unityGameID = "4932075";
    private final Boolean testMode = false;
    private final IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
        @Override
        public void onUnityAdsAdLoaded(String placementId) {
            UnityAds.show(memorygame.this, rewardAd, new UnityAdsShowOptions(), showListener);
        }

        @Override
        public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
            thewhatchadbtn.setEnabled(true);
            UnityAds.load(rewardAd, (IUnityAdsLoadListener) showListener);
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
                button_start.setVisibility(View.VISIBLE);
                thewhatchadbtn.setVisibility(View.GONE);

                // Do not reward the user for skipping the ad
            }
        }
    };
    Toast toast;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorygame);
        initialize();
        initializeLogic();
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(memorygame.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        memorygame.this.finish();
        super.onBackPressed();
    }
    private void initialize() {
        ImageView backic = findViewById(R.id.backic1);
        button_start = findViewById(R.id.button_start);
        TextView startgm = findViewById(R.id.startgm);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);
        card5 = findViewById(R.id.card5);
        card6 = findViewById(R.id.card6);
        card7 = findViewById(R.id.card7);
        card8 = findViewById(R.id.card8);
        card9 = findViewById(R.id.card9);
        card10 = findViewById(R.id.card10);
        card11 = findViewById(R.id.card11);
        card12 = findViewById(R.id.card12);
        card13 = findViewById(R.id.card13);
        card14 = findViewById(R.id.card14);
        card15 = findViewById(R.id.card15);
        card16 = findViewById(R.id.card16);
        timerProgress = findViewById(R.id.timerProgress2);
        timerProgress.setVisibility(View.INVISIBLE);
        TextView thegmname = findViewById(R.id.thegmname);
        timertxt = findViewById(R.id.textView7);
        timertxt.setVisibility(View.INVISIBLE);
        thewhatchadbtn = findViewById(R.id.thewhatchadbtn);
        TextView whatchadtx = findViewById(R.id.whatchadtxt);
        button_start.setVisibility(View.GONE);
        GradientTxt(thegmname);
        GradientTxt(timertxt);
        GradientTxt(startgm);
        GradientTxt(whatchadtx);
        backic.setOnClickListener(v -> {
            startActivity(new Intent(memorygame.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            memorygame.this.finish();
        });

        thewhatchadbtn.setOnClickListener(v -> UnityAds.initialize(getApplicationContext(), unityGameID, testMode, memorygame.this));

        button_start.setOnClickListener(_view -> {
            button_start.setVisibility(View.GONE);
            _new_game();
            timertxt.setVisibility(View.VISIBLE);
            timerProgress.setVisibility(View.VISIBLE);
        });

        card1.setOnClickListener(_view -> {
            card1.setEnabled(false);
            cardType = "card1";
            _gamePlay_logic(card1);
        });

        card2.setOnClickListener(_view -> {
            card2.setEnabled(false);
            cardType = "card2";
            _gamePlay_logic(card2);
        });

        card3.setOnClickListener(_view -> {
            card3.setEnabled(false);
            cardType = "card3";
            _gamePlay_logic(card3);
        });

        card4.setOnClickListener(_view -> {
            card4.setEnabled(false);
            cardType = "card4";
            _gamePlay_logic(card4);
        });

        card5.setOnClickListener(_view -> {
            card5.setEnabled(false);
            cardType = "card5";
            _gamePlay_logic(card5);
        });

        card6.setOnClickListener(_view -> {
            card6.setEnabled(false);
            cardType = "card6";
            _gamePlay_logic(card6);
        });

        card7.setOnClickListener(_view -> {
            card7.setEnabled(false);
            cardType = "card7";
            _gamePlay_logic(card7);
        });

        card8.setOnClickListener(_view -> {
            card8.setEnabled(false);
            cardType = "card8";
            _gamePlay_logic(card8);
        });

        card9.setOnClickListener(_view -> {
            card9.setEnabled(false);
            cardType = "card9";
            _gamePlay_logic(card9);
        });

        card10.setOnClickListener(_view -> {
            card10.setEnabled(false);
            cardType = "card10";
            _gamePlay_logic(card10);
        });

        card11.setOnClickListener(_view -> {
            card11.setEnabled(false);
            cardType = "card11";
            _gamePlay_logic(card11);
        });

        card12.setOnClickListener(_view -> {
            card12.setEnabled(false);
            cardType = "card12";
            _gamePlay_logic(card12);
        });

        card13.setOnClickListener(_view -> {
            card13.setEnabled(false);
            cardType = "card13";
            _gamePlay_logic(card13);
        });

        card14.setOnClickListener(_view -> {
            card14.setEnabled(false);
            cardType = "card14";
            _gamePlay_logic(card14);
        });

        card15.setOnClickListener(_view -> {
            card15.setEnabled(false);
            cardType = "card15";
            _gamePlay_logic(card15);
        });

        card16.setOnClickListener(_view -> {
            card16.setEnabled(false);
            cardType = "card16";
            _gamePlay_logic(card16);
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
                myCoins = dataSnapshot.child("UserCoins").getValue(Double.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initializeLogic() {
        _card_setEnable(false);
        _level_generator();

    }

    private void GradientTxt(final TextView _view){
        Shader myShader = new LinearGradient(
                0, 0, 0, 100,
                Color.rgb(254, 228, 188), Color.rgb(242, 206, 127),
                Shader.TileMode.CLAMP );
        _view.getPaint().setShader( myShader );
    }

    public void _level_generator() {
        for(int _repeat10 = 0; _repeat10 < 16; _repeat10++) {
            genRanNum++;
            gen_cardList.add("card".concat(String.valueOf((long)(genRanNum))));
        }
        Collections.shuffle(gen_cardList);
        cardVarMap = new HashMap<>();
        for(int _repeat18 = 0; _repeat18 < 16; _repeat18++) {
            genCardType++;
            cardVarMap.put(gen_cardList.get((int)(genPos)), String.valueOf((long)(genCardType)));
            genPos++;
            if (genCardType == 8) {
                genCardType = 0;
            }
        }
    }


    public void _show_card(final ImageView _card, final double _type) {
        if (_type == 1) {
            _card.setImageResource(R.drawable.card_cat);
        }
        if (_type == 2) {
            _card.setImageResource(R.drawable.card_frog);
        }
        if (_type == 3) {
            _card.setImageResource(R.drawable.card_monkey);
        }
        if (_type == 4) {
            _card.setImageResource(R.drawable.card_fox);
        }
        if (_type == 5) {
            _card.setImageResource(R.drawable.card_lion);
        }
        if (_type == 6) {
            _card.setImageResource(R.drawable.card_dog);
        }
        if (_type == 7) {
            _card.setImageResource(R.drawable.card_cow);
        }
        if (_type == 8) {
            _card.setImageResource(R.drawable.card_bear);
        }
    }


    public void _open_animation(final ImageView _card) {
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(_card, "ScaleX", 1, 0);
        anim1.setDuration(100);
        anim1.start();
        timer1 = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    _show_card(_card, Double.parseDouble(Objects.requireNonNull(cardVarMap.get(cardType)).toString()));
                    ObjectAnimator anim2 = ObjectAnimator.ofFloat(_card, "ScaleX", 0, 1);
                    anim2.setDuration(100);
                    anim2.start();
                });
            }
        };
        _timer.schedule(timer1, 100);
    }


    public void _close_animation(final ImageView _card) {
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(_card, "ScaleX", 1, 0);
        anim3.setDuration(100);
        anim3.start();
        timer1 = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    _card.setImageResource(R.drawable.card_back);
                    ObjectAnimator anim4 = ObjectAnimator.ofFloat(_card, "ScaleX", 0, 1);
                    anim4.setDuration(100);
                    anim4.start();
                });
            }
        };
        _timer.schedule(timer1, 100);
    }


    public void _card_setEnable(final boolean _setEnable) {
        if (_setEnable) {
            if (!matchedCardList.contains("card1")) {
                card1.setEnabled(true);
            }
            if (!matchedCardList.contains("card2")) {
                card2.setEnabled(true);
            }
            if (!matchedCardList.contains("card3")) {
                card3.setEnabled(true);
            }
            if (!matchedCardList.contains("card4")) {
                card4.setEnabled(true);
            }
            if (!matchedCardList.contains("card5")) {
                card5.setEnabled(true);
            }
            if (!matchedCardList.contains("card6")) {
                card6.setEnabled(true);
            }
            if (!matchedCardList.contains("card7")) {
                card7.setEnabled(true);
            }
            if (!matchedCardList.contains("card8")) {
                card8.setEnabled(true);
            }
            if (!matchedCardList.contains("card9")) {
                card9.setEnabled(true);
            }
            if (!matchedCardList.contains("card10")) {
                card10.setEnabled(true);
            }
            if (!matchedCardList.contains("card11")) {
                card11.setEnabled(true);
            }
            if (!matchedCardList.contains("card12")) {
                card12.setEnabled(true);
            }
            if (!matchedCardList.contains("card13")) {
                card13.setEnabled(true);
            }
            if (!matchedCardList.contains("card14")) {
                card14.setEnabled(true);
            }
            if (!matchedCardList.contains("card15")) {
                card15.setEnabled(true);
            }
            if (!matchedCardList.contains("card16")) {
                card16.setEnabled(true);
            }
        }
        else {
            card1.setEnabled(false);
            card2.setEnabled(false);
            card3.setEnabled(false);
            card4.setEnabled(false);
            card5.setEnabled(false);
            card6.setEnabled(false);
            card7.setEnabled(false);
            card8.setEnabled(false);
            card9.setEnabled(false);
            card10.setEnabled(false);
            card11.setEnabled(false);
            card12.setEnabled(false);
            card13.setEnabled(false);
            card14.setEnabled(false);
            card15.setEnabled(false);
            card16.setEnabled(false);
        }
    }


    public void _new_game() {
        _card_setEnable(true);
        gameStart = true;
        progresso = 60;
        ttaskk = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    progresso--;
                    timertxt.setText(String.valueOf(progresso).concat(" ".concat(getString(R.string.seconds_left))));
                    timerProgress.setProgress(progresso);
                    if ((progresso == 0) || (progresso < 1)){
                        map = new HashMap<>();
                        map.put("UserCoins", myCoins + total );
                        userInfo.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).updateChildren(map);
                        map.clear();
                        userInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                startActivity(new Intent(memorygame.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                startActivity(new Intent(memorygame.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                                finish();
                                Toast.makeText(memorygame.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });


                    }
                });
            }
        };
        timer.scheduleAtFixedRate(ttaskk, 100, 900);
    }


    @SuppressLint("SetTextI18n")
    public void _match_checker() {
        if (Double.parseDouble(Objects.requireNonNull(cardVarMap.get(clickedCard.get(0))).toString()) == Double.parseDouble(Objects.requireNonNull(cardVarMap.get(clickedCard.get(1))).toString())) {
            matchedCardList.add(clickedCard.get(0));
            matchedCardList.add(clickedCard.get(1));
            total = total + 6.25;
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("InflateParams") View toastL = inflater.inflate(R.layout.custom_toast, null);
            LinearLayout lin1 = toastL.findViewById(R.id.toast_bg);
            lin1.setBackgroundResource(R.drawable.mytoast);
            TextView content = toastL.findViewById(R.id.conteudo);
            content.setText("+6.25 " + getString(R.string.Denno_coin));
            toast = new Toast(this);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(toastL);
            toast.show();

            clickedCard.clear();
            if (matchedCardList.size() == 16) {
              ttaskk.cancel();
                map = new HashMap<>();
                map.put("UserCoins", myCoins + total );
                userInfo.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).updateChildren(map);
                userInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        startActivity(new Intent(memorygame.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        memorygame.this.finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        startActivity(new Intent(memorygame.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        memorygame.this.finish();
                        Toast.makeText(memorygame.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                map.clear();

            }
        }
    }


    public void _gamePlay_logic(final ImageView _card) {
        if (gameStart) {
            if (!(click == 2)) {
                click++;
                _open_animation(_card);
                clickedCard.add(cardType);
                if (click == 2) {
                    _match_checker();
                    _card_setEnable(false);
                    timer1 = new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(() -> {
                                if (clickedCard.contains("card1")) {
                                    _close_animation(card1);
                                }
                                if (clickedCard.contains("card2")) {
                                    _close_animation(card2);
                                }
                                if (clickedCard.contains("card3")) {
                                    _close_animation(card3);
                                }
                                if (clickedCard.contains("card4")) {
                                    _close_animation(card4);
                                }
                                if (clickedCard.contains("card5")) {
                                    _close_animation(card5);
                                }
                                if (clickedCard.contains("card6")) {
                                    _close_animation(card6);
                                }
                                if (clickedCard.contains("card7")) {
                                    _close_animation(card7);
                                }
                                if (clickedCard.contains("card8")) {
                                    _close_animation(card8);
                                }
                                if (clickedCard.contains("card9")) {
                                    _close_animation(card9);
                                }
                                if (clickedCard.contains("card10")) {
                                    _close_animation(card10);
                                }
                                if (clickedCard.contains("card11")) {
                                    _close_animation(card11);
                                }
                                if (clickedCard.contains("card12")) {
                                    _close_animation(card12);
                                }
                                if (clickedCard.contains("card13")) {
                                    _close_animation(card13);
                                }
                                if (clickedCard.contains("card14")) {
                                    _close_animation(card14);
                                }
                                if (clickedCard.contains("card15")) {
                                    _close_animation(card15);
                                }
                                if (clickedCard.contains("card16")) {
                                    _close_animation(card16);
                                }
                            });
                        }
                    };
                    _timer.schedule(timer1, 400);
                    TimerTask timer2 = new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(() -> {
                                click = 0;
                                _card_setEnable(true);
                                clickedCard.clear();
                            });
                        }
                    };
                    _timer.schedule(timer2,(600));
                }
            }
        }
    }


    @Deprecated
    public void showMessage(String _s) {
        Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
    }

    @Deprecated
    public int getLocationX(View _v) {
        int[] _location = new int[2];
        _v.getLocationInWindow(_location);
        return _location[0];
    }

    @Deprecated
    public int getLocationY(View _v) {
        int[] _location = new int[2];
        _v.getLocationInWindow(_location);
        return _location[1];
    }

    @Deprecated
    public int getRandom(int _min, int _max) {
        Random random = new Random();
        return random.nextInt(_max - _min + 1) + _min;
    }

    @Deprecated
    public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
        ArrayList<Double> _result = new ArrayList<>();
        SparseBooleanArray _arr = _list.getCheckedItemPositions();
        for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
            if (_arr.valueAt(_iIdx))
                _result.add((double)_arr.keyAt(_iIdx));
        }
        return _result;
    }


    @Deprecated
    public float getDip(int _input) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
    }

    @Deprecated
    public int getDisplayWidthPixels() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    @Deprecated
    public int getDisplayHeightPixels() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public void onInitializationComplete() {
            DisplayRewardedAd();
    }

    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError unityAdsInitializationError, String s) {

    }

    public void DisplayRewardedAd () {
        UnityAds.load(rewardAd, loadListener);
    }


}
