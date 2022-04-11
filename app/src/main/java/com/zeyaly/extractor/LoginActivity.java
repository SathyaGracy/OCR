package com.zeyaly.extractor;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.zeyaly.extractor.databinding.SignupLayoutBinding;
import com.zeyaly.extractor.session.Session;
import com.zeyaly.extractor.utils.TransistionAnimation;

public class LoginActivity extends AppCompatActivity {
    SignupLayoutBinding binding;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.signup_layout);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        TransistionAnimation transistionAnimation = new TransistionAnimation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(transistionAnimation.enterTransition());
            getWindow().setSharedElementReturnTransition(transistionAnimation.returnTransition());
        }
        intView();

    }

    private void intView() {
        session = new Session(this);
        session.setLogin("Login");
        session.setKEYPoints("100");
        binding.signupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.usernameEdt.getText().toString().equalsIgnoreCase("") ||
                        !binding.usernameEdt.getText().toString().equalsIgnoreCase("") ||
                        !binding.usernameEdt.getText().toString().equalsIgnoreCase("")) {
                    session.setKEY_User_Name(binding.usernameEdt.getText().toString());
                    session.setKEY_Email_ID(binding.emailIdEdt.getText().toString());
                    session.setKEY_Mobile_NO(binding.mobileEdt.getText().toString());
                    Intent intent = new Intent(LoginActivity.this, HomeScreen.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Enter Details", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.bottomHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, HomeScreen.class);
                startActivity(intent);

            }
        });

    }
}
