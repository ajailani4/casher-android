package com.example.casher.viewmodel.worker;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.casher.model.objects.InexStatistics;
import com.example.casher.model.repository.DataRepository;
import com.example.casher.model.repository.SqliteDataSource;
import com.google.gson.Gson;

import java.util.ArrayList;

public class InexStatsWorker extends Worker {
    private Context mContext;
    private DataRepository dataRepository;
    private ArrayList<InexStatistics> inexStatsListUpdate;

    public InexStatsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Data inputData = getInputData();
        String monthName = inputData.getString("Month Name");
        String inexChoice = inputData.getString("Inex Choice");

        dataRepository = new DataRepository(new SqliteDataSource(mContext), monthName);
        inexStatsListUpdate = dataRepository.getAllInexStats(inexChoice);

        //Serialize inexStatsListUpdate to JSON
        Gson gson = new Gson();
        String tempList = gson.toJson(inexStatsListUpdate);

        Data outputData = new Data.Builder()
                .putString("List Data", tempList)
                .build();

        Log.d("InexStats Worker", "Working");
        return Result.success(outputData);
    }
}
