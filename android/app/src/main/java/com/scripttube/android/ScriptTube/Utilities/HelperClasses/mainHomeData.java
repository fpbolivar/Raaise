package com.scripttube.android.ScriptTube.Utilities.HelperClasses;

import android.os.Handler;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;

public class mainHomeData {
    public static void ShowPause(LottieAnimationView view, int res) {
        view.setVisibility(View.VISIBLE);
        view.setAnimation(res);
        view.playAnimation();
        view.playAnimation();

    }
    public static void ShowPlayPause(LottieAnimationView view, int res) {
        view.setVisibility(View.VISIBLE);
        view.setAnimation(res);
        view.playAnimation();
        view.playAnimation();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);
            }
        }, 600);
    }

    public static void ShowHeart(LottieAnimationView view) {
        view.setVisibility(View.VISIBLE);
        view.playAnimation();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);
            }
        }, 600);
    }
}
