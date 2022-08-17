package com.example.notes;

import static com.example.notes.constants.IPermissions.CAMERA;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.example.notes.constants.IConstants;
import com.example.notes.database.AddNotesResponseModel;
import com.example.notes.databinding.DialogNewReminderBinding;
import com.example.notes.helper.ImageHelper;
import com.example.notes.helper.PermissionHelper;
import com.example.notes.helper.PopupDialogView;
import com.example.notes.listener.PermissionListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;

/**
 * @author yogitad
 * @since 15-08-2022
 * Fragment used to add new note and update note
 **/

public class AddNoteDialogFragment extends DialogFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, PermissionListener {

    /**
     * camera intent request code
     */
    private static final int REQUEST_CAMERA = 100;

    /**
     * bind layout
     */
    private DialogNewReminderBinding binding;

    /**
     * to get context
     */
    private Context context;
    /**
     * Assessment evidence List
     */
    private final List<AddNotesResponseModel> addNotesResponseModelList = new ArrayList<>();

    /**
     * Realm DataBase
     */
    private Realm realm;
    /**
     * selected date related data
     */
    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;

    /**
     * Image file from u click via camera
     */
    private File imageFile;

    /**
     * Image file from u click via camera
     */
    private Uri imageUri;


    public AddNoteDialogFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Bind activity with its layout
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_new_reminder, container, false);
        setupViews();
        //get realm instance
        // initializing our Realm
        Realm.init(context);
        realm = Realm.getDefaultInstance();
        if (getArguments() != null) {
            //if not null means user clicked on not so now handle reset flow
            //Response model class
            AddNotesResponseModel addNotesResponseModel = getArguments().getParcelable("responseModel");
            binding.btnSave.setText(getString(R.string.btn_reset));
            setNoteDate(addNotesResponseModel);
        } else {
            binding.btnSave.setText(getString(R.string.btn_save));
        }

        return binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    /**
     * Method used for views setup and initialization
     */
    private void setupViews() {
        //click listeners
        binding.ivClose.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);
        binding.btnReset.setOnClickListener(this);
        binding.etDateTime.setOnClickListener(this);
        binding.btnSelectImage.setOnClickListener(this);
    }

    /**
     * Method used to set note data for reset flow
     */
    private void setNoteDate(AddNotesResponseModel addNotesResponseModel) {
        if (addNotesResponseModel != null) {
            binding.etReminderName.setText(addNotesResponseModel.noteName);
            binding.etReminderDescription.setText(addNotesResponseModel.noteDescription);
            binding.etDateTime.setText(addNotesResponseModel.selectedDateTime);
            if(!addNotesResponseModel.capturedImage.isEmpty())
            binding.ivEvidenceImage.setImageURI(Uri.parse(addNotesResponseModel.capturedImage));
        }
    }

    /**
     * Method used to show case date picker
     */
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, this, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_close) {
            //dismiss/close dialog
            dismiss();
        } else if (view.getId() == R.id.btnSave) {
            if (!binding.etReminderName.getText().toString().isEmpty() && !binding.etReminderDescription.getText().toString().isEmpty()) {
                //handle button save
                AddNotesResponseModel responseModel = new AddNotesResponseModel();
                responseModel.setNoteName(binding.etReminderName.getText().toString());
                responseModel.setNoteDescription(binding.etReminderDescription.getText().toString());
                if(!String.valueOf(imageUri).isEmpty()){
                    responseModel.setCapturedImage(String.valueOf(imageUri));
                }
                //date time is optional
                responseModel.setSelectedDateTime(binding.etDateTime.getText().toString());
                //add in list
                addNotesResponseModelList.add(responseModel);
                saveNote();
            } else {
                Toast.makeText(context, R.string.str_enter_details, Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.btnReset) {
            //handle button reset
            //todo No need of reset button as in realm we have insertOrUpdate method which handles insertion n updation based on primary key
        } else if (view.getId() == R.id.etDateTime) {
            showDatePicker();
        } else if (view.getId() == R.id.btnSelectImage) {
            //Toast.makeText(context, getString(R.string.str_coming_soon), Toast.LENGTH_SHORT).show();
            if (PermissionHelper.getInstance().checkPermission(AddNoteDialogFragment.this, Manifest.permission.CAMERA, CAMERA, AddNoteDialogFragment.this)) {
                cameraIntent();
            }
        }

    }

    /**
     * Method used to save new note
     */
    private void saveNote() {
        realm.beginTransaction();
        realm.executeTransactionAsync(realm -> realm.insertOrUpdate(addNotesResponseModelList), () -> {
            dismiss();
            new PopupDialogView(context).showMessageConfirmDialog(getString(R.string.app_name), getString(R.string.str_reminder_Set), getString(R.string.str_ok), new PopupDialogView.OnDialogActionListener() {
                /**
                 * This method will be triggered on click of the positive action button.
                 */
                @Override
                public void onPositiveClick() {
                    //give a callback
                    OnDialogActionListener listener = (OnDialogActionListener) getTargetFragment();
                    if (listener != null) {
                        listener.onDialogDismiss();
                        dismiss();
                    }
                }

            });
        }, error -> Toast.makeText(context, getString(R.string.str_something_went_wrong), Toast.LENGTH_SHORT).show());
        realm.commitTransaction();
    }

    /**
     * Intent to open camera to take picture
     */
    private void cameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        // Create the File where the photo should go
        try {
            imageFile = ImageHelper.getInstance().createImageFile(context);
        } catch (IOException ex) {
            // Error occurred while creating the File
            ex.printStackTrace();
        }
        // Continue only if the File was successfully created
        if (imageFile != null) {
            Uri photoURI = FileProvider.getUriForFile(context,
                    "com.example.notes.fileprovider",
                    imageFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_CAMERA);

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        selectedYear = year;
        selectedMonth = month;
        selectedDay = day;
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, this, hour, minute, DateFormat.is24HourFormat(context));
        timePickerDialog.show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        //format yyyy-MM-dd 'T' HH:mm:ss
        binding.etDateTime.setText(selectedYear + "-" + selectedMonth + "-" + selectedDay + " 'T' " + hourOfDay + ":" + minute);
    }

    @Override
    public void isPermissionDenied(boolean isDenied) {
        //Handle permission deny flow
    }

    /**
     * Handle runtime permission callback
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new Handler(Looper.myLooper()).postDelayed(this::cameraIntent, 100);
            } else {
                Toast.makeText(context, R.string.str_access_denied, Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * OnActivityResult Handle Image intent callback
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                try {
                    imageUri = ImageHelper.getInstance().getUriFromFile(context, imageFile);
                    binding.ivEvidenceImage.setImageURI(Uri.parse(String.valueOf(imageUri)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Interface to handle the confirm dialog listener
     */
    public interface OnDialogActionListener {
        /**
         * This method will be triggered when dialog dismiss
         */
        void onDialogDismiss();
    }
}
