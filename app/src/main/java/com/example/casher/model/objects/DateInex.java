package com.example.casher.model.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class DateInex implements Parcelable {
    private String date;
    private double incomeTotal;
    private double expensesTotal;

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getDate()
    {
        return date;
    }

    public void setIncomeTotal(double incomeTotal)
    {
        this.incomeTotal = incomeTotal;
    }

    public double getIncomeTotal()
    {
        return incomeTotal;
    }

    public void setExpensesTotal(double expensesTotal)
    {
        this.expensesTotal = expensesTotal;
    }

    public double getExpensesTotal()
    {
        return expensesTotal;
    }

    public DateInex() {

    }

    protected DateInex(Parcel in) {
        date = in.readString();
        incomeTotal = in.readInt();
        expensesTotal = in.readInt();
    }

    public static final Creator<DateInex> CREATOR = new Creator<DateInex>() {
        @Override
        public DateInex createFromParcel(Parcel in) {
            return new DateInex(in);
        }

        @Override
        public DateInex[] newArray(int size) {
            return new DateInex[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeDouble(incomeTotal);
        dest.writeDouble(expensesTotal);
    }
}
