package com.example.casher.model.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class InexStatistics implements Parcelable {
    String monthName;
    String categories;
    double percentage;
    int color;
    int totalCat;
    int totalInex;
    int totalIncome;
    int totalExpenses;

    public int getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(int totalIncome) {
        this.totalIncome = totalIncome;
    }

    public int getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(int totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public int getTotalCat() {
        return totalCat;
    }

    public void setTotalCat(int totalCat) {
        this.totalCat = totalCat;
    }

    public int getTotalInex() {
        return totalInex;
    }

    public void setTotalInex(int totalInex) {
        this.totalInex = totalInex;
    }

    public InexStatistics()
    {

    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    protected InexStatistics(Parcel in) {
        monthName = in.readString();
        categories = in.readString();
        percentage = in.readDouble();
        color = in.readInt();
        totalCat = in.readInt();
        totalInex = in.readInt();
        totalIncome = in.readInt();
        totalExpenses = in.readInt();
    }

    public static final Creator<InexStatistics> CREATOR = new Creator<InexStatistics>() {
        @Override
        public InexStatistics createFromParcel(Parcel in) {
            return new InexStatistics(in);
        }

        @Override
        public InexStatistics[] newArray(int size) {
            return new InexStatistics[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(monthName);
        dest.writeString(categories);
        dest.writeDouble(percentage);
        dest.writeInt(color);
        dest.writeInt(totalCat);
        dest.writeInt(totalInex);
        dest.writeInt(totalIncome);
        dest.writeInt(totalExpenses);
    }
}
