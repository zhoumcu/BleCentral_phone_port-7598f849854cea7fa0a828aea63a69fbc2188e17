package com.example.sid_fu.blecentral;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.example.sid_fu.blecentral.activity.LoginActivity;
import com.example.sid_fu.blecentral.ui.activity.BaseFragmentActivity;
import com.example.sid_fu.blecentral.utils.Constants;
import com.example.sid_fu.blecentral.utils.Logger;
import com.example.sid_fu.blecentral.utils.SharedPreferences;
import com.example.sid_fu.blecentral.utils.SoundPlayUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tiansj on 15/7/29.
 */
public class SplashActivity extends BaseFragmentActivity {


    @Bind(R.id.imageView)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_splash);
        ButterKnife.bind(this);
        SoundPlayUtils.play(1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initActivity();
            }
        }, 4000);
    }

    private void initActivity() {
        boolean firstTimeUse = SharedPreferences.getInstance().getBoolean(Constants.FIRST_CONFIG, false);
        if (firstTimeUse) {
            //initGuideGallery();
            Intent intent = new Intent();
            intent.setClass(SplashActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            //initLaunchLogo();
            Intent intent = new Intent();
            intent.setClass(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.e("Ondestroy");
    }
}
