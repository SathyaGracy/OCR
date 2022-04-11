package com.zeyaly.extractor;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.tabs.TabLayout;
import com.zeyaly.extractor.databinding.ActivityMainBinding;
import com.zeyaly.extractor.fragment.Camera;
import com.zeyaly.extractor.fragment.DataFragment;
import com.zeyaly.extractor.fragment.Gallery;
import com.zeyaly.extractor.fragment.Setting;
import com.zeyaly.extractor.session.Session;
import com.zeyaly.extractor.utils.TransistionAnimation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeScreen extends AppCompatActivity implements RewardedVideoAdListener {
    ActivityMainBinding binding;
    View tabviewhome, tabviewpassbook,tabviewCloud, tabviewsetting;
    TextView tabHomeTxt,tabPassTxt,tabCloudTxt,tabSettingTxt;
    ImageView tabHomeimg, tabPassbookimg,tabCloudimg, tabSettingimg/*, icon_selectHome, icon_selectPassbook, icon_selectSetting*/;
    private RewardedVideoAd mRewardedVideoAd;
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        TransistionAnimation transistionAnimation = new TransistionAnimation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(transistionAnimation.enterTransition());
            getWindow().setSharedElementReturnTransition(transistionAnimation.returnTransition());
        }
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        intView();
        if(Integer.parseInt(session.getKEYPoints())<40) {
            loadRewardedVideoAd();
        }



    }

    private void intView() {
        session=new Session(this);
        tabviewhome = LayoutInflater.from(HomeScreen.this).inflate(R.layout.tab_title_layout, null);
        tabviewpassbook = LayoutInflater.from(HomeScreen.this).inflate(R.layout.tab_title_layout, null);
        tabviewsetting = LayoutInflater.from(HomeScreen.this).inflate(R.layout.tab_title_layout, null);
        tabviewCloud = LayoutInflater.from(HomeScreen.this).inflate(R.layout.tab_title_layout, null);
        tabHomeimg = tabviewhome.findViewById(R.id.icon);
        tabPassbookimg = tabviewpassbook.findViewById(R.id.icon);
        tabCloudimg = tabviewCloud.findViewById(R.id.icon);
        tabSettingimg = tabviewsetting.findViewById(R.id.icon);
        tabHomeTxt = tabviewhome.findViewById(R.id.text);
        tabPassTxt = tabviewpassbook.findViewById(R.id.text);
        tabCloudTxt = tabviewCloud.findViewById(R.id.text);
        tabSettingTxt = tabviewsetting.findViewById(R.id.text);
      /*  icon_selectHome = tabviewhome.findViewById(R.id.icon_select);
        icon_selectPassbook = tabviewpassbook.findViewById(R.id.icon_select);
        icon_selectSetting = tabviewsetting.findViewById(R.id.icon_select);*/
        binding.tab.setupWithViewPager(binding.viewpager);
        setupViewPager(binding.viewpager);
        binding.viewpager.setPagingEnabled(false);
        setupTabIcons();
        onChangeListerner();
        binding.viewpager.setOffscreenPageLimit(4);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Camera(), "Camera");
        adapter.addFrag(new Gallery(), "Gallery");
        adapter.addFrag(new DataFragment(), "Data");
        adapter.addFrag(new Setting(), "Settings");
        viewPager.setAdapter(adapter);
    }


    @SuppressLint("NewApi")
    private void setupTabIcons() {

        tabHomeimg.setImageDrawable(ContextCompat.getDrawable(HomeScreen.this, R.mipmap.camera));
        tabHomeimg.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
        tabHomeTxt.setText("Camera");
        tabHomeTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        Objects.requireNonNull(binding.tab.getTabAt(0)).setCustomView(tabviewhome);

        tabPassbookimg.setImageDrawable(ContextCompat.getDrawable(HomeScreen.this, R.mipmap.photo_unselect));
      //  tabPassbookimg.setColorFilter(ContextCompat.getColor(this, R.color.shade), android.graphics.PorterDuff.Mode.MULTIPLY);
        tabPassbookimg.setColorFilter(ContextCompat.getColor(this, R.color.shade), android.graphics.PorterDuff.Mode.SRC_IN);
        //  icon_selectPassbook.setVisibility(View.GONE);
        tabPassTxt.setText("Gallery");
        tabPassTxt.setTextColor(getResources().getColor(R.color.shade));
        Objects.requireNonNull(binding.tab.getTabAt(1)).setCustomView(tabviewpassbook);

        tabCloudimg.setImageDrawable(ContextCompat.getDrawable(HomeScreen.this, R.mipmap.cloud_unselect));
        tabCloudimg.setColorFilter(ContextCompat.getColor(this, R.color.shade), android.graphics.PorterDuff.Mode.SRC_IN);
        //icon_selectSetting.setVisibility(View.GONE);
        tabCloudTxt.setText("Data");
        tabCloudTxt.setTextColor(getResources().getColor(R.color.shade));
        Objects.requireNonNull(binding.tab.getTabAt(2)).setCustomView(tabviewCloud);

        tabSettingimg.setImageDrawable(ContextCompat.getDrawable(HomeScreen.this, R.mipmap.setting_unselect));
        tabSettingimg.setColorFilter(ContextCompat.getColor(this, R.color.shade), android.graphics.PorterDuff.Mode.SRC_IN);
        //icon_selectSetting.setVisibility(View.GONE);
        tabSettingTxt.setText("Settings");
        tabSettingTxt.setTextColor(getResources().getColor(R.color.shade));
        Objects.requireNonNull(binding.tab.getTabAt(3)).setCustomView(tabviewsetting);


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void tabUnSelected() {

        tabHomeimg.setImageDrawable(ContextCompat.getDrawable(HomeScreen.this, R.mipmap.camera_unselect));
        // icon_selectHome.setVisibility(View.GONE);
        //tabHomeimg.setColorFilter(ContextCompat.getColor(this, R.color.shade), android.graphics.PorterDuff.Mode.MULTIPLY);
        tabHomeimg.setColorFilter(ContextCompat.getColor(this, R.color.shade), android.graphics.PorterDuff.Mode.SRC_IN);
        tabHomeTxt.setText("Camera");
        tabHomeTxt.setTextColor(getResources().getColor(R.color.shade));
        Objects.requireNonNull(binding.tab.getTabAt(0)).setCustomView(tabviewhome);


        tabPassbookimg.setImageDrawable(ContextCompat.getDrawable(HomeScreen.this, R.mipmap.photo_unselect));
      //  tabPassbookimg.setColorFilter(ContextCompat.getColor(this, R.color.shade), android.graphics.PorterDuff.Mode.MULTIPLY);
        tabPassbookimg.setColorFilter(ContextCompat.getColor(this, R.color.shade), android.graphics.PorterDuff.Mode.SRC_IN);
        // icon_selectPassbook.setVisibility(View.GONE);
        tabPassTxt.setText("Gallery");
        tabPassTxt.setTextColor(getResources().getColor(R.color.shade));
        Objects.requireNonNull(binding.tab.getTabAt(1)).setCustomView(tabviewpassbook);

        tabCloudimg.setImageDrawable(ContextCompat.getDrawable(HomeScreen.this, R.mipmap.cloud_unselect));
        //  tabPassbookimg.setColorFilter(ContextCompat.getColor(this, R.color.shade), android.graphics.PorterDuff.Mode.MULTIPLY);
        tabCloudimg.setColorFilter(ContextCompat.getColor(this, R.color.shade), android.graphics.PorterDuff.Mode.SRC_IN);
        // icon_selectPassbook.setVisibility(View.GONE);
        tabCloudTxt.setText("Data");
        tabCloudTxt.setTextColor(getResources().getColor(R.color.shade));
        Objects.requireNonNull(binding.tab.getTabAt(2)).setCustomView(tabviewCloud);



        tabSettingimg.setImageDrawable(ContextCompat.getDrawable(HomeScreen.this, R.mipmap.setting_unselect));
       // tabSettingimg.setColorFilter(ContextCompat.getColor(this, R.color.shade), android.graphics.PorterDuff.Mode.MULTIPLY);
        tabSettingimg.setColorFilter(ContextCompat.getColor(this, R.color.shade), android.graphics.PorterDuff.Mode.SRC_IN);
        // icon_selectSetting.setVisibility(View.GONE);
        tabSettingTxt.setText("Settings");
        tabSettingTxt.setTextColor(getResources().getColor(R.color.shade));
        Objects.requireNonNull(binding.tab.getTabAt(3)).setCustomView(tabviewsetting);
    }

    private void onChangeListerner() {
        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int postion = binding.tab.getSelectedTabPosition();
                switch (postion) {
                    case 0:
                        tabUnSelected();
                        tabHomeimg.setImageDrawable(ContextCompat.getDrawable(HomeScreen.this, R.mipmap.camera));
                       // tabHomeimg.setColorFilter(ContextCompat.getColor(HomeScreen.this, R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY);
                        tabHomeimg.setColorFilter(ContextCompat.getColor(HomeScreen.this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                        // icon_selectHome.setVisibility(VISIBLE);
                        tabHomeTxt.setText("Camera");
                        tabHomeTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
                        Objects.requireNonNull(binding.tab.getTabAt(0)).setCustomView(tabviewhome);

                        break;

                    case 1:
                        tabUnSelected();
                        tabPassbookimg.setImageDrawable(ContextCompat.getDrawable(HomeScreen.this, R.mipmap.gallery));
                      //  tabPassbookimg.setColorFilter(ContextCompat.getColor(HomeScreen.this, R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY);
                        //icon_selectPassbook.setVisibility(VISIBLE);
                        tabPassTxt.setText("Gallery");
                        tabPassTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
                        tabPassbookimg.setColorFilter(ContextCompat.getColor(HomeScreen.this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                        Objects.requireNonNull(binding.tab.getTabAt(1)).setCustomView(tabviewpassbook);
                        break;
                    case 2:
                        tabUnSelected();
                        tabCloudimg.setImageDrawable(ContextCompat.getDrawable(HomeScreen.this, R.mipmap.cloud));
                        tabCloudimg.setColorFilter(ContextCompat.getColor(HomeScreen.this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                        tabCloudTxt.setText("Data");
                        tabCloudTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
                        Objects.requireNonNull(binding.tab.getTabAt(2)).setCustomView(tabviewCloud);
                        break;
                    case 3:
                        tabUnSelected();
                        tabSettingimg.setImageDrawable(ContextCompat.getDrawable(HomeScreen.this, R.mipmap.setting));
                        //  tabSettingimg.setColorFilter(ContextCompat.getColor(HomeScreen.this, R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY);
                        // icon_selectSetting.setVisibility(VISIBLE);
                        tabSettingimg.setColorFilter(ContextCompat.getColor(HomeScreen.this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                        tabSettingTxt.setText("Settings");
                        tabSettingTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
                        Objects.requireNonNull(binding.tab.getTabAt(3)).setCustomView(tabviewsetting);
                        break;

                }
                //do stuff here

            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
             /*   int tabIconColor = ContextCompat.getColor(BuySellActivity.this, R.color.full_textt_color);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);*/
            }
        });
    }

    @Override
    public void onRewardedVideoAdLoaded() {
       // Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
        showRewardedVideo();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        //Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
       // Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
      //  Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewarded(RewardItem reward) {
        int value=Integer.parseInt(session.getKEYPoints())+100;
        session.setKEYPoints(value+"");
       Toast.makeText(this, "onRewarded! Points: 10" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
       // Toast.makeText(this, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
      //  Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
    }



    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //   getTabView(position);
            return mFragmentTitleList.get(position);

        }


    }
    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }
    private void showRewardedVideo() {
        // make sure the ad is loaded completely before showing it
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }
    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(getString(R.string.rewarded_videos),
                new AdRequest.Builder().build());

        // showing the ad to user
        showRewardedVideo();
    }

}
