package com.zeyaly.extractor.fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.zeyaly.extractor.R;
import com.zeyaly.extractor.databinding.SettingLayoutBinding;
import com.zeyaly.extractor.session.Session;

public class Setting extends Fragment implements RewardedVideoAdListener {
    SettingLayoutBinding binding;
    private RewardedVideoAd mRewardedVideoAd;
    Session session;
    Boolean isFirst = false;
    Dialog dialog_loading;
    public Setting() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // View view = inflater.inflate(R.layout.setting_layout, container, false);
        binding = DataBindingUtil.inflate(
                inflater, R.layout.setting_layout, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        session = new Session(getActivity());
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        dialog_loading = new Dialog(getActivity());
        dialog_loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLoad();
        binding.points.setText(session.getKEYPoints() + " Pts");
        if (!session.getKEY_User_Name().equalsIgnoreCase(""))
            binding.usernameEdt.setText(session.getKEY_User_Name());
        if (!session.getKEY_Email_ID().equalsIgnoreCase(""))
            binding.emailIdEdt.setText(session.getKEY_Email_ID());
        if (!session.getKEY_Mobile_NO().equalsIgnoreCase(""))
            binding.mobileEdt.setText(session.getKEY_Mobile_NO());

        binding.ltRPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_loading.show();
                loadRewardedVideoAd();
                //showRewardedVideo();
            }
        });
        binding.nextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.usernameEdt.getText().toString().equalsIgnoreCase("") ||
                        !binding.usernameEdt.getText().toString().equalsIgnoreCase("") ||
                        !binding.usernameEdt.getText().toString().equalsIgnoreCase("")) {
                    session.setKEY_User_Name(binding.usernameEdt.getText().toString());
                    session.setKEY_Email_ID(binding.emailIdEdt.getText().toString());
                    session.setKEY_Mobile_NO(binding.mobileEdt.getText().toString());
                    Toast.makeText(getActivity(), "Details Saved successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Enter Details", Toast.LENGTH_SHORT).show();
                }
            }
        });
        isFirst = true;

    }

    @Override
    public void onRewardedVideoAdLoaded() {
        showRewardedVideo();
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        int value = Integer.parseInt(session.getKEYPoints()) + 10;
        session.setKEYPoints(value + "");
        binding.points.setText(session.getKEYPoints() + " Pts");
        Toast.makeText(getActivity(), "onRewarded! Points: 10" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    private void showRewardedVideo() {
        // make sure the ad is loaded completely before showing it
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
            dialog_loading.dismiss();
        }
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(getString(R.string.rewarded_videos),
                new AdRequest.Builder().build());

        // showing the ad to user
        showRewardedVideo();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isFirst) {
            binding.points.setText(session.getKEYPoints() + " Pts");
            session.setKEY_User_Name(binding.usernameEdt.getText().toString());
            session.setKEY_Email_ID(binding.emailIdEdt.getText().toString());
            session.setKEY_Mobile_NO(binding.mobileEdt.getText().toString());
        }

    }
    private void dialogLoad() {
        dialog_loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_loading.setContentView(R.layout.dialog_loading);
        dialog_loading.setCanceledOnTouchOutside(false);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog_loading != null) {
            dialog_loading.dismiss();
            dialog_loading = null;
        }
    }
}
