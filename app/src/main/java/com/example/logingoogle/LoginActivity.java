package com.example.logingoogle;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logingoogle.back_firebase.FirebaseAuth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextInputEditText ETcorreo, ETcontrasena;
    private FirebaseAuth auth;
    private static final int RC_SIGN_IN = 9001;

    private String correo;
    private String contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = new FirebaseAuth(this);

        ETcorreo = findViewById(R.id.et_correo);
        ETcontrasena = findViewById(R.id.et_contrasena);


        SignInButton signInButton = findViewById(R.id.loginGoogle);
        signInButton.setOnClickListener(this);
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText("Inicia con Google");
                return;
            }
        }
    }

    //TODO:Verificar la documentacion para resetear contrasena
    public void rememberPass(View view) {
    }

    public void logIn(View view) {
        if (!validateForm())
            return;

        auth.signIn(correo, contrasena);
    }

    public void goSignUp(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }

    private boolean validateForm(){
        boolean valid = true;

        correo = ETcorreo.getText().toString();
        if (TextUtils.isEmpty(correo)){
            ETcorreo.setError("Campo Vacio");
            valid = false;
        } else {
            ETcorreo.setError(null);
        }

        contrasena = ETcontrasena.getText().toString();
        if (TextUtils.isEmpty(contrasena)){
            ETcontrasena.setError("Campo Vacio");
            valid = false;
        } else {
            ETcontrasena.setError(null);
        }
        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                auth.signInGoogle(account);
            } catch (ApiException e){
                Log.e("ErrorFirebase", e.toString());
                Toast.makeText(this, "Fallo en la autentificacion con Google", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginGoogle:
                Intent signInIntent = auth.mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
        }
    }
}
