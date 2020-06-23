package com.example.casher.model.repository;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.casher.R;
import com.example.casher.model.database.DatabaseHelper;
import com.example.casher.model.database.DatabaseSettings;
import com.example.casher.model.objects.DateInex;
import com.example.casher.model.objects.InexDetails;
import com.example.casher.model.objects.InexStatistics;
import com.example.casher.model.objects.MonthInex;
import com.example.casher.viewmodel.MonthViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SqliteDataSource implements AppDataSource {
    private DatabaseHelper dbHelper;
    private Context mContext;

    public SqliteDataSource(Context context)
    {
        mContext = context;
        dbHelper = new DatabaseHelper(mContext);
    }

    //Month
    @Override
    public void insertMonth(String monthName) {
        Log.d("Month", monthName + " added");

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cur = readAllMonth();
        boolean res = false;
        boolean isMonthEqualsTemp = false;

        String tempDate = "";
        int inMonth = getTotalIncomeDate(monthName);
        int exMonth = getTotalExpensesDate(monthName);
        int balanceMonth = inMonth - exMonth;
        Log.d("month", monthName);
        Log.d("total income month", Integer.toString(inMonth));
        Log.d("total expenses month", Integer.toString(exMonth));
        Log.d("total balance month", Integer.toString(balanceMonth));

        if(cur.getCount() > 0)
        {
            while(cur.moveToNext())
            {
                tempDate = cur.getString(cur.getColumnIndex(DatabaseSettings.MonthEntry.COL_MONTH_NAME));

                if(tempDate.equals(monthName))
                {
                    Log.d("Month", "updating...");
                    isMonthEqualsTemp = true;
                    break;
                } else
                {
                    Log.d("Month", "inserting...");
                    isMonthEqualsTemp = false;
                }
            }

            if(isMonthEqualsTemp)
            {
                values.put(DatabaseSettings.MonthEntry.COL_MONTH_INCOME, inMonth);
                values.put(DatabaseSettings.MonthEntry.COL_MONTH_EXPENSES, exMonth);
                values.put(DatabaseSettings.MonthEntry.COL_MONTH_BALANCE, balanceMonth);

                int result = db.update(DatabaseSettings.MonthEntry.TABLE_NAME,
                        values,
                        DatabaseSettings.MonthEntry.COL_MONTH_NAME + "=?",
                        new String[]{monthName});
            } else
            {
                values.put(DatabaseSettings.MonthEntry.COL_MONTH_NAME, monthName);
                values.put(DatabaseSettings.MonthEntry.COL_MONTH_INCOME, inMonth);
                values.put(DatabaseSettings.MonthEntry.COL_MONTH_EXPENSES, exMonth);
                values.put(DatabaseSettings.MonthEntry.COL_MONTH_BALANCE, balanceMonth);

                long id = db.insertWithOnConflict(DatabaseSettings.MonthEntry.TABLE_NAME,
                        null,
                        values,
                        SQLiteDatabase.CONFLICT_REPLACE);
            }
        } else
        {
            values.put(DatabaseSettings.MonthEntry.COL_MONTH_NAME, monthName);
            values.put(DatabaseSettings.MonthEntry.COL_MONTH_INCOME, inMonth);
            values.put(DatabaseSettings.MonthEntry.COL_MONTH_EXPENSES, exMonth);
            values.put(DatabaseSettings.MonthEntry.COL_MONTH_BALANCE, balanceMonth);

            long id = db.insertWithOnConflict(DatabaseSettings.MonthEntry.TABLE_NAME,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    @Override
    public MonthInex getMonth(String monthName) {
        Cursor cur = readAllMonth();
        boolean isMonthExist = false;

        int totalIncomeMonth = 0;
        int totalExpensesMonth = 0;
        int balanceMonth = 0;

        if(cur.getCount() > 0)
        {
            while(cur.moveToNext())
            {
                String tempMonth = cur.getString(cur.getColumnIndex(DatabaseSettings.MonthEntry.COL_MONTH_NAME));

                if(tempMonth.equals(monthName))
                {
                    isMonthExist = true;
                    totalIncomeMonth = cur.getInt(cur.getColumnIndex(DatabaseSettings.MonthEntry.COL_MONTH_INCOME));
                    totalExpensesMonth = cur.getInt(cur.getColumnIndex(DatabaseSettings.MonthEntry.COL_MONTH_EXPENSES));
                    balanceMonth = cur.getInt(cur.getColumnIndex(DatabaseSettings.MonthEntry.COL_MONTH_BALANCE));
                }
            }

            if(!isMonthExist)
            {
                totalIncomeMonth = 0;
                totalExpensesMonth = 0;
                balanceMonth = 0;
            }
        } else
        {
            totalIncomeMonth = 0;
            totalExpensesMonth = 0;
            balanceMonth = 0;
        }

        MonthInex monthInex = new MonthInex();
        monthInex.setTotalIncomeMonth(totalIncomeMonth);
        monthInex.setTotalExpensesMonth(totalExpensesMonth);
        monthInex.setBalanceMonth(balanceMonth);

        return monthInex;
    }

    @Override
    public Cursor readAllMonth() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + DatabaseSettings.MonthEntry.TABLE_NAME, null);
        return cur;
    }

    @Override
    public int getTotalIncomeDate(String monthName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + DatabaseSettings.DateEntry.TABLE_NAME,
                null);
        int totalIncomeDate = 0;
        SimpleDateFormat inSdf = new SimpleDateFormat("dd-MM-yyyy");

        while(cur.moveToNext())
        {
            try
            {
                String dateName = cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsInEntry.COL_DATE_NAME));
                Date dateForMonth = inSdf.parse(dateName);
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateForMonth);
                int month = cal.get(Calendar.MONTH);
                String tempMonthName = MonthViewModel.monthArray[month];

                if(monthName.equals(tempMonthName))
                {
                    totalIncomeDate += cur.getInt(cur.getColumnIndex(DatabaseSettings.DateEntry.COL_DATE_INCOME));
                }
            } catch(Exception ex)
            {

            }
        }

        return totalIncomeDate;
    }

    @Override
    public int getTotalExpensesDate(String monthName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + DatabaseSettings.DateEntry.TABLE_NAME,
                null);
        int totalExpensesDate = 0;
        SimpleDateFormat inSdf = new SimpleDateFormat("dd-MM-yyyy");

        while(cur.moveToNext())
        {
            try
            {
                String dateName = cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsInEntry.COL_DATE_NAME));
                Date dateForMonth = inSdf.parse(dateName);
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateForMonth);
                int month = cal.get(Calendar.MONTH);
                String tempMonthName = MonthViewModel.monthArray[month];

                if(monthName.equals(tempMonthName))
                {
                    totalExpensesDate += cur.getInt(cur.getColumnIndex(DatabaseSettings.DateEntry.COL_DATE_EXPENSES));
                }
            } catch(Exception ex)
            {

            }
        }

        return totalExpensesDate;
    }

    //Date
    @Override
    public void insertDate(String dateName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cur = readAllDate();
        boolean isDateEqualsTemp = false;

        String tempDate = "";
        int inDate = readTotalIncome(dateName);
        int exDate = readTotalExpenses(dateName);
        Log.d("date", dateName);
        Log.d("total income date", Integer.toString(inDate));
        Log.d("total expenses date", Integer.toString(exDate));

        if(cur.getCount() > 0)
        {
            while(cur.moveToNext())
            {
                tempDate = cur.getString(cur.getColumnIndex(DatabaseSettings.DateEntry.COL_DATE_NAME));

                if(tempDate.equals(dateName))
                {
                    Log.d("date", "updating...");
                    isDateEqualsTemp = true;
                    break;
                } else
                {
                    Log.d("date", "inserting...");
                    isDateEqualsTemp = false;
                }
            }

            if(isDateEqualsTemp)
            {
                values.put(DatabaseSettings.DateEntry.COL_DATE_INCOME, inDate);
                values.put(DatabaseSettings.DateEntry.COL_DATE_EXPENSES, exDate);

                int result = db.update(DatabaseSettings.DateEntry.TABLE_NAME,
                        values,
                        DatabaseSettings.DateEntry.COL_DATE_NAME + "=?",
                        new String[]{dateName});
            } else
            {
                values.put(DatabaseSettings.DateEntry.COL_DATE_NAME, dateName);
                values.put(DatabaseSettings.DateEntry.COL_DATE_INCOME, inDate);
                values.put(DatabaseSettings.DateEntry.COL_DATE_EXPENSES, exDate);

                long id = db.insertWithOnConflict(DatabaseSettings.DateEntry.TABLE_NAME,
                        null,
                        values,
                        SQLiteDatabase.CONFLICT_REPLACE);
            }
        } else
        {
            values.put(DatabaseSettings.DateEntry.COL_DATE_NAME, dateName);
            values.put(DatabaseSettings.DateEntry.COL_DATE_INCOME, inDate);
            values.put(DatabaseSettings.DateEntry.COL_DATE_EXPENSES, exDate);

            long id = db.insertWithOnConflict(DatabaseSettings.DateEntry.TABLE_NAME,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    @Override
    public ArrayList<DateInex> getAllDate(String monthName) {
        ArrayList<DateInex> dateInexList = new ArrayList<>();
        SimpleDateFormat inSdf = new SimpleDateFormat("dd-MM-yyyy");
        Cursor cur = readAllDate();
        String dateName;
        int dateTotalIncome;
        int dateTotalExpenses;

        if(cur.getCount() > 0)
        {
            while(cur.moveToNext())
            {
                try
                {
                    //Get month from date column and compare it based on user input
                    dateName = cur.getString(cur.getColumnIndex(DatabaseSettings.DateEntry.COL_DATE_NAME));
                    Date dateForMonth = inSdf.parse(dateName);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateForMonth);
                    int month = cal.get(Calendar.MONTH);
                    String tempMonthName = MonthViewModel.monthArray[month];

                    if(monthName.equals(tempMonthName))
                    {
                        DateInex dateInex = new DateInex();

                        dateName = cur.getString(cur.getColumnIndex(DatabaseSettings.DateEntry.COL_DATE_NAME));
                        dateTotalIncome = cur.getInt(cur.getColumnIndex(DatabaseSettings.DateEntry.COL_DATE_INCOME));
                        dateTotalExpenses = cur.getInt(cur.getColumnIndex(DatabaseSettings.DateEntry.COL_DATE_EXPENSES));

                        dateInex.setDate(dateName);
                        dateInex.setIncomeTotal(dateTotalIncome);
                        dateInex.setExpensesTotal(dateTotalExpenses);

                        dateInexList.add(dateInex);

                        Log.d("date name", dateName);
                        Log.d("date income", Integer.toString(dateTotalIncome));
                        Log.d("date expenses", Integer.toString(dateTotalExpenses));
                    }
                } catch(Exception ex)
                {
                    Log.d("Error get date", ex.getMessage());
                }
            }
        }

        return dateInexList;
    }

    @Override
    public Cursor readAllDate() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM  " + DatabaseSettings.DateEntry.TABLE_NAME, null);
        return cur;
    }

    @Override
    public int readTotalIncome(String dateName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + DatabaseSettings.DetailsInEntry.TABLE_NAME,
                null);
        int totalIncome = 0;

        while(cur.moveToNext())
        {
            String tempDate = cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsInEntry.COL_DATE_NAME));

            if(tempDate.equals(dateName))
            {
                totalIncome += cur.getInt(cur.getColumnIndex(DatabaseSettings.DetailsInEntry.COL_IN_TOTAL));
            }
        }

        return totalIncome;
    }

    @Override
    public int readTotalExpenses(String dateName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + DatabaseSettings.DetailsExEntry.TABLE_NAME,
                null);
        int totalIncome = 0;

        while(res.moveToNext())
        {
            String tempDate = res.getString(res.getColumnIndex(DatabaseSettings.DetailsExEntry.COL_DATE_NAME));

            if(tempDate.equals(dateName))
            {
                totalIncome += res.getInt(res.getColumnIndex(DatabaseSettings.DetailsExEntry.COL_EX_TOTAL));
            }
        }

        return totalIncome;
    }

    @Override
    public void deleteDate(String dateName)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = readAllDate();

        if(cur.getCount() > 0)
        {
            while(cur.moveToNext())
            {
                String tempDate = cur.getString(cur.getColumnIndex(DatabaseSettings.DateEntry.COL_DATE_NAME));

                if(tempDate.equals(dateName))
                {
                    int totalIncome = cur.getInt(cur.getColumnIndex(DatabaseSettings.DateEntry.COL_DATE_INCOME));
                    int totalExpenses = cur.getInt(cur.getColumnIndex(DatabaseSettings.DateEntry.COL_DATE_EXPENSES));

                    if(totalIncome == 0 && totalExpenses == 0)
                    {
                        Log.d("Delete date", dateName);

                        db.delete(DatabaseSettings.DateEntry.TABLE_NAME, "date_name=?", new String[]{dateName});
                    }
                }
            }
        }
    }

    //Details of InEx
    @Override
    public void insertInDetails(InexDetails inexDetails) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseSettings.DetailsInEntry.COL_DATE_NAME, inexDetails.getDateName());
        values.put(DatabaseSettings.DetailsInEntry.COL_IN_CATEGORIES, inexDetails.getCategories());
        values.put(DatabaseSettings.DetailsInEntry.COL_IN_TOTAL, (int) inexDetails.getTotal());
        values.put(DatabaseSettings.DetailsInEntry.COL_IN_NOTES, inexDetails.getNotes());

        long id = db.insertWithOnConflict(
                DatabaseSettings.DetailsInEntry.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    @Override
    public void insertExDetails(InexDetails inexDetails) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseSettings.DetailsExEntry.COL_DATE_NAME, inexDetails.getDateName());
        values.put(DatabaseSettings.DetailsExEntry.COL_EX_CATEGORIES, inexDetails.getCategories());
        values.put(DatabaseSettings.DetailsExEntry.COL_EX_TOTAL, (int) inexDetails.getTotal());
        values.put(DatabaseSettings.DetailsExEntry.COL_EX_NOTES, inexDetails.getNotes());

        long id = db.insertWithOnConflict(
                DatabaseSettings.DetailsExEntry.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    @Override
    public ArrayList<InexDetails> getAllInexDetails() {
        ArrayList<InexDetails> inexDetailsList = new ArrayList<>();
        inexDetailsList.addAll(getAllInDetails());
        inexDetailsList.addAll(getAllExDetails());

        return inexDetailsList;
    }

    @Override
    public ArrayList<InexDetails> getAllInDetails() {
        ArrayList<InexDetails> inexDetailsList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + DatabaseSettings.DetailsInEntry.TABLE_NAME, null);
        String id;
        String dateName;
        String inCategories;
        int inTotal;
        String inNotes;

        if(cur.getCount() > 0)
        {
            while(cur.moveToNext())
            {
                InexDetails inexDetails = new InexDetails();

                id = cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsInEntry._ID));
                dateName = cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsInEntry.COL_DATE_NAME));
                inCategories = cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsInEntry.COL_IN_CATEGORIES));
                inTotal = cur.getInt(cur.getColumnIndex(DatabaseSettings.DetailsInEntry.COL_IN_TOTAL));
                inNotes = cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsInEntry.COL_IN_NOTES));

                inexDetails.setId(id);
                inexDetails.setInex("Income");
                inexDetails.setDateName(dateName);
                inexDetails.setCategories(inCategories);
                inexDetails.setTotal(inTotal);
                inexDetails.setNotes(inNotes);

                inexDetailsList.add(inexDetails);
            }
        }

        return inexDetailsList;
    }

    @Override
    public ArrayList<InexDetails> getAllExDetails() {
        ArrayList<InexDetails> inexDetailsList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + DatabaseSettings.DetailsExEntry.TABLE_NAME, null);
        String id;
        String dateName;
        String exCategories;
        int exTotal;
        String exNotes;

        if(cur.getCount() > 0)
        {
            while(cur.moveToNext())
            {
                InexDetails inexDetails = new InexDetails();

                id = cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsExEntry._ID));
                dateName = cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsExEntry.COL_DATE_NAME));
                exCategories = cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsExEntry.COL_EX_CATEGORIES));
                exTotal = cur.getInt(cur.getColumnIndex(DatabaseSettings.DetailsExEntry.COL_EX_TOTAL));
                exNotes = cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsExEntry.COL_EX_NOTES));

                inexDetails.setId(id);
                inexDetails.setInex("Expenses");
                inexDetails.setDateName(dateName);
                inexDetails.setCategories(exCategories);
                inexDetails.setTotal(exTotal);
                inexDetails.setNotes(exNotes);

                inexDetailsList.add(inexDetails);
            }
        }

        return inexDetailsList;
    }

    @Override
    public void editInexDetails(InexDetails inexDetails)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        String tableName = "";

        Log.d("inex choice", inexDetails.getInex());
        Log.d("inex cat", inexDetails.getCategories());
        Log.d("inex total", String.valueOf(inexDetails.getTotal()));
        Log.d("inex notes", inexDetails.getNotes());

        if(inexDetails.getInex().equals("Income"))
        {
            tableName = DatabaseSettings.DetailsInEntry.TABLE_NAME;
            values.put(DatabaseSettings.DetailsInEntry.COL_IN_CATEGORIES, inexDetails.getCategories());
            values.put(DatabaseSettings.DetailsInEntry.COL_IN_TOTAL, inexDetails.getTotal());
            values.put(DatabaseSettings.DetailsInEntry.COL_IN_NOTES, inexDetails.getNotes());
        } else if(inexDetails.getInex().equals("Expenses"))
        {
            tableName = DatabaseSettings.DetailsExEntry.TABLE_NAME;
            values.put(DatabaseSettings.DetailsExEntry.COL_EX_CATEGORIES, inexDetails.getCategories());
            values.put(DatabaseSettings.DetailsExEntry.COL_EX_TOTAL, inexDetails.getTotal());
            values.put(DatabaseSettings.DetailsExEntry.COL_EX_NOTES, inexDetails.getNotes());
        }

        db.update(tableName, values, "_id=?", new String[]{inexDetails.getId()});
    }

    @Override
    public void deleteInexDetails(String id, String inexChoice)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Log.d("inexchoice delete", inexChoice);
        Log.d("inex id", id);

        if(inexChoice.equals("Income"))
        {
            db.delete(DatabaseSettings.DetailsInEntry.TABLE_NAME, "_id=?", new String[]{id});
        } else if(inexChoice.equals("Expenses"))
        {
            db.delete(DatabaseSettings.DetailsExEntry.TABLE_NAME, "_id=?", new String[]{id});
        }
    }

    //InEx Statistics
    @Override
    public ArrayList<InexStatistics> getAllInexStats(String monthName, String inexChoice) {
        ArrayList<InexStatistics> inexStatsList = new ArrayList<>();

        int totalInex = getInexTotal(monthName, inexChoice);
        int totalIncome = getTotalIncomeDate(monthName);
        int totalExpenses = getTotalExpensesDate(monthName);

        ArrayList<String> categories = getInexCategories(monthName, inexChoice);
        ArrayList<Double> categoriesPercentage = getInexPercentage(monthName, inexChoice);
        ArrayList<Integer> categoriesColor = getInexColor(monthName, inexChoice);
        ArrayList<Integer> categoriesTotal = getInexCatTotal(monthName, inexChoice);

        if(categories.size() > 0)
        {
            for(int i=0;i<categories.size();i++)
            {
                InexStatistics inexStats = new InexStatistics();
                inexStats.setMonthName(monthName);
                inexStats.setTotalInex(totalInex);
                inexStats.setTotalIncome(totalIncome);
                inexStats.setTotalExpenses(totalExpenses);
                inexStats.setCategories(categories.get(i));
                inexStats.setPercentage(categoriesPercentage.get(i));
                inexStats.setColor(categoriesColor.get(i));
                inexStats.setTotalCat(categoriesTotal.get(i));

                inexStatsList.add(inexStats);
            }
        } else
        {
            InexStatistics inexStats = new InexStatistics();
            inexStats.setTotalIncome(totalIncome);
            inexStats.setTotalExpenses(totalExpenses);

            inexStatsList.add(inexStats);
        }

        return inexStatsList;
    }

    @Override
    public int getInexTotal(String monthName, String inexChoice) {
        int inexTotal = 0;

        if(inexChoice.equals("Income"))
        {
            inexTotal = getTotalIncomeDate(monthName);
        } else if(inexChoice.equals("Expenses"))
        {
            inexTotal = getTotalExpensesDate(monthName);
        }

        return inexTotal;
    }

    @Override
    public ArrayList<String> getInexCategories(String monthName, String inexChoice) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur;
        SimpleDateFormat inSdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateName;
        ArrayList<String> categories = new ArrayList<>();
        ArrayList<String> resCategories = new ArrayList<>();

        if(inexChoice.equals("Income"))
        {
            cur = db.rawQuery("SELECT * FROM " + DatabaseSettings.DetailsInEntry.TABLE_NAME, null);

            if(cur.getCount() > 0)
            {
                while(cur.moveToNext())
                {
                    try
                    {
                        //Get month from date column and compare it based on user input
                        dateName = cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsInEntry.COL_DATE_NAME));
                        Date dateForMonth = inSdf.parse(dateName);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dateForMonth);
                        int month = cal.get(Calendar.MONTH);
                        String tempMonthName = MonthViewModel.monthArray[month];

                        if(monthName.equals(tempMonthName))
                        {
                            categories.add(cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsInEntry.COL_IN_CATEGORIES)));
                        }
                    } catch(Exception ex)
                    {

                    }
                }
            }
        } else if(inexChoice.equals("Expenses"))
        {
            cur = db.rawQuery("SELECT * FROM " + DatabaseSettings.DetailsExEntry.TABLE_NAME, null);

            if(cur.getCount() > 0)
            {
                while(cur.moveToNext())
                {
                    try
                    {
                        //Get month from date column and compare it based on user input
                        dateName = cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsExEntry.COL_DATE_NAME));
                        Date dateForMonth = inSdf.parse(dateName);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dateForMonth);
                        int month = cal.get(Calendar.MONTH);
                        String tempMonthName = MonthViewModel.monthArray[month];

                        if(monthName.equals(tempMonthName))
                        {
                            categories.add(cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsExEntry.COL_EX_CATEGORIES)));
                        }
                    } catch(Exception ex)
                    {

                    }
                }
            }
        }

        //Categorize kind of categories
        for(int i=0;i<categories.size();i++)
        {
            String tempCat = categories.get(i);

            if(resCategories != null)
            {
                if(isElementExist(tempCat, resCategories))
                {
                    continue;
                } else
                {
                    resCategories.add(tempCat);
                }
            } else
            {
                resCategories.add(tempCat);
            }
        }

        return resCategories;
    }

    @Override
    public ArrayList<Double> getInexPercentage(String monthName, String inexChoice) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur;
        SimpleDateFormat inSdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateName;
        ArrayList<String> categories = getInexCategories(monthName, inexChoice);
        ArrayList<Double> categoriesPercentage = new ArrayList<>();

        if(inexChoice.equals("Income"))
        {
            int incomeTotal = getInexTotal(monthName, inexChoice);

            for(int i=0;i<categories.size();i++)
            {
                int catIncomeTotal = 0;
                double catIncomePercentage;
                cur = db.rawQuery("SELECT * FROM " + DatabaseSettings.DetailsInEntry.TABLE_NAME, null);

                if(cur.getCount() > 0)
                {
                    Log.d("Stats total", "count > 0");

                    while(cur.moveToNext())
                    {
                        try
                        {
                            //Get month from date column and compare it based on user input
                            dateName = cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsExEntry.COL_DATE_NAME));
                            Date dateForMonth = inSdf.parse(dateName);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(dateForMonth);
                            int month = cal.get(Calendar.MONTH);
                            String tempMonthName = MonthViewModel.monthArray[month];
                            String tempCategories = cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsInEntry.COL_IN_CATEGORIES));

                            if(monthName.equals(tempMonthName))
                            {
                                if(categories.get(i).equals(tempCategories))
                                {
                                    catIncomeTotal += cur.getInt(cur.getColumnIndex(DatabaseSettings.DetailsInEntry.COL_IN_TOTAL));
                                }
                            }
                        } catch(Exception ex)
                        {

                        }
                    }
                }

                catIncomePercentage = (catIncomeTotal / (double) incomeTotal) * 100;
                categoriesPercentage.add(catIncomePercentage);
            }
        } else if(inexChoice.equals("Expenses"))
        {
            int expensesTotal = getInexTotal(monthName, inexChoice);

            for(int i=0;i<categories.size();i++)
            {
                int catExpensesTotal = 0;
                double catExpensesPercentage;
                cur = db.rawQuery("SELECT * FROM " + DatabaseSettings.DetailsExEntry.TABLE_NAME, null);

                if (cur.getCount() > 0)
                {
                    while (cur.moveToNext())
                    {
                        try
                        {
                            //Get month from date column and compare it based on user input
                            dateName = cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsExEntry.COL_DATE_NAME));
                            Date dateForMonth = inSdf.parse(dateName);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(dateForMonth);
                            int month = cal.get(Calendar.MONTH);
                            String tempMonthName = MonthViewModel.monthArray[month];
                            String tempCategories = cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsExEntry.COL_EX_CATEGORIES));

                            if(monthName.equals(tempMonthName))
                            {
                                if(categories.get(i).equals(tempCategories))
                                {
                                    catExpensesTotal += cur.getInt(cur.getColumnIndex(DatabaseSettings.DetailsExEntry.COL_EX_TOTAL));
                                }
                            }
                        } catch(Exception ex)
                        {

                        }
                    }
                }

                catExpensesPercentage = (catExpensesTotal / (double) expensesTotal) * 100;
                categoriesPercentage.add(catExpensesPercentage);
            }
        }

        return categoriesPercentage;
    }

    @Override
    public ArrayList<Integer> getInexColor(String monthName, String inexChoice) {
            int[] inexStatsColor = mContext.getResources().getIntArray(R.array.inex_stats_color);
        ArrayList<String> categories = getInexCategories(monthName, inexChoice);
        ArrayList<Integer> categoriesColor = new ArrayList<>();

        for(int i=0;i<categories.size();i++)
        {
            categoriesColor.add(inexStatsColor[i]);
        }

        return categoriesColor;
    }

    @Override
    public ArrayList<Integer> getInexCatTotal(String monthName, String inexChoice) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur;
        SimpleDateFormat inSdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateName;
        ArrayList<String> categories = getInexCategories(monthName, inexChoice);
        ArrayList<Integer> categoriesTotal = new ArrayList<>();

        if(inexChoice.equals("Income"))
        {
            for(int i=0;i<categories.size();i++)
            {
                int catIncomeTotal = 0;
                cur = db.rawQuery("SELECT * FROM " + DatabaseSettings.DetailsInEntry.TABLE_NAME, null);

                if(cur.getCount() > 0)
                {
                    Log.d("Stats total", "count > 0");

                    while(cur.moveToNext())
                    {
                        try
                        {
                            //Get month from date column and compare it based on user input
                            dateName = cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsExEntry.COL_DATE_NAME));
                            Date dateForMonth = inSdf.parse(dateName);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(dateForMonth);
                            int month = cal.get(Calendar.MONTH);
                            String tempMonthName = MonthViewModel.monthArray[month];
                            String tempCategories = cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsInEntry.COL_IN_CATEGORIES));

                            if(monthName.equals(tempMonthName))
                            {
                                if(categories.get(i).equals(tempCategories))
                                {
                                    catIncomeTotal += cur.getInt(cur.getColumnIndex(DatabaseSettings.DetailsInEntry.COL_IN_TOTAL));
                                }
                            }
                        } catch(Exception ex)
                        {

                        }
                    }
                }

                categoriesTotal.add(catIncomeTotal);
            }
        } else if(inexChoice.equals("Expenses"))
        {
            for(int i=0;i<categories.size();i++)
            {
                int catExpensesTotal = 0;
                cur = db.rawQuery("SELECT * FROM " + DatabaseSettings.DetailsExEntry.TABLE_NAME, null);

                if (cur.getCount() > 0)
                {
                    while (cur.moveToNext())
                    {
                        try
                        {
                            //Get month from date column and compare it based on user input
                            dateName = cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsExEntry.COL_DATE_NAME));
                            Date dateForMonth = inSdf.parse(dateName);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(dateForMonth);
                            int month = cal.get(Calendar.MONTH);
                            String tempMonthName = MonthViewModel.monthArray[month];
                            String tempCategories = cur.getString(cur.getColumnIndex(DatabaseSettings.DetailsExEntry.COL_EX_CATEGORIES));

                            if(monthName.equals(tempMonthName))
                            {
                                if(categories.get(i).equals(tempCategories))
                                {
                                    catExpensesTotal += cur.getInt(cur.getColumnIndex(DatabaseSettings.DetailsExEntry.COL_EX_TOTAL));
                                }
                            }
                        } catch(Exception ex)
                        {

                        }
                    }
                }

                categoriesTotal.add(catExpensesTotal);
            }
        }

        return categoriesTotal;
    }

    //Additional method
    //Check if element exist
    public boolean isElementExist(String temp, ArrayList<String> resCategories)
    {
        boolean res = false;

        for(int i=0;i<resCategories.size();i++)
        {
            if(temp.equals(resCategories.get(i)))
            {
                res = true;
                break;
            }
        }

        return res;
    }
}
