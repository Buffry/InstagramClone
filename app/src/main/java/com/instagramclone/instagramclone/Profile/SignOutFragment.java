package com.instagramclone.instagramclone.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.instagramclone.instagramclone.Login.LoginActivity;
import com.instagramclone.instagramclone.R;

public class SignOutFragment extends Fragment {

    private static final String TAG = "SignOutfragment";

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ProgressBar mProgressBar;
    private TextView tvSignOut, tvSigningOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signout, container, false);
        tvSignOut = (TextView) view.findViewById(R.id.tvConfirmSignout);
        tvSigningOut = (TextView) view.findViewById(R.id.tvSigningOut);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        Button btnConfirmSignout = (Button) view.findViewById(R.id.btnConfirmSignout);

        mProgressBar.setVisibility(View.GONE);
        tvSigningOut.setVisibility(View.GONE);

        setupFirebaseAuth();

        btnConfirmSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to sign out.");
                mProgressBar.setVisibility(View.VISIBLE);
                tvSigningOut.setVisibility(View.VISIBLE);

                mAuth.signOut();
                getActivity().finish();


            }
        });

        return view;
    }

     /*
   ------------------------------------------- Firebase -----------------------------------------
    */




    /*
    set up firebase authentication to check if a user is currently logged in to the app
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");



        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null){
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in: " + user.getUid());
                }else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                    Log.d(TAG, "onAuthStateChanged: navigating back to login screen");
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener!= null)
            mAuth.removeAuthStateListener(mAuthListener);
    }
}
