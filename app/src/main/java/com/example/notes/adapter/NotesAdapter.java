package com.example.notes.adapter;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.database.AddNotesResponseModel;
import com.example.notes.databinding.AdapterNotesBinding;
import com.example.notes.enums.IExtraArgs;
import com.example.notes.listener.AdapterResponseListener;

import java.util.List;

/**
 * @author yogitad
 * @since 16-08-2022
 **/

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    /**
     * Adapter response listener
     */
    private final AdapterResponseListener adapterResponseListener;

    /**
     * Assessment Details list
     */
    private List<AddNotesResponseModel> addNotesResponseModelList;

    public NotesAdapter(AdapterResponseListener adapterResponseListener) {
        this.adapterResponseListener = adapterResponseListener;
    }

    @NonNull
    @Override
    public NotesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterNotesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_notes, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.MyViewHolder holder, int position) {
        // method will be used at the actual time of implementation
        AddNotesResponseModel addNotesResponseModel = this.addNotesResponseModelList.get(position);
        holder.bind(addNotesResponseModel);
    }

    @Override
    public int getItemCount() {
        return addNotesResponseModelList == null ? 0 : addNotesResponseModelList.size();
    }

    public void setData(List<AddNotesResponseModel> addNotesResponseModels) {
        this.addNotesResponseModelList = addNotesResponseModels;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final AdapterNotesBinding binding;

        MyViewHolder(AdapterNotesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        void bind(AddNotesResponseModel addNotesResponseModel) {
            binding.executePendingBindings();

            //set data
            binding.tvNoteTitle.setText(addNotesResponseModel.noteName);
            binding.tvNoteDetails.setText(addNotesResponseModel.noteDescription);
            binding.tvDateTime.setText(addNotesResponseModel.selectedDateTime);
            binding.ivEvidenceImage.setImageURI(Uri.parse(addNotesResponseModel.capturedImage));
            //set click listener
            binding.rlNote.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == binding.rlNote) {
                //Pass assessment ID for api call
                Bundle bundle = new Bundle();
                bundle.putParcelable(IExtraArgs.ARG_NAVIGATE_TO_DASHBOARD_FRAGMENT.getShortValue(), addNotesResponseModelList.get(getAdapterPosition()));
                adapterResponseListener.onAdapterItemClicked(bundle);
            }
        }
    }
}
