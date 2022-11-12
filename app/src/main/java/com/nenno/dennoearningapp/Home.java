package com.nenno.dennoearningapp;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdk;
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
import com.unity3d.services.banners.IUnityBannerListener;
import com.unity3d.services.banners.UnityBanners;

import java.util.concurrent.TimeUnit;


public class Home extends AppCompatActivity implements IUnityAdsInitializationListener, MaxAdListener, LifecycleOwner{

    TextView btn1, btn, btn2, coinInfo, ID, gm1_desc, gm_desc, gm_desc2;
    ImageView userProfile, configi;

    FirebaseAuth firebaseAuth;
    String userKey;
    LoadingDialog customload;
    private SharedPreferences showCoinSP;
    private FrameLayout adContainerView;
    private static final String rewardedAdUnitId = "Rewarded_Android";
    private View bannerView;
    private MaxInterstitialAd reward;
    private int retryAttempt;
    private int hold = 0;



    private final IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
        @Override
        public void onUnityAdsAdLoaded(String placementId) {

        }

        @Override
        public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
            Log.e("UnityAdsExample", "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
            createRewardedAd();
        }
    };

    private final IUnityAdsShowListener showListener = new IUnityAdsShowListener() {
        @Override
        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
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
            // Reward the user for watching the ad to completion
            // Do not reward the user for skipping the ad
        }
    };

    void createInterstitialAd()
    {
        reward = new MaxInterstitialAd( "reward", this);
        reward.setListener( this );

        // Load the first ad
        reward.loadAd();
    }

    // MAX Ad Listener
    @Override
    public void onAdLoaded(final MaxAd maxAd)
    {
        // Interstitial ad is ready to be shown. interstitialAd.isReady() will now return 'true'
            if (hold < 1){
                reward.showAd();
            }
        // Reset retry attempt
        retryAttempt = 0;
    }

    @Override
    public void onAdLoadFailed(final String adUnitId, final MaxError error)
    {
        // Interstitial ad failed to load
        // AppLovin recommends that you retry with exponentially higher delays up to a maximum delay (in this case 64 seconds)

        retryAttempt++;
        long delayMillis = TimeUnit.SECONDS.toMillis( (long) Math.pow( 2, Math.min( 6, retryAttempt ) ) );

        new Handler().postDelayed(() -> reward.loadAd(), delayMillis );
    }

    @Override
    public void onAdDisplayFailed(final MaxAd maxAd, final MaxError error)
    {
        // Interstitial ad failed to display. AppLovin recommends that you load the next ad.
        reward.loadAd();
    }

    @Override
    public void onAdDisplayed(final MaxAd maxAd) {}

    @Override
    public void onAdClicked(final MaxAd maxAd) {}

    @Override
    public void onAdHidden(final MaxAd maxAd)
    {
        // Interstitial ad is hidden. Pre-load the next ad
        hold = 1;
        reward.loadAd();
    }


    @SuppressLint("ObsoleteSdkInt")
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FirebaseApp.initializeApp(Home.this);
        Context context = Home.this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
         firebaseAuth = FirebaseAuth.getInstance();
        String unityGameID = "4932075";
        boolean testMode = false;
        UnityAds.initialize(getApplicationContext(), unityGameID, testMode, this);
        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, config -> createInterstitialAd());
        btn1 = findViewById(R.id.btngo1);
        customload = new LoadingDialog(this);
        btn2 = findViewById(R.id.btngo2);
        btn = findViewById(R.id.btngo);
        coinInfo = findViewById(R.id.txt_you_have);
        ID = findViewById(R.id.userid);
        gm1_desc = findViewById(R.id.txt_combine);
        gm_desc = findViewById(R.id.txt_try_luck);
        gm_desc2 = findViewById(R.id.txt_math);
        userProfile = findViewById(R.id.userprofile);
        configi = findViewById(R.id.configi);
        ViewGroup btnBanner = findViewById(R.id.bottomBanner);
        LinearLayout showBannerr = findViewById(R.id.showbanner);
        SharedPreferences showRefSP = PreferenceManager
                .getDefaultSharedPreferences(this);
        String name = showRefSP.getString("show", "");
        if (name.equals("true")){
            coinInfo.setVisibility(View.VISIBLE);
        }
        else {
            coinInfo.setVisibility(View.INVISIBLE);
        }
        IUnityBannerListener bannerListener = new IUnityBannerListener() {
            @Override
            public void onUnityBannerLoaded(String s, View view) {
                    if (btnBanner.getParent() != null) {
                        ((ViewGroup) findViewById(R.id.showbanner)).removeView(showBannerr);
                    } else {
                        bannerView = view;
                        ((ViewGroup) findViewById(R.id.bottomBanner)).addView(view);

                }


            }

            @Override
            public void onUnityBannerUnloaded(String s) {

            }

            @Override
            public void onUnityBannerShow(String s) {

            }

            @Override
            public void onUnityBannerClick(String s) {

            }

            @Override
            public void onUnityBannerHide(String s) {

            }

            @Override
            public void onUnityBannerError(String s) {

            }
        };
        UnityBanners.setBannerListener(bannerListener);
        String bannerPlacement = "Banner_Android";
        UnityBanners.loadBanner(Home.this, bannerPlacement);



        GradientTxt(btn); GradientTxt(btn2); GradientTxt(btn1); GradientTxt(coinInfo); GradientTxt(ID); GradientTxt(gm1_desc); GradientTxt(gm_desc); GradientTxt(gm_desc2);
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        userProfile.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, userinfos.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            Home.this.finish();
        });
        btn1.setOnClickListener((View v) -> {
            startActivity(new Intent(Home.this, mathminigame.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            Home.this.finish();
        });
        btn.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, memorygame.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            Home.this.finish();
        });
        btn2.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, guessanimalgame.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            Home.this.finish();
        });
       configi.setOnClickListener(v -> {
           startActivity(new Intent(Home.this, configs.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            Home.this.finish();
       });

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDb = mDatabase.getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        assert user != null;
        userKey = user.getUid();

        mDb.child("userInfo").child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userID = dataSnapshot.child("UserID").getValue(String.class);
                Long userCoin = dataSnapshot.child("UserCoins").getValue(Long.class);
                assert userCoin != null;
                double test = userCoin;
                String testt = String.format("%.2f", (double)test);
                ID.setText(String.format("ID: %s", userID));
                coinInfo.setText(String.format("%s %s %s", getResources().getString(R.string.you_have), testt, getResources().getString(R.string.Denno_coin)));
                if (testt.length() > 8){
                    coinInfo.setTextSize(14);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }



    private void GradientTxt(final TextView _view){
        Shader myShader = new LinearGradient(
                0, 0, 0, 100,
                Color.rgb(254, 228, 188), Color.rgb(242, 206, 127),
                Shader.TileMode.CLAMP );
        _view.getPaint().setShader( myShader );
    }
    public void createRewardedAd()
    {



        reward.setListener( this );

        reward.loadAd();
    }


    @Override
    public void onInitializationComplete() {
                DisplayRewardedAd();
        }


    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError unityAdsInitializationError, String s) {
        Log.e("UnityAdsExample", "Unity Ads initialization failed with error: [" + s + "] " + s);
    }

    public void DisplayRewardedAd () {
        String adUnitId = "Rewarded_Android";
        UnityAds.load(adUnitId, loadListener);
    }

}