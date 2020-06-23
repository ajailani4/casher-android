package com.example.casher.model.repository;

import android.database.Cursor;

import com.example.casher.model.objects.DateInex;
import com.example.casher.model.objects.InexDetails;
import com.example.casher.model.objects.InexStatistics;
import com.example.casher.model.objects.MonthInex;

import java.util.ArrayList;

public class DataRepository {

    AppDataSource dataSource;
    String monthName;
    String dateName;
    InexDetails inexDetails;

    //Casual Constructor
    public DataRepository(AppDataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    //Constructor for Month
    public DataRepository(AppDataSource dataSource, String monthName)
    {
        this.dataSource = dataSource;
        this.monthName = monthName;
    }

    //Constructor for Date
    public DataRepository(AppDataSource dataSource, String monthName, String dateName)
    {
        this.dataSource = dataSource;
        this.monthName = monthName;
        this.dateName = dateName;
    }

    //Constructor for Details of InEx
    public DataRepository(AppDataSource dataSource, InexDetails inexDetails)
    {
        this.dataSource = dataSource;
        this.inexDetails = inexDetails;
    }

    //Month
    public void insertMonth()
    {
        dataSource.insertMonth(monthName);
    }

    public MonthInex getMonth()
    {
        return dataSource.getMonth(monthName);
    }

    //Date
    public void insertDate()
    {
        dataSource.insertDate(dateName);
    }

    public ArrayList<DateInex> getAllDate()
    {
        return dataSource.getAllDate(monthName);
    }

    public void deleteDate()
    {
        dataSource.deleteDate(dateName);
    }

    //Details of InEx
    public void insertInDetails()
    {
        dataSource.insertInDetails(inexDetails);
    }

    public void insertExDetails()
    {
        dataSource.insertExDetails(inexDetails);
    }

    public ArrayList<InexDetails> getAllInexDetails()
    {
        return dataSource.getAllInexDetails();
    }

    public void editInexDetails()
    {
        dataSource.editInexDetails(inexDetails);
    }

    public void deleteInexDetails()
    {
        dataSource.deleteInexDetails(inexDetails.getId(), inexDetails.getInex());
    }

    //Inex Statistics
    public ArrayList<InexStatistics> getAllInexStats(String inexChoice)
    {
        return dataSource.getAllInexStats(monthName, inexChoice);
    }
}
