package com.example.logingoogle.back_firebase;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class FirebaseDatabase {

    private com.google.firebase.database.FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private Context context;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    public FirebaseDatabase(Context c) {
        context = c;
        mDatabase = com.google.firebase.database.FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("Usuarios");
        mAuth = new FirebaseAuth(c);
        user = mAuth.getmAuth().getCurrentUser();

        myRef.child(user.getUid()).setValue("DSD");
    }

}
