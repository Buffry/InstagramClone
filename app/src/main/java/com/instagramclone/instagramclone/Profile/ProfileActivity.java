package com.instagramclone.instagramclone.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.instagramclone.instagramclone.R;
import com.instagramclone.instagramclone.Utils.BottomNavigationViewHelper;
import com.instagramclone.instagramclone.Utils.GridImageAdapter;
import com.instagramclone.instagramclone.Utils.UniversalImageLoader;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";

    private static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLUMNS = 3; //sets number of columns in grid on ProfileActivity

    private Context mContext = ProfileActivity.this;

    private ProgressBar mProgressBar;

    private ImageView profilePhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: started.");
        mProgressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
        mProgressBar.setVisibility(View.GONE);

        setupBottomNavigationView();
        setupToolbar();
        setupActivityWidgets();
        setProfileImage();

        tempGridSetup();

    }

    /*
    temporary function to display images on the grid in ProfileActivity by calling images from
    an ArrayList of String URLs
     */
    private void tempGridSetup(){
        ArrayList<String> imgURLs = new ArrayList<>();
        imgURLs.add("https://cdn.pixabay.com/photo/2014/09/14/18/04/dandelion-445228__340.jpg");
        imgURLs.add("https://i.pinimg.com/originals/f4/a8/ac/f4a8ac1a2d768fdfbc73dd35f93a9292.jpg");
        imgURLs.add("https://www.londonbeep.com/wp-content/uploads/2014/04/london_eye_pictures_at_night_1.jpg");
        imgURLs.add("https://assets.saatchiart.com/saatchi/511785/art/5555299/4625111-ZWYSKXHV-25.jpg");
        imgURLs.add("https://i.pinimg.com/originals/ea/e4/a5/eae4a5191fafae3979ea975d206fcd0b.jpg");
        imgURLs.add("https://static1.squarespace.com/static/54505d2be4b0866138cb510c/54505f23e4b079609a872e00/54eccd2ce4b0b080e169b42d/1424805164758/Sirena_PezLeon.jpg?format=500w");
        imgURLs.add("https://i.pinimg.com/originals/97/69/6f/97696f6ef55bbf5f26921c018503c2e0.jpg");
        imgURLs.add("https://www.petmd.com/sites/default/files/breedopedia/arabian_horse.jpg");
        imgURLs.add("https://atgbcentral.com/data/out/152/5306099-beautiful-pictures.jpg");
        imgURLs.add("https://www.bhs.org.uk/~/media/bhs/images/global/general-content-blocks/horizonal-image-copy-blocks/accredited-professionals-495x335/membershiphomepage.ashx?h=335&la=en&w=495");
        imgURLs.add("https://static1.squarespace.com/static/56cc26c98259b5d1a025a426/t/58aebe1de4fcb57eb0eb9881/1487846946611/");

        setupImageGrid(imgURLs);

    }

    /*
    * Sets up the Grid to display the images on the "ProfileActivity"
     */
    private void setupImageGrid(ArrayList<String> imgURLs){
        GridView gridView = (GridView) findViewById(R.id.gridView);

        /*
        sets the width of the images in the grid view
         */
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth); // sets columnwidth to imageWidth

        GridImageAdapter adapter = new GridImageAdapter(mContext, R.layout.layout_grid_imageview, "", imgURLs);
        gridView.setAdapter(adapter);
    }

    /*
    * displays profile image on "ProfileActivity"
     */
    private void setProfileImage(){
        Log.d(TAG, "setProfileImage: setting profile photo");
        String imgURL = "cdn.vox-cdn.com/thumbor/W9QjW3HynAcaotTNo45wISHluU8=/0x0:2040x1360/1200x800/filters:focal(857x517:1183x843)/cdn.vox-cdn.com/uploads/chorus_image/image/62857528/wjoel_180413_1777_android_001.0.jpg";
        UniversalImageLoader.setImage(imgURL, profilePhoto, mProgressBar, "https://");

    }

    /*
    * Initializes widgets on the "ProfileActivity"
     */
    private void setupActivityWidgets(){
        mProgressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
        mProgressBar.setVisibility(View.GONE);
        profilePhoto = (ImageView) findViewById(R.id.profile_photo);
    }

    /*
    Profile top toolbar setup
     */
    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);


        /*
    Allows selection of profile menu at the top right corner of profile top toolbar to
    segue to the AccountSettingsActivity
     */
        ImageView profileMenu = (ImageView)findViewById(R.id.profileMenu);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to account settings.");
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });


    }

    /*
    Bottom Navigation View Setup
     */
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: settingUpBottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


}
