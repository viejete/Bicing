package com.example.a53639858v.bicing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


class CustomInfoWindow extends MarkerInfoWindow {

    private Station mSelectedPoi;
    private String mCurrentPhotoPath = null;
    final Button btn;

    public CustomInfoWindow(final MapView mapView) {
        super(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, mapView);

        btn = (Button) (mView.findViewById(org.osmdroid.bonuspack.R.id.bubble_moreinfo));

        if (mCurrentPhotoPath == null) {

            btn.setBackground(ContextCompat.getDrawable(btn.getContext(), R.drawable.no_image));

        } else {

            Drawable photo = Drawable.createFromPath(mCurrentPhotoPath);
            btn.setBackground(photo);
        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mCurrentPhotoPath == null) {

                    dispatchTakePictureIntent();
                    mSelectedPoi.setPhotoUrl(mCurrentPhotoPath);

                } else {

                    File photoToShow = new File(mSelectedPoi.getPhotoUrl());
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_VIEW);
                    i.setDataAndType(Uri.parse("file://" + photoToShow.getAbsolutePath()), "image/*");
                    mView.getContext().startActivity(i);

                }
            }
        });


    }

    @Override
    public void onOpen(Object item) {
        super.onOpen(item);
        mView.findViewById(org.osmdroid.bonuspack.R.id.bubble_moreinfo).setVisibility(View.VISIBLE);

        Marker marker = (Marker) item;
        mSelectedPoi = (Station) marker.getRelatedObject();

        if (mSelectedPoi.getPhotoUrl() != null) {
            mCurrentPhotoPath = mSelectedPoi.getPhotoUrl();

            Bitmap originalBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap , 25 , 40 , true);

            btn.setBackground(new BitmapDrawable(mView.getContext().getResources() , scaledBitmap));
        }
    }


    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // sufix
                storageDir      // direcotry
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(mView.getContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                mView.getContext().startActivity(takePictureIntent);
            }
        }
    }

    void refresh() {
        if (mCurrentPhotoPath != null) {

            Drawable photo = Drawable.createFromPath(mCurrentPhotoPath);

            btn.setBackground(photo);
        }
    }
}