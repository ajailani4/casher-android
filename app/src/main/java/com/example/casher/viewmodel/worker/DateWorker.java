package com.example.casher.viewmodel.worker;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.casher.model.database.DatabaseHelper;
import com.example.casher.model.objects.DateInex;
import com.example.casher.model.repository.DataRepository;
import com.example.casher.model.repository.SqliteDataSource;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DateWorker extends Worker {
    private Context mContext;

    private DataRepository dataRepository;
    private ArrayList<DateInex> dateInexListUpdate;

    public DateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Data inputData = getInputData();
        String crudAction = inputData.getString("CRUD Action");
        String dateName = inputData.getString("Date Name");
        String monthName = inputData.getString("Month Name");

        dataRepository = new DataRepository(new SqliteDataSource(mContext), monthName, dateName);

        if(crudAction.equals("Insert"))
        {
            Log.d("Date Worker", "Inserting...");

            dataRepository.insertDate();
        } else if(crudAction.equals("Read"))
        {
            Log.d("Date Worker", "Reading...");

            dateInexListUpdate = dataRepository.getAllDate();
        } else if(crudAction.equals("Delete"))
        {
            Log.d("Date Worker", "Deleting...");

            dataRepository.deleteDate();
        }

        //Serialize dateInexListUpdate to JSON
        Gson gson = new Gson();
        String tempList = gson.toJson(dateInexListUpdate);

        Data outputData = new Data.Builder()
                .putString("List Data", tempList)
                .build();

        Log.d("Date Worker", "Working");
        return Result.success(outputData);
    }
}
