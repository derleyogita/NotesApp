package com.example.notes.helper;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.notes.listener.PermissionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author yogitad
 * @since 17-08-2022
 * This helper class is used to handle runtime permission flow
 **/

public class PermissionHelper {

    private static PermissionHelper permissionHelper = null;

    private PermissionHelper() {
        // No instance
    }

    public static PermissionHelper getInstance() {
        if (permissionHelper == null) {
            permissionHelper = new PermissionHelper();
        }
        return permissionHelper;
    }

    /**
     * This is used to grant permission according to need
     *
     * @param fragment              fragment
     * @param permission:permission which need to give grant
     * @param permissionCode:       permission code
     * @param permissionListener:   to handle action of permission dialog
     * @return true if permission granted else false
     */
    public boolean checkPermission(Fragment fragment, String permission, int permissionCode, PermissionListener permissionListener) {

        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(fragment.getContext()), permission) != PackageManager.PERMISSION_GRANTED) {
                if (fragment.shouldShowRequestPermissionRationale(permission)) {
                    permissionListener.isPermissionDenied(true);
                } else {
                    fragment.requestPermissions(new String[]{permission}, permissionCode);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    /**
     * Check for permissions available else show permissions dialog
     *
     * @param activity    current activity context
     * @param permissions permissions to ask for
     */
    public boolean checkPermissions(Activity activity, String[] permissions, int permissionCode) {
        int result;

        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = checkSelfPermission(activity, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(activity,
                    listPermissionsNeeded.toArray(new String[0]), permissionCode);
            return false;
        }
        return true;
    }
}

