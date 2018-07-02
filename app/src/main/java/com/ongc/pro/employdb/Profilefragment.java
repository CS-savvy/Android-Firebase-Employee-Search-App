package com.ongc.pro.employdb;

/**
 * Created by Mukul on 6/20/2017.
 */

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Mukul on 6/20/2017.
 */

public class Profilefragment extends android.support.v4.app.Fragment {

    private TextView t1;
    private Button b1;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private DatabaseReference uDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.profile , container ,false);
        return v;


    }


    public void onViewCreated(View view ,@Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view ,savedInstanceState);

        t1 = (TextView)view.findViewById(R.id.pro_Name);
        b1 =(Button)view.findViewById(R.id.pro_Btn);
        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        uDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);


        uDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user_name = dataSnapshot.child("name").getValue(String.class);
                t1.setText(user_name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAuthListner = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null)
                {
                    startActivity(new Intent(getActivity() , Login.class));
                }

            }
        };


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
            }
        });
    }

    @Override
    public void onStart() {

        mAuth.addAuthStateListener(mAuthListner);
        super.onStart();
    }
}