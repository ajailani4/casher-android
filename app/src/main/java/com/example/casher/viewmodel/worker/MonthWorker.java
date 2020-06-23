package com.example.casher.viewmodel.worker;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.casher.model.objects.MonthInex;
import com.example.casher.model.repository.DataRepository;
import com.example.casher.model.repository.SqliteDataSource;
import com.google.gson.Gson;

public class MonthWorker extends Worker {
    private Context mContext;

    private DataRepository dataRepository;
    private MonthInex monthInexData = new MonthInex();

    public MonthWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Data inputData = getInputData();
        String crudAction = inputData.getString("CRUD Action");
        String monthName = inputData.getString("Month Name");

        dataRepository = new DataRepository(new SqliteDataSource(mContext), monthName);

        if(crudAction.equals("Insert"))
        {
            Log.d("Month Worker", "Inserting...");

            dataRepository.insertMonth();
        } else if(crudAction.equals("Read"))
        {
            Log.d("Month Worker", "Reading...");

            MonthInex tempMonthInex = dataRepository.getMonth();
            monthInexData.setTotalIncomeMonth(tempMonthInex.getTotalIncomeMonth());
            monthInexData.setTotalExpensesMonth(tempMonthInex.getTotalExpensesMonth());
            monthInexData.setBalanceMonth(tempMonthInex.getBalanceMonth());
        }

        //Serialize monthData to JSON
        Gson gson = new Gson();
        String tempData = gson.toJson(monthInexData);
        Data outputData = new Data.Builder()
                .putString("Data", tempData)
                .build();

        Log.d("Month Worker", "Working");
        return Result.success(outputData);
    }
}
