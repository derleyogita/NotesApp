package com.example.notes.database;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author yogitad
 * @since 16-08-2022
 * This model will hold user added notes details
 **/

public class AddNotesResponseModel extends RealmObject implements Parcelable {
    public static final Parcelable.Creator<AddNotesResponseModel> CREATOR = new Parcelable.Creator<AddNotesResponseModel>() {
        @Override
        public AddNotesResponseModel createFromParcel(Parcel in) {
            return new AddNotesResponseModel(in);
        }

        @Override
        public AddNotesResponseModel[] newArray(int size) {
            return new AddNotesResponseModel[size];
        }
    };
    @PrimaryKey
    public String noteName;
    public String noteDescription;
    public String selectedDateTime;
    public String capturedImage;

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }

    public void setSelectedDateTime(String selectedDateTime) {
        this.selectedDateTime = selectedDateTime;
    }

    public void setCapturedImage(String capturedImage) {
        this.capturedImage = capturedImage;
    }

    public String getCapturedImage() {
        return capturedImage;
    }

    public String getSelectedDateTime() {
        return selectedDateTime;
    }

    public AddNotesResponseModel() {
    }

    private AddNotesResponseModel(Parcel in) {
        noteName = in.readString();
        noteDescription = in.readString();
        selectedDateTime = in.readString();
        capturedImage = in.readString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(noteName);
        dest.writeString(noteDescription);
        dest.writeString(selectedDateTime);
        dest.writeString(capturedImage);
    }

    public int describeContents() {
        return 0;
    }
}
