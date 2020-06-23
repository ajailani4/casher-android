package com.example.casher.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.casher.model.objects.InexStatistics;
import com.example.casher.viewmodel.worker.InexStatsWorker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class InexStatsViewModel extends AndroidViewModel {
    private WorkManager inexStatsWorkManager;
    private OneTimeWorkRequest workRequest;

    private MutableLiveData<ArrayList<InexStatistics>> inexStatsList = new MutableLiveData<>();

    public InexStatsViewModel(Application application) {
        super(application);
    }

    public MutableLiveData<ArrayList<InexStatistics>> getInexStatsList()
    {
        return inexStatsList;
    }

    //Read all InEx Statistics based on month name and Inex Choice from database
    public void getAllInexStats(String monthName, String inexChoice)
    {
        inexStatsWorkManager = WorkManager.getInstance();
        Data.Builder inexStatsData = new Data.Builder();
        inexStatsData.putString("Month Name", monthName);
        inexStatsData.putString("Inex Choice", inexChoice);
        workRequest = new OneTimeWorkRequest.Builder(InexStatsWorker.class).
                setInputData(inexStatsData.build())
                .build();
        inexStatsWorkManager.enqueue(workRequest);

        inexStatsWorkManager.getWorkInfoByIdLiveData(workRequest.getId()).observeForever(new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if(workInfo != null && workInfo.getState().isFinished())
                {
                    //Deserialize inexStatsListUpdate
                    Gson gson = new Gson();
                    String tempList = workInfo.getOutputData().getString("List Data");
                    Type inexStatsType = new TypeToken<ArrayList<InexStatistics>>(){}.getType();
                    ArrayList<InexStatistics> inexStatsUpdate = gson.fromJson(tempList, inexStatsType);

                    inexStatsList.postValue(inexStatsUpdate);
                }
            }
        });
    }
}
