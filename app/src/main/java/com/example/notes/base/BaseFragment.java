package com.example.notes.base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.notes.helper.UIHelper;
import com.example.notes.listener.AppProgressDialog;

/**
 * @author yogitad
 * @since 14-08-2022
 * Class represents common Base formation
 **/

public class BaseFragment extends Fragment implements AppProgressDialog {

    /**
     * Current Activity context
     */
    private Context context;


    @Override
    public void showProgress() {
        UIHelper.getInstance().showProgress(getActivity());
    }

    @Override
    public void hideProgress() {
        UIHelper.getInstance().hideProgress(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        UIHelper.getInstance().hideKeyboard(context);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }
}
