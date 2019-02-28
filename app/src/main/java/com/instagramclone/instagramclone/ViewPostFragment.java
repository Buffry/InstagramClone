package com.instagramclone.instagramclone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.instagramclone.instagramclone.Models.Photo;
import com.instagramclone.instagramclone.Models.UserAccountSettings;
import com.instagramclone.instagramclone.Utils.BottomNavigationViewHelper;
import com.instagramclone.instagramclone.Utils.FirebaseMethods;
import com.instagramclone.instagramclone.Utils.GridImageAdapter;
import com.instagramclone.instagramclone.Utils.SquareImageView;
import com.instagramclone.instagramclone.Utils.UniversalImageLoader;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewPostFragment extends Fragment {

    private static final String TAG  = "ViewPostFragment";

    public ViewPostFragment(){
        super();
        setArguments(new Bundle());
    }

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    //widgets
    private SquareImageView mPostImage;
    private BottomNavigationViewEx bottomNavigationView;
    private TextView mBackLabel, mCaption, mUsername, mTimeStamp;
    private ImageView mBackArrow, mEllipses, mHeartRed, mHeartWhite, mSpeechBubble, mProfileImage;

    //vars
    private Photo mPhoto;
    private int mActivityNumber = 0;
    private String photoUsername = "";
    private String profilePhotoUrl = "";
    private UserAccountSettings mUserAccountSettings;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_view_post, container, false);
         mPostImage = (SquareImageView) view.findViewById(R.id.post_image);
         bottomNavigationView = (BottomNavigationViewEx) view.findViewById(R.id.bottomNavViewBar);
         mBackArrow = (ImageView) view.findViewById(R.id.backArrow);
         mBackLabel = (TextView) view.findViewById(R.id.tvBackLabel);
         mCaption = (TextView) view.findViewById(R.id.image_caption);
         mUsername = (TextView) view.findViewById(R.id.username);
         mTimeStamp = (TextView) view.findViewById(R.id.image_time_posted);
         mEllipses = (ImageView) view.findViewById(R.id.ivEllipses);
         mHeartRed = (ImageView) view.findViewById(R.id.image_heart_red);
         mHeartWhite = (ImageView) view.findViewById(R.id.image_heart);
         mSpeechBubble = (ImageView) view.findViewById(R.id.speech_bubble);
         mProfileImage = (CircleImageView) view.findViewById(R.id.profile_photo);


         try{
             mPhoto = getPhotoFromBundle();
             UniversalImageLoader.setImage(mPhoto.getImage_path(), mPostImage, null, "");
             mActivityNumber = getActivityNumFromBundle();


         }catch (NullPointerException e){
             Log.e(TAG, "onCreateView: NullPointerException: photo was null from bundle: " + e.getMessage() );
         }

         setupFirebaseAuth();
         setupBottomNavigationView();
         getPhotoDetails();
         //setupWidgets();

         return view;
    }

    private void getPhotoDetails(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_user_account_settings))
                .orderByChild(getString(R.string.field_user_id))
                .equalTo(mPhoto.getUser_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    mUserAccountSettings = singleSnapshot.getValue(UserAccountSettings.class);

                }
                setupWidgets();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });
    }

    private void setupWidgets(){
        String timestampDiff = getTimeStampDifference();
        if(!timestampDiff.equals("0")){
            mTimeStamp.setText(timestampDiff + " " + getString(R.string.days_ago));
        }else {
            mTimeStamp.setText(getString(R.string.today));
        }
        UniversalImageLoader.setImage(mUserAccountSettings.getProfile_photo(), mProfileImage, null, "");
        mUsername.setText(mUserAccountSettings.getUsername());
    }

    /*
    returns a string representing the number of days ago the post was made
     */
    private String getTimeStampDifference(){
        Log.d(TAG, "getTimeStampDifference: getting timestamp difference.");

        String difference = "";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA);
        sdf.setTimeZone(TimeZone.getTimeZone("Canada/Eastern")); //google android list of timezones to get other timezones
        Date today = c.getTime();
        sdf.format(today);
        Date timeStamp;
        final String photoTimeStamp = mPhoto.getDate_created();
        try{
            timeStamp = sdf.parse(photoTimeStamp);
            difference = String.valueOf(Math.round(((today.getTime() - timeStamp.getTime()) / 1000 / 60 / 60 / 24 )));
        }catch (ParseException e){
            Log.e(TAG, "getTimeStampDifference: ParseException: " + e.getMessage() );
            difference = "0";
        }
        return difference;
    }

    /*
    retrieve the photo from the incoming bundle from ProfileActivity interface
     */
    private Photo getPhotoFromBundle(){
        Log.d(TAG, "getPhotoFromBUndle: arguments: " + getArguments());

        Bundle bundle = this.getArguments();
        if (bundle != null){
            return bundle.getParcelable(getString(R.string.photo));
        }else {
            return null;
        }
    }

    /*
    retrieve the activity number from the incoming bundle from ProfileActivity interface
     */
    private int getActivityNumFromBundle(){
        Log.d(TAG, "getActivityNumFromBundle: arguments: " + getArguments());

        Bundle bundle = this.getArguments();
        if (bundle != null){
            return bundle.getInt(getString(R.string.activity_number));
        }else {
            return 0;
        }
    }

    /*
    Bottom Navigation View Setup
     */
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: settingUpBottomNavigationView");
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(getActivity(), getActivity(), bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(mActivityNumber);
        menuItem.setChecked(true);
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
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

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
