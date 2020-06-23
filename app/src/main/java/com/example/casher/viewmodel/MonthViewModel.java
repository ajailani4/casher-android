package com.example.casher.viewmodel;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.casher.model.database.DatabaseHelper;
import com.example.casher.model.database.DatabaseSettings;
import com.example.casher.model.objects.MonthInex;
import com.example.casher.viewmodel.worker.MonthWorker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class MonthViewModel extends AndroidViewModel {
    public DatabaseHelper dbHelper;

    private WorkManager monthWorkManager;
    private OneTimeWorkRequest workRequest;

    private MutableLiveData<MonthInex> monthInexLive = new MutableLiveData<>();
    private MutableLiveData<Boolean> isMonthInexInserted = new MutableLiveData<>();

    public static String[] monthArray = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

    public MonthViewModel(Application application)
    {
        super(application);
        dbHelper = new DatabaseHelper(application);
    }

    public MutableLiveData<MonthInex> getMonthInexLive()
    {
        return monthInexLive;
    }

    public MutableLiveData<Boolean> getMonthInexInserted()
    {
        return isMonthInexInserted;
    }

    //Insert month, month income, month expenses, month balance
    public void insertMonth(final String monthName)
    {
        Log.d("Month", monthName + " added");

        monthWorkManager = WorkManager.getInstance();
        Data.Builder monthData = new Data.Builder();
        monthData.putString("CRUD Action", "Insert");
        monthData.putString("Month Name", monthName);
        workRequest = new OneTimeWorkRequest.Builder(MonthWorker.class)
                .setInputData(monthData.build())
                .build();
        monthWorkManager.enqueue(workRequest);

        monthWorkManager.getWorkInfoByIdLiveData(workRequest.getId()).observeForever(new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if(workInfo != null && workInfo.getState().isFinished())
                {
                    isMonthInexInserted.postValue(true);
                }
            }
        });
    }

    //Get month based on user input
    public void getMonth(String monthName)
    {
        monthWorkManager = WorkManager.getInstance();
        Data.Builder monthData = new Data.Builder();
        monthData.putString("CRUD Action", "Read");
        monthData.putString("Month Name", monthName);
        workRequest = new OneTimeWorkRequest.Builder(MonthWorker.class)
                .setInputData(monthData.build())
                .build();
        monthWorkManager.enqueue(workRequest);

        monthWorkManager.getWorkInfoByIdLiveData(workRequest.getId()).observeForever(new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if(workInfo != null && workInfo.getState().isFinished())
                {
                    //Deserialize monthInexData
                    Gson gson = new Gson();
                    String tempData = workInfo.getOutputData().getString("Data");
                    Type monthInexType = new TypeToken<MonthInex>(){}.getType();
                    MonthInex monthInexData = gson.fromJson(tempData, monthInexType);
                    monthInexLive.postValue(monthInexData);
                }
            }
        });
    }

    //Get month name
    public String getMonthName(int month)
    {
        return monthArray[month];
    }

    //Temporary code!
    public void deleteAllData()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.delete(DatabaseSettings.MonthEntry.TABLE_NAME, null, null);
        db.execSQL("DELETE FROM " + DatabaseSettings.MonthEntry.TABLE_NAME);
    }
}
