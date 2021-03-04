package com.ssb.ssbapp.DialogHelper;

import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ssb.ssbapp.R;

public class ImagePickerDailog extends BottomSheetDialogFragment {

    private LinearLayout cameraPick, galleryPick;
    private ImagePickerListner mListner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.image_picker_dailog, container,
                false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle
            savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        cameraPick = view.findViewById(R.id.fromCamera);
        galleryPick = view.findViewById(R.id.fromGallery);

        cameraPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListner.pickImageIntent(0);
                dismiss();
            }
        });

        galleryPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListner.pickImageIntent(1);
                dismiss();
            }
        });


    }


    public interface ImagePickerListner {
        void pickImageIntent(int type);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mListner = (ImagePickerListner) context;
        } catch (Exception e) {
            Toast.makeText(context, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


}
