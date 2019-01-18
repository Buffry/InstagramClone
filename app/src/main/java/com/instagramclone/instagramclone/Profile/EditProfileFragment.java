package com.instagramclone.instagramclone.Profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.instagramclone.instagramclone.R;
import com.instagramclone.instagramclone.Utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

public class EditProfileFragment extends Fragment {

    private static final String TAG = "EditProfileFragment";

    private ImageView mProfilePhoto; //mProfilePhoto is an image view

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);
        mProfilePhoto = (ImageView) view.findViewById(R.id.profile_photo); // mProfilePhoto refers to the imageView in profile_photo in the EditProfile fragment


        setProfileImage();

        //back arrow for navigating back to "ProfileActivity"
        ImageView backArrow = (ImageView) view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View  v){
                Log.d(TAG, "onClick: navigating back to profile activty");
                getActivity().finish();
            }
        });

        return view;
    }



    /*
    sets the url of image to be displayed in mProfilePhoto
     */
    private void  setProfileImage(){
        Log.d(TAG, "setProfileImage: setting profile image");
        String imgURL = "cdn.vox-cdn.com/thumbor/W9QjW3HynAcaotTNo45wISHluU8=/0x0:2040x1360/1200x800/filters:focal(857x517:1183x843)/cdn.vox-cdn.com/uploads/chorus_image/image/62857528/wjoel_180413_1777_android_001.0.jpg";
        UniversalImageLoader.setImage(imgURL, mProfilePhoto, null, "https://");
    }
}
