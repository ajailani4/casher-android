package com.example.casher.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.casher.R;
import com.example.casher.model.objects.InexDetails;
import com.example.casher.model.objects.DateInex;
import com.example.casher.view.fragment.Home;
import com.example.casher.viewmodel.DetailsInexViewModel;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateInexAdapter extends RecyclerView.Adapter<DateInexAdapter.ViewHolder> {
    private Context context;
    private DetailsInexViewModel detailsInexViewModel;

    private List<DateInex> dateInexList;
    private List<InexDetails> inexDetailsList;

    private String[] dateArray = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    public DateInexAdapter(Context context, List<DateInex> dateInexList, List<InexDetails> inexDetailsList)
    {
        this.context = context;
        this.dateInexList = dateInexList;
        this.inexDetailsList = inexDetailsList;
        detailsInexViewModel = new ViewModelProvider((FragmentActivity) context).get(DetailsInexViewModel.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_date_inex, parent, false);

        return new DateInexAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final DateInex dateInexData = dateInexList.get(position);

        try
        {
            SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = inFormat.parse(dateInexData.getDate());

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            int dateNum = cal.get(Calendar.DAY_OF_WEEK);
            String dateName = dateArray[dateNum-1];

            int day = cal.get(Calendar.DAY_OF_MONTH);

            String dateDay = dateName + ", " + Integer.toString(day);

            holder.dateInexTv.setText(dateDay);
        } catch(Exception ex)
        {
            Log.e("date err", ex.getMessage());
        }

        Locale localeID = new Locale("in", "ID");
        NumberFormat rpFormat = NumberFormat.getCurrencyInstance(localeID);

        BigDecimal incomeTotal = new BigDecimal(dateInexData.getIncomeTotal());
        BigDecimal expensesTotal = new BigDecimal(dateInexData.getExpensesTotal());
        holder.inTotalTv.setText(rpFormat.format(incomeTotal));
        holder.exTotalTv.setText(rpFormat.format(expensesTotal));

        //Check if dateName in inexDetailsList equals to dateName in dateInexList
        //If both of them are same, then put it in tempInexDetailsList
        List<InexDetails> tempInexDetailsList = new ArrayList<>();

        for(int i=0;i<inexDetailsList.size();i++)
        {
            InexDetails inexDetails = inexDetailsList.get(i);

            if(inexDetails.getDateName().equals(dateInexData.getDate()))
            {
                InexDetails tempInexDetails = new InexDetails();
                tempInexDetails.setId(inexDetails.getId());
                tempInexDetails.setInex(inexDetails.getInex());
                tempInexDetails.setDateName(inexDetails.getDateName());
                tempInexDetails.setCategories(inexDetails.getCategories());
                tempInexDetails.setTotal(inexDetails.getTotal());
                tempInexDetails.setNotes(inexDetails.getNotes());

                tempInexDetailsList.add(tempInexDetails);
            }
        }

        InexAdapter inexAdapter = new InexAdapter(tempInexDetailsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        holder.inexDetailsRv.setLayoutManager(linearLayoutManager);
        holder.inexDetailsRv.setAdapter(inexAdapter);
    }

    @Override
    public int getItemCount() {
        return dateInexList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateInexTv;
        TextView inTotalTv;
        TextView exTotalTv;
        RecyclerView inexDetailsRv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dateInexTv = itemView.findViewById(R.id.date_inex);
            inTotalTv = itemView.findViewById(R.id.total_income);
            exTotalTv = itemView.findViewById(R.id.total_expenses);
            inexDetailsRv = itemView.findViewById(R.id.inex_details_rv);
        }
    }

    public void observeInexDetails(final ViewHolder holder, final DateInex dateInexData)
    {
        //OBSERVER FOR inexDetailsListUpdate
        final Observer<ArrayList<InexDetails>> inexDetailsListObserver = new Observer<ArrayList<InexDetails>>() {
            @Override
            public void onChanged(ArrayList<InexDetails> inexDetailsListUpdate) {
                List<InexDetails> inexDetailsList = inexDetailsListUpdate;
                InexAdapter inexAdapter = new InexAdapter(inexDetailsList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                holder.inexDetailsRv.setLayoutManager(linearLayoutManager);
                holder.inexDetailsRv.setAdapter(inexAdapter);

                Locale localeID = new Locale("in", "ID");
                NumberFormat rpFormat = NumberFormat.getCurrencyInstance(localeID);

                BigDecimal incomeTotal = new BigDecimal(dateInexData.getIncomeTotal());
                BigDecimal expensesTotal = new BigDecimal(dateInexData.getExpensesTotal());
                holder.inTotalTv.setText(rpFormat.format(incomeTotal));
                holder.exTotalTv.setText(rpFormat.format(expensesTotal));

                Log.d("InexDetails Read", "Finished");
            }
        };

        //Observe the inexDetailsList LiveData
        detailsInexViewModel.getInexDetailsList().observeForever(inexDetailsListObserver);
    }
}
