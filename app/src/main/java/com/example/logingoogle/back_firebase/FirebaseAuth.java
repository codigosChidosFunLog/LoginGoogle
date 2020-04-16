package com.example.logingoogle.back_firebase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.logingoogle.MainActivity;
import com.example.logingoogle.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class FirebaseAuth {

    private com.google.firebase.auth.FirebaseAuth mAuth;
    private Context context;
    private static  final String TAG = "Autenficacion Firebase";
    public  GoogleSignInClient mGoogleSignInClient;

    public FirebaseAuth(Context c) {
        context = c;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
    }

    public void start(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        verifyStateUSer(currentUser);
    }

    public void createAccount(String correo, String contrasena){
        mAuth.createUserWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener((Activity)context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            verifyStateUSer(user);
                        } else {
                            Log.d(TAG, "Fallo en la creacion de usuario", task.getException());
                            Toast.makeText(context, "Fallo en la creacion de usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signIn(String correo, String contrasena){
        mAuth.signInWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener((Activity)context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            verifyStateUSer(user);
                        } else {
                            Log.d(TAG, "Fallo en inicio de sesion", task.getException());
                            Toast.makeText(context, "Verifique sus datos o cree un usuario si no lo ha hecho", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public  void signInGoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            verifyStateUSer(user);
                        } else{
                            Log.d(TAG, "Fallo en inicio de sesion con Google", task.getException());
                            Toast.makeText(context, "Fallo de inicio de sesion con Google", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void verifyStateUSer(FirebaseUser user) {
        if (user != null){
            context.startActivity(new Intent(context, MainActivity.class));
            ((Activity)context).finish();
        }
    }

    public void signOut(){
        mAuth.signOut();
    }

    public com.google.firebase.auth.FirebaseAuth getmAuth() {
        return mAuth;
    }
}
