package com.example.logingoogle;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logingoogle.back_firebase.FirebaseAuth;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText ETcorreo, ETcontrasena, ETrContrasena;
    private FirebaseAuth auth;

    private String correo;
    private String contrasena;
    private String rContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = new FirebaseAuth(this);

        ETcorreo = findViewById(R.id.et_correo);
        ETcontrasena = findViewById(R.id.et_contrasena);
        ETrContrasena = findViewById(R.id.et_rcontrasena);
    }

    public void registrer(View view) {
        if (!validateForm())
            return;

        auth.createAccount(correo, contrasena);
    }

    public void goLogIn(View view) {
        startActivity(new Intent(this, LoginActivity.class));
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

        if (!TextUtils.isEmpty(contrasena) && !TextUtils.isEmpty(rContrasena) && !contrasena.equals(rContrasena)){
            ETrContrasena.setError("Verifique que password coincidan");
            valid = false;
        } else {
            ETrContrasena.setError(null);
        }

        rContrasena = ETrContrasena.getText().toString();
        if (TextUtils.isEmpty(rContrasena)){
            ETrContrasena.setError("Campo Vacio");
            valid = false;
        } else {
            if (!TextUtils.isEmpty(contrasena) && !contrasena.equals(rContrasena)){
                ETrContrasena.setError("Verifique que password coincidan");
                valid = false;
            } else {
                ETrContrasena.setError(null);
            }
        }

        return valid;
    }
}
