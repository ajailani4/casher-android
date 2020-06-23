package com.example.casher.model.repository;

import android.database.Cursor;

import com.example.casher.model.objects.DateInex;
import com.example.casher.model.objects.InexDetails;
import com.example.casher.model.objects.InexStatistics;
import com.example.casher.model.objects.MonthInex;

import java.util.ArrayList;

public interface AppDataSource {
    //Month
    void insertMonth(String monthName);
    MonthInex getMonth(String monthName);
    Cursor readAllMonth();
    int getTotalIncomeDate(String monthName);
    int getTotalExpensesDate(String monthName);

    //Date
    void insertDate(String dateName);
    ArrayList<DateInex> getAllDate(String monthName);
    Cursor readAllDate();
    int readTotalIncome(String dateName);
    int readTotalExpenses(String dateName);
    void deleteDate(String dateName);

    //Details of InEx
    void insertInDetails(InexDetails inexDetails);
    void insertExDetails(InexDetails inexDetails);
    ArrayList<InexDetails> getAllInexDetails();
    ArrayList<InexDetails> getAllInDetails();
    ArrayList<InexDetails> getAllExDetails();
    void editInexDetails(InexDetails inexDetails);
    void deleteInexDetails(String id, String inexChoice);

    //InEx Statistics
    ArrayList<InexStatistics> getAllInexStats(String monthName, String inexChoice);
    int getInexTotal(String monthName, String inexChoice);
    ArrayList<String> getInexCategories(String monthName, String inexChoice);
    ArrayList<Double> getInexPercentage(String monthName, String inexChoice);
    ArrayList<Integer> getInexColor(String monthName, String inexChoice);
    ArrayList<Integer> getInexCatTotal(String monthName, String inexChoice);
}
