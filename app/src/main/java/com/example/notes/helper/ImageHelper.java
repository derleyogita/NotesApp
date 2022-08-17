package com.example.notes.helper;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Base64;

import androidx.core.content.FileProvider;

import com.example.notes.constants.IConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

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
    /**
     * method to get uri from file
     * @param context fragment context
     * @param imageFile - file name
     */
    public Uri getUriFromFile(Context context, File imageFile) {
        return FileProvider.getUriForFile(context, "com.example.notes.fileprovider", imageFile);
    }
}
