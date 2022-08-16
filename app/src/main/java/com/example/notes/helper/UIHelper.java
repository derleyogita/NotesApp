package com.example.notes.helper;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.notes.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;

/**
 * @author yogitad
 * @since 14-08-2022
 * A Singleton class which holds all the common methods which can help in UI operations
 **/

public class UIHelper {

    /**
     * Class object
     */
    private static UIHelper uiHelper = null;

    /**
     * ProgressDialog instance
     */
    private ProgressDialog progressDialog;

    private UIHelper() {
        // No instance
    }

    public static UIHelper getInstance() {
        if (uiHelper == null) {
            uiHelper = new UIHelper();
        }
        return uiHelper;
    }

    /**
     * show snake bar with the provided message
     *
     * @param view    - view
     * @param message - message
     */
    public void showSnackBar(Context context, View view, String message) {

        if (context == null || view == null || TextUtils.isEmpty(message)) {
            return;
        }

        Snackbar snackbar = Snackbar.make(view, message, LENGTH_LONG);

        snackbar.getView();
        snackbar.setAction(R.string.lbl_dismiss, view1 -> snackbar.dismiss());
        snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.white));
        View snackBarView = snackbar.getView();

        TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setMaxLines(5);
        snackbar.show();
    }

    /**
     * This method hides the keyboard
     *
     * @param context Application context
     */
    public void hideKeyboard(Context context) {
        try {
            // Check if no view has focus:
            AppCompatActivity appCompatActivity = (AppCompatActivity) context;
            View view = appCompatActivity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm =
                        (InputMethodManager) appCompatActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows progress dialog
     *
     * @param context Activity context
     */
    public void showProgress(Context context) {
        if (context == null || ((AppCompatActivity) context).isFinishing())
            return;

        if (!((AppCompatActivity) context).isFinishing()) {
            hideProgress(context);
        }
        AppCompatActivity mAppCompatActivity = (AppCompatActivity) context;
        progressDialog = new ProgressDialog(mAppCompatActivity, R.style.ProgressDialog);
        progressDialog.setMessage("Please wait..!");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * Hides progress dialog
     *
     * @param context Activity context
     */
    public void hideProgress(Context context) {
        if (context != null && !((AppCompatActivity) context).isFinishing() && progressDialog != null) {
            if (progressDialog.isShowing()) progressDialog.dismiss();
        }
    }

    /**
     * This method replaces the given fragment in the given layout ID
     *
     * @param context        Activity Context
     * @param fragment       Fragment to be replaces
     * @param tag            Tag to identify the fragment by {@link FragmentManager}
     * @param bundle         To send data as an argument
     * @param containerID    Layout ID in which the fragment will be replaced
     * @param addToBackStack boolean for add/replace fragment to back stack
     */
    public void replaceFragment(Context context,
                                Fragment fragment,
                                String tag,
                                Bundle bundle,
                                int containerID, boolean addToBackStack, boolean isAddFragment) {
        try {
            hideKeyboard(context);
            AppCompatActivity appCompatActivity = (AppCompatActivity) context;
            FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack(tag);
            if (bundle != null && !bundle.isEmpty()) {
                fragment.setArguments(bundle);
            }
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(tag);
            }

            if (isAddFragment) {
                fragmentTransaction.add(containerID, fragment, tag);
            } else {
                fragmentTransaction.replace(containerID, fragment, tag);
            }
            fragmentTransaction.commit();
            appCompatActivity.getSupportFragmentManager().executePendingTransactions();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method validates input email
     *
     * @param email input name string
     * @return true if matching false otherwise
     */
    public boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    /**
     * Method to show date picker dialog
     * @param context    Current activity context
     * @param date       date picker dialog
     * @param myCalendar calendar object
     */
    public void showDatePicker(Context context, DatePickerDialog.OnDateSetListener date, Calendar myCalendar) {
        Calendar maxDate = Calendar.getInstance();
        maxDate.set(Calendar.MONTH, myCalendar.get(Calendar.MONTH) + 6);
        DatePickerDialog dialog = new DatePickerDialog(context, R.style.DatePickerTheme, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(new Date().getTime());
        dialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        dialog.show();
    }


    /**
     * Common method to show bottomSheets
     *
     * @param context        Current activity context
     * @param dialogFragment DialogFragment needs to be shown on screen
     * @param TAG            Tag of current BottomSheetDialogFragment
     */
    public void showDialogFragment(Context context, DialogFragment dialogFragment, String TAG, Bundle bundle) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) context;
        dialogFragment.setArguments(bundle);
        dialogFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
    }



}
