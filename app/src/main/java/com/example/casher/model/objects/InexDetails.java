package com.example.casher.model.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class InexDetails implements Parcelable {
    String id;
    String inex;
    String dateName;
    String categories;
    String notes;
    double total;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInex() {
        return inex;
    }

    public void setInex(String inex) {
        this.inex = inex;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public InexDetails()
    {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(inex);
        dest.writeString(dateName);
        dest.writeString(categories);
        dest.writeString(notes);
        dest.writeDouble(total);
    }

    protected InexDetails(Parcel in) {
        id = in.readString();
        inex = in.readString();
        dateName = in.readString();
        categories = in.readString();
        notes = in.readString();
        total = in.readDouble();
    }

    public static final Creator<InexDetails> CREATOR = new Creator<InexDetails>() {
        @Override
        public InexDetails createFromParcel(Parcel in) {
            return new InexDetails(in);
        }

        @Override
        public InexDetails[] newArray(int size) {
            return new InexDetails[size];
        }
    };
}
