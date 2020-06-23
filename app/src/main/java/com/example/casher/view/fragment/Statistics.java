package com.example.casher.view.fragment;

import android.app.Application;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.casher.model.objects.InexStatistics;
import com.example.casher.view.activity.MainActivity;
import com.example.casher.R;
import com.example.casher.view.adapter.InexAdapter;
import com.example.casher.view.adapter.InexStatsAdapter;
import com.example.casher.viewmodel.InexStatsViewModel;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Statistics extends Fragment {

    private ConstraintLayout statsLayout;
    private Spinner inexSpinner;
    private PieChartView pieChartView;
    private TextView titleTv;
    private TextView inexTotalTv;
    private View separatedView;
    private RecyclerView inexStatsRv;
    private RecyclerView inexDetailsRv;
    private LinearLayoutManager layoutManagerGroup;
    private ConstraintLayout noTransLayout;
    private TextView noTransMonthTv;

    private ArrayAdapter spinnerAdapter;
    private InexStatsViewModel inexStatsViewModel;
    private InexStatsAdapter inexStatsAdapter;
    private InexAdapter inexDetailsAdapter;

    private String monthName;
    private List<InexStatistics> inexStatsList;
    private List<InexDetails> inexDetailsList;
    private List<InexStatistics> tempList;

    public Statistics() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getParentFragmentManager().setFragmentResultListener("monthStats", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                monthName = result.getString("month");
                Log.d("Month name in Stats", monthName);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.stats));
        ((MainActivity) getActivity()).setBellVisibility(false);
        ((MainActivity) getActivity()).getSupportActionBar().setElevation(20);

        statsLayout = view.findViewById(R.id.stats_layout);
        inexSpinner = view.findViewById(R.id.inex_spinner);
        titleTv = view.findViewById(R.id.inex_stat_title);
        inexTotalTv = view.findViewById(R.id.inex_stat_total);
        pieChartView = view.findViewById(R.id.pie_chart);
        separatedView = view.findViewById(R.id.separated_view);
        inexStatsRv = view.findViewById(R.id.inex_stat_rv);
        inexDetailsRv = view.findViewById(R.id.inex_stat_details_rv);
        noTransLayout = view.findViewById(R.id.notrans_layout);
        noTransMonthTv = view.findViewById(R.id.notrans_month_tv);

        //Adapter for Spinner
        spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.inex, R.layout.custom_spinner_inex);
        spinnerAdapter.setDropDownViewResource(R.layout.custom_dropdown);
        inexSpinner.setAdapter(spinnerAdapter);

        //Set visible of Spinner, Pie Chart, InexDetails RecyclerView, and No Transactions Layout
        statsLayout.setVisibility(View.INVISIBLE);
        separatedView.setVisibility(View.GONE);
        inexDetailsRv.setVisibility(View.GONE);
        noTransLayout.setVisibility(View.GONE);

        //Initialize ViewModel
        inexStatsViewModel = new ViewModelProvider(this).get(InexStatsViewModel.class);

        //OBSERVER FOR inexStatsList
        final Observer<ArrayList<InexStatistics>> inexStatsListObserver = new Observer<ArrayList<InexStatistics>>() {
            @Override
            public void onChanged(ArrayList<InexStatistics> inexStatsUpdate) {
                inexStatsList = inexStatsUpdate;
                int totalInex;

                Locale localeID = new Locale("in", "ID");
                NumberFormat rpFormat = NumberFormat.getCurrencyInstance(localeID);

                if(inexStatsList.get(0).getTotalIncome() <= 0 && inexStatsList.get(0).getTotalExpenses() <= 0)
                {
                    Log.d("stats", "null");
                    statsLayout.setVisibility(View.GONE);
                    separatedView.setVisibility(View.GONE);
                    inexDetailsRv.setVisibility(View.GONE);
                    noTransLayout.setVisibility(View.VISIBLE);

                    String noTransMonth = "No transactions in " + monthName;

                    noTransMonthTv.setText(noTransMonth);
                } else
                {
                    Log.d("stats", "not null");
                    statsLayout.setVisibility(View.VISIBLE);
                    noTransLayout.setVisibility(View.GONE);

                    if(inexStatsList.size() > 0)
                    {
                        separatedView.setVisibility(View.VISIBLE);
                        inexDetailsRv.setVisibility(View.VISIBLE);
                        totalInex = inexStatsList.get(0).getTotalInex();
                    } else
                    {
                        totalInex = 0;
                    }

                    BigDecimal totalInexBD = new BigDecimal(totalInex);
                    inexTotalTv.setText(rpFormat.format(totalInexBD));

                    if(inexStatsList.get(0).getCategories() != null)
                    {
                        pieChartView.setVisibility(View.VISIBLE);
                        inexStatsRv.setVisibility(View.VISIBLE);
                        inexDetailsRv.setVisibility(View.VISIBLE);

                        showPieChart();
                        setInexStatsAdapter();
                        setInexDetailsAdapter();
                    } else
                    {
                        pieChartView.setVisibility(View.INVISIBLE);
                        inexStatsRv.setVisibility(View.INVISIBLE);
                        inexDetailsRv.setVisibility(View.INVISIBLE);
                    }
                }

                Log.d("InexStats Read", "Finished");
            }
        };

        inexStatsViewModel.getInexStatsList().observe(getViewLifecycleOwner(), inexStatsListObserver);

        inexSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String title = "Total " + parent.getItemAtPosition(position).toString() + " in " + monthName;
                titleTv.setText(title);

                inexStatsViewModel.getAllInexStats(monthName, parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    //Set value and show the PieChart
    public void showPieChart()
    {
        //Set value for pieChartView
        List<SliceValue> pieData = new ArrayList<>();

        for(int i=0;i<inexStatsList.size();i++)
        {
            pieData.add(new SliceValue((float) inexStatsList.get(i).getPercentage(), inexStatsList.get(i).getColor()));
        }

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasCenterCircle(true);
        pieChartView.setChartRotationEnabled(false);
        pieChartView.setPieChartData(pieChartData);
    }

    //Create InexStats Adapter
    public void setInexStatsAdapter()
    {
        inexStatsAdapter = new InexStatsAdapter(inexStatsList);
        layoutManagerGroup = new LinearLayoutManager(getContext());
        inexStatsRv.setLayoutManager(layoutManagerGroup);
        inexStatsRv.setAdapter(inexStatsAdapter);
    }

    //Create InexDetails Adapter
    public void setInexDetailsAdapter()
    {
        inexDetailsList = new ArrayList<>();

        for(int i=0;i<inexStatsList.size();i++)
        {
            InexDetails inexDetails = new InexDetails();
            inexDetails.setCategories(inexStatsList.get(i).getCategories());

            String notes;

            if(inexStatsList.get(i).getCategories().equals("others_in") || inexStatsList.get(i).getCategories().equals("others_ex"))
            {
                notes = inexStatsList.get(i).getCategories().substring(0, 1).toUpperCase() + inexStatsList.get(i).getCategories().substring(1, 6);
            } else
            {
                notes = inexStatsList.get(i).getCategories().substring(0, 1).toUpperCase() + inexStatsList.get(i).getCategories().substring(1);
            }

            inexDetails.setNotes(notes);
            inexDetails.setTotal(inexStatsList.get(i).getTotalCat());

            inexDetailsList.add(inexDetails);
        }

        inexDetailsAdapter = new InexAdapter(inexDetailsList, "Statistics");
        layoutManagerGroup = new LinearLayoutManager(getContext());
        inexDetailsRv.setLayoutManager(layoutManagerGroup);
        inexDetailsRv.setAdapter(inexDetailsAdapter);
    }
}
