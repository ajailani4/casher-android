package com.example.casher.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.casher.model.database.DatabaseHelper;
import com.example.casher.model.objects.InexDetails;
import com.example.casher.viewmodel.worker.DetailsInexWorker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DetailsInexViewModel extends AndroidViewModel {
    private WorkManager detailsInexWorkManager;
    private OneTimeWorkRequest workRequest;

    private MutableLiveData<Boolean> isDetailsInexInserted = new MutableLiveData<>();
    private MutableLiveData<ArrayList<InexDetails>> inexDetailsList = new MutableLiveData<>();
    private MutableLiveData<Boolean> isDetailsInexEditted = new MutableLiveData<>();
    private MutableLiveData<Boolean> isDetailsInexDeleted = new MutableLiveData<>();

    public DetailsInexViewModel(Application application)
    {
        super(application);
    }

    public MutableLiveData<Boolean> getDetailsInexInserted()
    {
        return isDetailsInexInserted;
    }

    public MutableLiveData<ArrayList<InexDetails>> getInexDetailsList() {
        return inexDetailsList;
    }

    public MutableLiveData<Boolean> getIsDetailsInexEditted()
    {
        return isDetailsInexEditted;
    }

    public MutableLiveData<Boolean> getIsDetailsInexDeleted()
    {
        return isDetailsInexDeleted;
    }

    //Insert income based on date and month
    public void insertInDetails(InexDetails inexDetails)
    {
        detailsInexWorkManager = WorkManager.getInstance();
        Data.Builder detailsInexData = new Data.Builder();
        detailsInexData.putString("CRUD Action", "Insert");
        detailsInexData.putString("Inex Choice", "Income");
        detailsInexData.putString("Date Name", inexDetails.getDateName());
        detailsInexData.putString("In Categories", inexDetails.getCategories());
        detailsInexData.putInt("In Total", (int) inexDetails.getTotal());
        detailsInexData.putString("In Notes", inexDetails.getNotes());
        workRequest = new OneTimeWorkRequest.Builder(DetailsInexWorker.class)
                .setInputData(detailsInexData.build())
                .build();
        detailsInexWorkManager.enqueue(workRequest);

        detailsInexWorkManager.getWorkInfoByIdLiveData(workRequest.getId()).observeForever(new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if(workInfo != null && workInfo.getState().isFinished())
                {
                    isDetailsInexInserted.postValue(true);
                }
            }
        });
    }

    //Insert expenses based on date and month
    public void insertExDetails(InexDetails inexDetails)
    {
        detailsInexWorkManager = WorkManager.getInstance();
        Data.Builder detailsInexData = new Data.Builder();
        detailsInexData.putString("CRUD Action", "Insert");
        detailsInexData.putString("Inex Choice", "Expenses");
        detailsInexData.putString("Date Name", inexDetails.getDateName());
        detailsInexData.putString("Ex Categories", inexDetails.getCategories());
        detailsInexData.putInt("Ex Total", (int) inexDetails.getTotal());
        detailsInexData.putString("Ex Notes", inexDetails.getNotes());
        workRequest = new OneTimeWorkRequest.Builder(DetailsInexWorker.class)
                .setInputData(detailsInexData.build())
                .build();
        detailsInexWorkManager.enqueue(workRequest);

        detailsInexWorkManager.getWorkInfoByIdLiveData(workRequest.getId()).observeForever(new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if(workInfo != null && workInfo.getState().isFinished())
                {
                    isDetailsInexInserted.postValue(true);
                }
            }
        });
    }

    //Read all InEx based on date and month from database
    public void getAllInexDetails()
    {
        detailsInexWorkManager = WorkManager.getInstance();
        Data.Builder detailsInexData = new Data.Builder();
        detailsInexData.putString("CRUD Action", "Read");
        workRequest = new OneTimeWorkRequest.Builder(DetailsInexWorker.class)
                .setInputData(detailsInexData.build())
                .build();
        detailsInexWorkManager.enqueue(workRequest);

        detailsInexWorkManager.getWorkInfoByIdLiveData(workRequest.getId()).observeForever(new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if(workInfo != null && workInfo.getState().isFinished())
                {
                    //Deserialize inexDetailsListUpdate
                    Gson gson = new Gson();
                    String tempList = workInfo.getOutputData().getString("List Data");
                    Type inexDetailsType = new TypeToken<ArrayList<InexDetails>>(){}.getType();
                    ArrayList<InexDetails> inexDetailsUpdate = gson.fromJson(tempList, inexDetailsType);

                    inexDetailsList.postValue(inexDetailsUpdate);
                }
            }
        });
    }

    //Edit details of InEx
    public void editInexDetails(InexDetails inexDetails)
    {
        detailsInexWorkManager = WorkManager.getInstance();
        Data.Builder detailsInexData = new Data.Builder();
        detailsInexData.putString("CRUD Action", "Edit");
        detailsInexData.putString("Id", inexDetails.getId());
        detailsInexData.putString("Inex Choice", inexDetails.getInex());
        detailsInexData.putString("Inex Categories", inexDetails.getCategories());
        detailsInexData.putInt("Inex Total", (int) inexDetails.getTotal());
        detailsInexData.putString("Inex Notes", inexDetails.getNotes());
        workRequest = new OneTimeWorkRequest.Builder(DetailsInexWorker.class)
                .setInputData(detailsInexData.build())
                .build();
        detailsInexWorkManager.enqueue(workRequest);

        detailsInexWorkManager.getWorkInfoByIdLiveData(workRequest.getId()).observeForever(new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if(workInfo != null && workInfo.getState().isFinished())
                {
                    isDetailsInexEditted.postValue(true);
                }
            }
        });
    }

    //Delete details of InEx
    public void deleteInexDetails(String id, String inexChoice)
    {
        detailsInexWorkManager = WorkManager.getInstance();
        Data.Builder detailsInexData = new Data.Builder();
        detailsInexData.putString("CRUD Action", "Delete");
        detailsInexData.putString("Id", id);
        detailsInexData.putString("Inex Choice", inexChoice);
        workRequest = new OneTimeWorkRequest.Builder(DetailsInexWorker.class)
                .setInputData(detailsInexData.build())
                .build();
        detailsInexWorkManager.enqueue(workRequest);

        detailsInexWorkManager.getWorkInfoByIdLiveData(workRequest.getId()).observeForever(new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if(workInfo != null && workInfo.getState().isFinished())
                {
                    isDetailsInexDeleted.postValue(true);
                }
            }
        });
    }
}
