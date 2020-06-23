package com.example.casher.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.casher.model.objects.InexDetails;
import com.example.casher.model.objects.MonthInex;
import com.example.casher.view.activity.MainActivity;
import com.example.casher.R;
import com.example.casher.model.objects.DateInex;
import com.example.casher.view.adapter.DateInexAdapter;
import com.example.casher.viewmodel.DateViewModel;
import com.example.casher.viewmodel.DetailsInexViewModel;
import com.example.casher.viewmodel.MonthViewModel;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    private View viewParent;
    private Spinner monthSpinner;
    private TextView balanceMonthTv;
    private TextView totalIncomeMonthTv;
    private TextView totalExpensesMonthTv;
    private RecyclerView dateInexRv;
    private LinearLayoutManager layoutManagerGroup;
    private ConstraintLayout noTransLayout;
    private TextView noTransMonthTv;

    private MonthViewModel monthViewModel;
    private DateViewModel dateViewModel;
    private DetailsInexViewModel detailsInexViewModel;
    private ArrayAdapter adapter;
    private DateInexAdapter dateInexAdapter;
    private MonthInex monthInexData;

    private String monthName;
    private List<DateInex> dateInexList;
    private List<InexDetails> inexDetailsList;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewParent = inflater.inflate(R.layout.fragment_home, container, false);
        Log.d("Home", "onCreateView");

        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.app_name));
        ((MainActivity) getActivity()).setBellVisibility(true);
        ((MainActivity) getActivity()).getSupportActionBar().setElevation(0);

        monthSpinner = viewParent.findViewById(R.id.month_spinner);
        dateInexRv = viewParent.findViewById(R.id.date_inex_rv);
        balanceMonthTv = viewParent.findViewById(R.id.balace_month);
        totalIncomeMonthTv = viewParent.findViewById(R.id.total_income_month);
        totalExpensesMonthTv = viewParent.findViewById(R.id.total_expenses_month);
        noTransLayout = viewParent.findViewById(R.id.notrans_layout);
        noTransMonthTv = viewParent.findViewById(R.id.notrans_month_tv);

        adapter = ArrayAdapter.createFromResource(getContext(), R.array.months, R.layout.custom_spinner);
        adapter.setDropDownViewResource(R.layout.custom_dropdown);
        monthSpinner.setAdapter(adapter);

        //Initialize ViewModel
        monthViewModel = new ViewModelProvider(this).get(MonthViewModel.class);
        dateViewModel = new ViewModelProvider(this).get(DateViewModel.class);
        detailsInexViewModel = new ViewModelProvider(this).get(DetailsInexViewModel.class);

        //Set visible of InexDetails RecyclerView and No Transactions Layout
        dateInexRv.setVisibility((View.VISIBLE));
        noTransLayout.setVisibility(View.GONE);

        //Get current month
        Date currentDate = Calendar.getInstance().getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int month = cal.get(Calendar.MONTH);
        monthName = monthViewModel.getMonthName(month);

        Log.d("Current month", monthName);

        //Send monthName to Add
        Bundle bundle = new Bundle();
        bundle.putString("month", monthName);
        getParentFragmentManager().setFragmentResult("monthAdd", bundle);

        monthSpinner.setSelection(adapter.getPosition(monthName));

        //OBSERVER FOR monthDataUpdate
        final Observer<MonthInex> monthInexDataObserver = new Observer<MonthInex>() {
            @Override
            public void onChanged(MonthInex monthInexUpdate) {
                monthInexData = monthInexUpdate;

                Locale localeID = new Locale("in", "ID");
                NumberFormat rpFormat = NumberFormat.getCurrencyInstance(localeID);

                BigDecimal totalIncomeMonth = new BigDecimal(monthInexData.getTotalIncomeMonth());
                BigDecimal totalExpensesMonth = new BigDecimal(monthInexData.getTotalExpensesMonth());
                BigDecimal balanceMonth = new BigDecimal(monthInexData.getBalanceMonth());

                balanceMonthTv.setText(rpFormat.format(balanceMonth));
                totalIncomeMonthTv.setText(rpFormat.format(totalIncomeMonth));
                totalExpensesMonthTv.setText(rpFormat.format(totalExpensesMonth));

                if(monthInexData.getTotalIncomeMonth() == 0 && monthInexData.getTotalExpensesMonth() == 0 && monthInexData.getBalanceMonth() == 0)
                {
                    Log.d("RV Enable", "false");
                    dateInexRv.setVisibility(View.GONE);
                    noTransLayout.setVisibility(View.VISIBLE);

                    String noTransMonth = "No transactions in " + monthSpinner.getSelectedItem();

                    noTransMonthTv.setText(noTransMonth);
                } else
                {
                    Log.d("RV Enable", "true");
                    dateInexRv.setVisibility((View.VISIBLE));
                    noTransLayout.setVisibility(View.GONE);

                    dateViewModel.getAllDate(monthName);
                }

                Log.d("MonthInex Read", "Finished");
            }
        };

        //Observe the monthInexData LiveData
        monthViewModel.getMonthInexLive().observe(getViewLifecycleOwner(), monthInexDataObserver);

        //OBSERVER FOR dateInexListUpdate
        final Observer<ArrayList<DateInex>> dateInexListObserver = new Observer<ArrayList<DateInex>>() {
            @Override
            public void onChanged(ArrayList<DateInex> dateInexListUpdate) {
                dateInexList = dateInexListUpdate;
                detailsInexViewModel.getAllInexDetails();

                Log.d("DateInex Read", "Finished");
            }
        };

        //Observe the dateInexList LiveData
        dateViewModel.getDateInexLive().observe(getViewLifecycleOwner(), dateInexListObserver);

        //OBSERVER FOR inexDetailsListUpdate
        final Observer<ArrayList<InexDetails>> inexDetailsListObserver = new Observer<ArrayList<InexDetails>>() {
            @Override
            public void onChanged(ArrayList<InexDetails> inexDetailsListUpdate) {
                inexDetailsList = inexDetailsListUpdate;

                for(int i=0;i<inexDetailsList.size();i++)
                {
                    InexDetails inexDetails = inexDetailsList.get(i);
                    Log.d("InexDetails", String.valueOf(inexDetails.getNotes()));
                }

                setDateInexAdapter();

                Log.d("InexDetails Read", "Finished");
            }
        };

        //Observe the inexDetailsList LiveData
        detailsInexViewModel.getInexDetailsList().observe(getViewLifecycleOwner(), inexDetailsListObserver);

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Month name in Home", monthName);

                monthViewModel.getMonth(parent.getItemAtPosition(position).toString());

                //Send monthName to Statistics
                Bundle bundle = new Bundle();
                bundle.putString("month", monthSpinner.getSelectedItem().toString());
                getParentFragmentManager().setFragmentResult("monthStats", bundle);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return viewParent;
    }

    //Create DateInex Adapter
    public void setDateInexAdapter()
    {
        dateInexAdapter = new DateInexAdapter(viewParent.getContext(), dateInexList, inexDetailsList);
        layoutManagerGroup = new LinearLayoutManager(getContext());
        dateInexRv.setLayoutManager(layoutManagerGroup);
        dateInexRv.setAdapter(dateInexAdapter);
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }
}
