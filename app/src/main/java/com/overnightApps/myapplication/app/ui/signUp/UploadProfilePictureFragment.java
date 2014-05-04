package com.overnightApps.myapplication.app.ui.signUp;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.overnightApps.myapplication.app.R;
import com.overnightApps.myapplication.app.util.BitmapUtil;
import com.overnightApps.myapplication.app.util.Logger;

import junit.framework.Assert;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link UploadProfilePictureFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class UploadProfilePictureFragment extends Fragment {
    private static final int PROFILE_PICTURE_REQUEST_CODE = 1;
    private static final String USER_SIGN_UP = "userSignUp";
    @InjectView(R.id.iv_profilePicture)
    ImageView iv_profilePicture;
    @InjectView(R.id.b_changePicture)
    Button b_changePicture;
    @InjectView(R.id.tv_selectImage)
    TextView tv_selectImage;

    @InjectView(R.id.b_signUp)
    Button b_done;

    private Bitmap selectedImage;
    private OnSignUpListener onSignUpListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UploadProfilePictureFragment.
     */
    public static UploadProfilePictureFragment newInstance() {
        return new UploadProfilePictureFragment();
    }
    public UploadProfilePictureFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_upload_profile_picture, container, false);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PROFILE_PICTURE_REQUEST_CODE:
                if (resultCode == getActivity().RESULT_OK) {
                    Uri photoUri = data.getData();
                    if (photoUri != null) {
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(
                                    getActivity().getContentResolver(), photoUri);
                            bitmap = BitmapUtil.scaleBitmap(bitmap);
                            Assert.assertNotNull(bitmap);
                            iv_profilePicture.setImageBitmap(bitmap);
                            selectedImage = bitmap;
                            b_done.animate().alpha(1f);
                            b_done.setClickable(true);
                        } catch (IOException e) {
                            Log.e("obtain image", "error while obtaining image", e);
                            Toast.makeText(getActivity(),"Error while obtaining image. Please try again",
                                    Toast.LENGTH_LONG).show();
                        }
                      }
                }
        }
    }

    @OnClick(R.id.b_changePicture)
    public void changePictureClick(Button Button) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("image/*");
            startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"),
                    PROFILE_PICTURE_REQUEST_CODE);
    }

    @OnClick(R.id.b_signUp)
    public void onDoneClick(Button button){
        if(selectedImage == null){
            Logger.p(getActivity(),"Please Select a valid image");
        }else{
            selectedImage = BitmapUtil.scaleBitmap(selectedImage);
            onSignUpListener.profilePictureSelected(selectedImage);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onSignUpListener = (OnSignUpListener)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSignUpListener = null;
    }
}
