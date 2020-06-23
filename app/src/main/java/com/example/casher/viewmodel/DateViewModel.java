package com.example.casher.viewmodel;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.casher.model.database.DatabaseHelper;
import com.example.casher.model.objects.DateInex;
import com.example.casher.viewmodel.worker.DateWorker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DateViewModel extends AndroidViewModel {
    public DatabaseHelper dbHelper;

    private WorkManager dateWorkManager;
    private OneTimeWorkRequest workRequest;

    private MutableLiveData<ArrayList<DateInex>> dateInexLive = new MutableLiveData<>();
    private MutableLiveData<Boolean> isDateInexInserted = new MutableLiveData<>();
    private MutableLiveData<Boolean> isDateInexDeleted = new MutableLiveData<>();

    public DateViewModel(Application application)
    {
        super(application);
        dbHelper = new DatabaseHelper(application);
    }

    public MutableLiveData<ArrayList<DateInex>> getDateInexLive()
    {
        return dateInexLive;
    }

    public MutableLiveData<Boolean> getDateInexInserted()
    {
        return isDateInexInserted;
    }

    public MutableLiveData<Boolean> getDateInexDeleted() {
        return isDateInexDeleted;
    }

    //Insert date, date income, date expenses
    public void insertDate(String dateName)
    {
        dateWorkManager = WorkManager.getInstance();
        Data.Builder dateData = new Data.Builder();
        dateData.putString("CRUD Action", "Insert");
        dateData.putString("Date Name", dateName);
        workRequest = new OneTimeWorkRequest.Builder(DateWorker.class)
                .setInputData(dateData.build())
                .build();
        dateWorkManager.enqueue(workRequest);

        dateWorkManager.getWorkInfoByIdLiveData(workRequest.getId()).observeForever(new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if(workInfo != null && workInfo.getState().isFinished())
                {
                    isDateInexInserted.postValue(true);
                }
            }
        });
    }

    //Get all date based on month
    public void getAllDate(String monthName)
    {
        dateWorkManager = WorkManager.getInstance();
        Data.Builder dateData = new Data.Builder();
        dateData.putString("CRUD Action", "Read");
        dateData.putString("Month Name", monthName);
        workRequest = new OneTimeWorkRequest.Builder(DateWorker.class)
                .setInputData(dateData.build())
                .build();
        dateWorkManager.enqueue(workRequest);

        dateWorkManager.getWorkInfoByIdLiveData(workRequest.getId()).observeForever(new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if(workInfo != null && workInfo.getState().isFinished())
                {
                    //Deserialize dateInexListUpdate
                    Gson gson = new Gson();
                    String tempList = workInfo.getOutputData().getString("List Data");
                    Type dateInexType = new TypeToken<ArrayList<DateInex>>(){}.getType();
                    ArrayList<DateInex> dateInexList = gson.fromJson(tempList, dateInexType);
                    dateInexLive.postValue(dateInexList);
                }
            }
        });
    }

    //Delete date if its total income and expenses are equals to zero
    public void deleteDate(String dateName)
    {
        dateWorkManager = WorkManager.getInstance();
        Data.Builder dateData = new Data.Builder();
        dateData.putString("CRUD Action", "Delete");
        dateData.putString("Date Name", dateName);
        workRequest = new OneTimeWorkRequest.Builder(DateWorker.class)
                .setInputData(dateData.build())
                .build();
        dateWorkManager.enqueue(workRequest);

        dateWorkManager.getWorkInfoByIdLiveData(workRequest.getId()).observeForever(new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if(workInfo != null && workInfo.getState().isFinished())
                {
                    isDateInexDeleted.postValue(true);
                }
            }
        });
    }
}
