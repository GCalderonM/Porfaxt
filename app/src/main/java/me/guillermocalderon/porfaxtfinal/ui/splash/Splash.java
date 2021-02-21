package me.guillermocalderon.porfaxtfinal.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bumptech.glide.Glide;

import me.guillermocalderon.porfaxtfinal.R;
import me.guillermocalderon.porfaxtfinal.databinding.ActivitySplashBinding;
import me.guillermocalderon.porfaxtfinal.ui.auth.AuthActivity;

public class Splash extends AppCompatActivity {

    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Glide.with(this).load(R.drawable.loader).into(binding.loadingIcon);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splash.this, AuthActivity.class);
                startActivity(i);
                finish();
            }
        }, 4000);
    }
}