package com.example.casher.viewmodel.worker;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.casher.model.objects.InexDetails;
import com.example.casher.model.repository.DataRepository;
import com.example.casher.model.repository.SqliteDataSource;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DetailsInexWorker extends Worker {
    private Context mContext;

    private DataRepository dataRepository;
    private InexDetails inexDetails = new InexDetails();
    private ArrayList<InexDetails> inexDetailsListUpdate;

    public DetailsInexWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Data inputData = getInputData();
        String crudAction = inputData.getString("CRUD Action");
        String inexChoice = inputData.getString("Inex Choice");
        String dateName = inputData.getString("Date Name");
        String id = inputData.getString("Id");

        if(crudAction.equals("Insert"))
        {
            Log.d("DetailsInex Worker", "Inserting...");

            if(inexChoice.equals("Income"))
            {
                String inCategories = inputData.getString("In Categories");
                int inTotal = inputData.getInt("In Total", 0);
                String inNotes = inputData.getString("In Notes");

                inexDetails.setDateName(dateName);
                inexDetails.setCategories(inCategories);
                inexDetails.setTotal(inTotal);
                inexDetails.setNotes(inNotes);

                dataRepository = new DataRepository(new SqliteDataSource(mContext), inexDetails);
                dataRepository.insertInDetails();
            } else if(inexChoice.equals("Expenses"))
            {
                String exCategories = inputData.getString("Ex Categories");
                int exTotal = inputData.getInt("Ex Total", 0);
                String exNotes = inputData.getString("Ex Notes");

                inexDetails.setDateName(dateName);
                inexDetails.setCategories(exCategories);
                inexDetails.setTotal(exTotal);
                inexDetails.setNotes(exNotes);

                dataRepository = new DataRepository(new SqliteDataSource(mContext), inexDetails);
                dataRepository.insertExDetails();
            }
        } else if(crudAction.equals("Read"))
        {
            Log.d("DetailsInex Worker", "Reading...");

            dataRepository = new DataRepository(new SqliteDataSource(mContext));
            inexDetailsListUpdate = dataRepository.getAllInexDetails();
        } else if(crudAction.equals("Edit"))
        {
            Log.d("DetailsInex Worker", "Editing...");

            String inexCategories = inputData.getString("Inex Categories");
            int inexTotal = inputData.getInt("Inex Total", 0);
            String inexNotes = inputData.getString("Inex Notes");

            inexDetails.setId(id);
            inexDetails.setInex(inexChoice);
            inexDetails.setCategories(inexCategories);
            inexDetails.setTotal(inexTotal);
            inexDetails.setNotes(inexNotes);

            dataRepository = new DataRepository(new SqliteDataSource(mContext), inexDetails);
            dataRepository.editInexDetails();
        } else if(crudAction.equals("Delete"))
        {
            Log.d("DetailsInex Worker", "Deleting...");

            inexDetails.setId(id);
            inexDetails.setInex(inexChoice);

            dataRepository = new DataRepository(new SqliteDataSource(mContext), inexDetails);
            dataRepository.deleteInexDetails();
        }

        //Serialize inexDetailsListUpdate to JSON
        Gson gson = new Gson();
        String tempList = gson.toJson(inexDetailsListUpdate);

        Data outputData = new Data.Builder()
                .putString("List Data", tempList)
                .build();

        Log.d("DetailsInex Worker", "Working");
        return Result.success(outputData);
    }
}
