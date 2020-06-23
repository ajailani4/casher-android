package com.example.casher.model.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoriesItem implements Parcelable {
    private String icon;
    private String title;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CategoriesItem()
    {
        
    }
    
    protected CategoriesItem(Parcel in) {
        icon = in.readString();
        title = in.readString();
    }

    public static final Creator<CategoriesItem> CREATOR = new Creator<CategoriesItem>() {
        @Override
        public CategoriesItem createFromParcel(Parcel in) {
            return new CategoriesItem(in);
        }

        @Override
        public CategoriesItem[] newArray(int size) {
            return new CategoriesItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(icon);
        dest.writeString(title);
    }
}
