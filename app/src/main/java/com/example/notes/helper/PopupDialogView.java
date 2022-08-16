package com.example.notes.helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.notes.R;

/**
 * @author yogitad
 * @since 16-08-2022
 * This helper class used to showcase dialog
 **/

public class PopupDialogView {

    /**
     * Context of the application
     */
    private final Context context;

    /**
     * Constructor to pass the context
     *
     * @param context Context of the application
     */
    public PopupDialogView(Context context) {
        this.context = context;
    }

    /**
     * This method is used to display message/drop down selection UI in an alert dialog with callback on positive, negative and neutral buttons.
     *
     * @param title                  dialog title
     * @param message                message to be displayed in dialog
     * @param positiveAction         positive action button name
     * @param onDialogActionListener OnDialogActionListener callback interface instance
     */
    public void showMessageConfirmDialog(final String title, final String message, String positiveAction, final OnDialogActionListener onDialogActionListener) {
        AppCompatActivity mAppCompatActivity = (AppCompatActivity) context;
        View mView = LayoutInflater.from(context).inflate(R.layout.common_message_dialog, null, false);
        final Dialog mDialog = new Dialog(mAppCompatActivity);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.setTitle(R.string.app_name);
        mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(mView);

        AppCompatTextView mAppCompatTextViewTitle = mDialog.findViewById(R.id.tvMessageDialogTitle);
        AppCompatTextView mAppCompatTextViewMessage = mDialog.findViewById(R.id.tvMessageDialogMessage);
        Button mAppCompatButtonPositive = mDialog.findViewById(R.id.btMessageDialogPositive);

        if (title != null) {
            mAppCompatTextViewTitle.setText(title);
        } else {
            mAppCompatTextViewTitle.setVisibility(View.GONE);
        }
        mAppCompatTextViewMessage.setText(message);

        if (positiveAction != null) {
            mAppCompatButtonPositive.setText(positiveAction);
            mAppCompatButtonPositive.setOnClickListener(view -> {
                mDialog.dismiss();
                if (onDialogActionListener != null) {
                    onDialogActionListener.onPositiveClick();
                }
            });
        }
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.copyFrom(mDialog.getWindow().getAttributes());
        DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();

        // Screen width will be divided by 0.92 to give left and right margin to the dialog view.

        mLayoutParams.width = (int) (mDisplayMetrics.widthPixels * 0.92f);

        //set the dim level of the background
        mLayoutParams.dimAmount = 0.5f; //change this value for more or less dimming

        mDialog.getWindow().setAttributes(mLayoutParams);
        mDialog.getWindow().setGravity(Gravity.CENTER);
        mDialog.setCancelable(false);
        if (!mAppCompatActivity.isFinishing()) {
            mDialog.show();
        }

    }

    /**
     * Interface to handle the confirm dialog listener
     */
    public interface OnDialogActionListener {
        /**
         * This method will be triggered on click of the positive action button.
         */
        void onPositiveClick();

    }
}
