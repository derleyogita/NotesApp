package com.example.notes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.notes.adapter.NotesAdapter;
import com.example.notes.base.BaseFragment;
import com.example.notes.database.AddNotesResponseModel;
import com.example.notes.databinding.FragmentDashboardBinding;
import com.example.notes.enums.IExtraArgs;
import com.example.notes.listener.AdapterResponseListener;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author yogitad
 * @since 15-08-2022
 * Dashboard fragment which showcase added notes and action button to add n reset notes.
 **/

public class DashboardFragment extends BaseFragment implements AdapterResponseListener, View.OnClickListener, AddNoteDialogFragment.OnDialogActionListener {

    /**
     * Layout binding
     */
    private FragmentDashboardBinding binding;

    /**
     * App context
     */
    private Context context;

    /**
     * Response model class
     */
    private AddNotesResponseModel addNotesResponseModel;

    /**
     * Method used to check whether FAB button clicked or user clicked on list item
     */
    private boolean isFABBtnClicked;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Bind activity with its layout
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);  //call adapter which show case assessment details
        binding.rvNotes.setLayoutManager(new LinearLayoutManager(getActivity()));
        //show notes data
        fetchAndShowData();
        //Set UI elements
        setupViews();
        return binding.getRoot();
    }

    /**
     * Method used for views setup and initialization
     */
    private void setupViews() {
        //click listeners
        binding.faButton.setOnClickListener(this);
    }

    /**
     * Method used to fetch notes data from local storage and showcase on UI
     */
    private void fetchAndShowData() {
        //as there is not API call so for now showing progress dialog here
        showProgress();
        //Assessment adapter
        NotesAdapter mAdapter = new NotesAdapter(this);
        binding.rvNotes.setAdapter(mAdapter);
        binding.rvNotes.setItemAnimator(new DefaultItemAnimator());

        //fetch data from local database
        Realm.init(context);
        //Realm DataBase
        Realm realm = Realm.getDefaultInstance();
        //fetch results
        RealmResults<AddNotesResponseModel> results = realm.where(AddNotesResponseModel.class).findAll();
        //assessment status list
        List<AddNotesResponseModel> addNotesResponseModels = realm.copyFromRealm(results);
        //bind list to adapter
        if (addNotesResponseModels != null && !addNotesResponseModels.isEmpty()) {
            binding.tvNoNotes.setVisibility(View.GONE);
            binding.rvNotes.setVisibility(View.VISIBLE);
            mAdapter.setData(addNotesResponseModels);
            hideProgress();
        } else {
            hideProgress();
            binding.rvNotes.setVisibility(View.GONE);
            binding.tvNoNotes.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAdapterItemClicked(Bundle bundle) {
        //handle adapter click to reset note
        if (bundle != null) {
            addNotesResponseModel = bundle.getParcelable(IExtraArgs.ARG_NAVIGATE_TO_DASHBOARD_FRAGMENT.getShortValue());
            isFABBtnClicked = false;
            showDialog();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.faButton) {
            isFABBtnClicked = true;
            showDialog();
        }
    }

    /**
     * Method used to show signature panel
     */
    void showDialog() {
        FragmentManager fm = getFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putParcelable("responseModel", addNotesResponseModel);
        DialogFragment dialogFragment = new AddNoteDialogFragment();
        if(!isFABBtnClicked) {
            dialogFragment.setArguments(bundle);
        }
        dialogFragment.setTargetFragment(this, 1);
        if (fm != null) {
            dialogFragment.show(fm, "AddNote");
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }


    @Override
    public void onDialogDismiss() {
        //show notes data
        fetchAndShowData();
    }
}
