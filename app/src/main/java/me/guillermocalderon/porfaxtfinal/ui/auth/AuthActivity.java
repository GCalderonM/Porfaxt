package me.guillermocalderon.porfaxtfinal.ui.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import me.guillermocalderon.porfaxtfinal.MainActivity;
import me.guillermocalderon.porfaxtfinal.R;
import me.guillermocalderon.porfaxtfinal.databinding.ActivityAuthBinding;
import me.guillermocalderon.porfaxtfinal.databinding.ActivityMainBinding;

public class AuthActivity extends AppCompatActivity {

    ActivityAuthBinding binding;
    private final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() == null) {
                showHome(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                        R.drawable.defaultimg);
            }else {
                showHome(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                        FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
            }
            this.finish();
        }

        handleLoginRegister(binding.getRoot());
    }

    public void handleLoginRegister(View view) {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.porfaxt)
                .setTheme(R.style.LoginTheme)
                .build();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                showHome(user.getEmail(), user.getPhotoUrl().toString());
            }else {
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (response == null) {
                    showAlert();
                }
            }
        }
    }

    // Intent que nos pasa el objeto user de Firebase
    private void showHome(String email, String img) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("userEmail", email);
        i.putExtra("userImg", img);
        startActivity(i);
        this.finish();
    }

    private void showHome(String email, int img) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("userEmail", email);
        i.putExtra("userImg", img);
        startActivity(i);
        this.finish();
    }

    // Mostramos una alerta de error
    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Se ha producido un error autenticando al usuario");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}