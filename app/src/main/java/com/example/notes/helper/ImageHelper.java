package com.example.notes.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import com.example.notes.constants.IConstants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yogitad
 * @since 16-08-2022
 * A Singleton class which holds all the common methods which can help in UI operations
 **/

public class ImageHelper{

    private static ImageHelper imageHelper = null;
    private String currentPhotoPath;

    private ImageHelper() {
        // No instance
    }

    public static ImageHelper getInstance() {
        if (imageHelper == null) {
            imageHelper = new ImageHelper();
        }
        return imageHelper;
    }

    /**
     * Method to create file in cache memory
     *
     * @param context - context
     * @return image file
     */
    public File createImageFile(Context context) throws IOException {
        // Create an image file name
        File imageFile = null;
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());
        String imageFileName = IConstants.DIR_IMAGES + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                IConstants.EXT_JPG,         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

}
