package com.rsschool.android2021;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity implements FirstFragment.ActionPerformedListenerFragment1,
        SecondFragment.ActionPerformedListenerFragment2 {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            openFirstFragment(0, 0, 0);
        }
    }

    private void openFirstFragment(int previousNumber, int minValue, int maxValue) {
        final Fragment firstFragment = FirstFragment.newInstance(previousNumber, minValue, maxValue);
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, firstFragment);
        transaction.commit();
    }


    private void openSecondFragment(int min, int max) {
        final Fragment secondFragment = SecondFragment.newInstance(min, max);
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, secondFragment);
        transaction.commit();
    }

    @Override
    public void onActionPerformedFragment1(int min, int max) {
        openSecondFragment(min, max);
    }

    @Override
    public void onActionPerformedFragment2(int result, int min, int max) { openFirstFragment(result, min, max); }
}
