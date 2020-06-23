package com.example.casher.model.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class MonthInex implements Parcelable {
    int balanceMonth;
    int totalIncomeMonth;
    int totalExpensesMonth;

    public MonthInex()
    {

    }

    public int getBalanceMonth() {
        return balanceMonth;
    }

    public void setBalanceMonth(int balanceMonth) {
        this.balanceMonth = balanceMonth;
    }

    public int getTotalIncomeMonth() {
        return totalIncomeMonth;
    }

    public void setTotalIncomeMonth(int totalIncomeMonth) {
        this.totalIncomeMonth = totalIncomeMonth;
    }

    public int getTotalExpensesMonth() {
        return totalExpensesMonth;
    }

    public void setTotalExpensesMonth(int totalExpensesMonth) {
        this.totalExpensesMonth = totalExpensesMonth;
    }

    protected MonthInex(Parcel in) {
    }

    public static final Creator<MonthInex> CREATOR = new Creator<MonthInex>() {
        @Override
        public MonthInex createFromParcel(Parcel in) {
            return new MonthInex(in);
        }

        @Override
        public MonthInex[] newArray(int size) {
            return new MonthInex[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
