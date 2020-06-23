package com.example.casher.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casher.model.objects.CategoriesItem;
import com.example.casher.model.objects.InexDetails;
import com.example.casher.view.activity.MainActivity;
import com.example.casher.R;
import com.example.casher.view.adapter.CategoriesAdapter;
import com.example.casher.viewmodel.CategoriesViewModel;
import com.example.casher.viewmodel.DateViewModel;
import com.example.casher.viewmodel.DetailsInexViewModel;
import com.example.casher.viewmodel.MonthViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Add extends Fragment {

    private Context context;
    private Spinner inexSpinner;
    private ImageView catIconIv;
    private TextView catTitleTv;
    private EditText totalInput;
    private EditText notesInput;
    private Button doneBtn;
    private RecyclerView categoriesRv;
    private RecyclerView.LayoutManager layoutManager;
    private CategoriesViewModel catViewModel;
    private MonthViewModel monthViewModel;
    private DateViewModel dateViewModel;
    private DetailsInexViewModel detailsInexViewModel;
    private InexDetails inexDetails;

    private ArrayAdapter spinnerAdapter;
    private CategoriesAdapter categoriesAdapter;

    private String inexChoice = "Income";
    private List<CategoriesItem> categoriesItemList;
    private String monthName;
    private String curDateName;
    private String catIcon = "salary";
    private String catTitle = "Salary";
    private int clickedPosition = 0;

    public Add() {
        // Required empty public constructor
    }

    public Add(InexDetails inexDetails)
    {
        this.inexDetails = inexDetails;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getParentFragmentManager().setFragmentResultListener("monthAdd", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                monthName = result.getString("month");
                Log.d("Month name in Add", monthName);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.add));
        ((MainActivity) getActivity()).setBellVisibility(false);
        ((MainActivity) getActivity()).getSupportActionBar().setElevation(20);

        context = view.getContext();

        inexSpinner = view.findViewById(R.id.inex_spinner);
        catIconIv = view.findViewById(R.id.cat_icon_input);
        catTitleTv = view.findViewById(R.id.cat_title_input);
        categoriesRv = view.findViewById(R.id.categories_rv);
        totalInput = view.findViewById(R.id.total_input);
        notesInput = view.findViewById(R.id.notes_input);
        doneBtn = view.findViewById(R.id.done_button);

        layoutManager = new GridLayoutManager(context, 4);
        categoriesRv.setLayoutManager(layoutManager);

        //Adapter for Spinner
        spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.inex, R.layout.custom_spinner_inex);
        spinnerAdapter.setDropDownViewResource(R.layout.custom_dropdown);
        inexSpinner.setAdapter(spinnerAdapter);

        //Initialize ViewModel
        catViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        monthViewModel = new ViewModelProvider(this).get(MonthViewModel.class);
        dateViewModel = new ViewModelProvider(this).get(DateViewModel.class);
        detailsInexViewModel = new ViewModelProvider(this).get(DetailsInexViewModel.class);

        //Get current date
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        curDateName = sdf.format(currentDate);
        Log.d("Current date", curDateName);

        //Get and Show Categories List
        categoriesItemList = new ArrayList<>();
        categoriesItemList.addAll(catViewModel.getCategoriesList(inexChoice, getContext()));

        if(inexDetails != null)
        {
            Log.d("inex details", "not null");
            catIcon = inexDetails.getCategories();
            catTitle = inexDetails.getCategories().substring(0, 1).toUpperCase() + inexDetails.getCategories().substring(1);
            curDateName = inexDetails.getDateName();

            //Get monthName of inexDetails
            try
            {
                SimpleDateFormat inSdf = new SimpleDateFormat("dd-MM-yyyy");
                Date date = inSdf.parse(inexDetails.getDateName());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int tempMonth = cal.get(Calendar.MONTH);
                monthName = MonthViewModel.monthArray[tempMonth];
            } catch(Exception ex)
            {

            }

            //Set view in Categories Item
            String[] catTitle = new String[8];

            if(inexDetails.getInex().equals("Income"))
            {
                catTitle = context.getResources().getStringArray(R.array.categories_icon_in);
            } else if(inexDetails.getInex().equals("Expenses"))
            {
                catTitle = context.getResources().getStringArray(R.array.categories_icon_ex);
            }

            clickedPosition = Arrays.asList(catTitle).indexOf(inexDetails.getCategories());

            if(!inexDetails.getInex().equals(inexSpinner.getSelectedItem()))
            {
                categoriesItemList = catViewModel.getCategoriesList(inexDetails.getInex(), getContext());
                inexChoice = inexDetails.getInex();
                inexSpinner.setSelection(spinnerAdapter.getPosition(inexDetails.getInex()));
            }

            showCategoriesList(clickedPosition);

            //Set Details of InEx Info in input
            catViewModel.setCatIcon(inexDetails.getCategories());
            catIconIv.setImageResource(catViewModel.catIconImg);
            String titleDetails = "";

            if(inexDetails.getCategories().equals("others_in") || inexDetails.getCategories().equals("others_ex"))
            {
                titleDetails = inexDetails.getCategories().substring(0, 1).toUpperCase() + inexDetails.getCategories().substring(1, 6);
            } else
            {
                titleDetails = inexDetails.getCategories().substring(0, 1).toUpperCase() + inexDetails.getCategories().substring(1);
            }

            catTitleTv.setText(titleDetails);

            totalInput.setText(String.valueOf((int) inexDetails.getTotal()));
            notesInput.setText(inexDetails.getNotes());
        } else
        {
            Log.d("inex details", "null");
            showCategoriesList(0);

            //Set icon and categories title when Add is started
            if(inexChoice.equals("Income"))
            {
                catTitleTv.setText(catViewModel.firstItemOnList);
                catIconIv.setImageResource(R.drawable.icon_salary);
                catTitle = "Salary";
                catIcon = "salary";
            } else if(inexChoice.equals("Expenses"))
            {
                catTitleTv.setText(catViewModel.firstItemOnList);
                catIconIv.setImageResource(R.drawable.icon_food);
                catTitle = "Food";
                catIcon = "food";
            }
        }

        /* Observe all of WorkManager LiveData through each ViewModel */

        //OBSERVER FOR isInexDetailsInserted
        final Observer<Boolean> inexDetailsInsertedObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDetailsInexInserted) {
                Log.d("InexDetails Update", String.valueOf(isDetailsInexInserted));

                if(isDetailsInexInserted)
                {
                    ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    insertDateInex();
                } else
                {
                    Toast.makeText(getContext(), "Unsuccessfully added", Toast.LENGTH_SHORT).show();
                }
            }
        };

        //Observe the isInexDetailsInserted LiveData
        detailsInexViewModel.getDetailsInexInserted().observe(getViewLifecycleOwner(), inexDetailsInsertedObserver);

        //OBSERVER FOR isDateInexInserted
        final Observer<Boolean> dateInexInsertedObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDateInexInserted) {
                Log.d("Date Update", String.valueOf(isDateInexInserted));

                if(isDateInexInserted)
                {
                    insertMonth();
                } else
                {
                    Toast.makeText(getContext(), "Unsuccessfully added", Toast.LENGTH_SHORT).show();
                }
            }
        };

        //Observe the isDateInexInserted LiveData
        dateViewModel.getDateInexInserted().observe(getViewLifecycleOwner(), dateInexInsertedObserver);

        //OBSERVER FOR isMonthInexInserted LiveData
        final Observer<Boolean> monthInexInsertedObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isMonthInexInserted) {
                Log.d("Month Update", String.valueOf(isMonthInexInserted));

                if(isMonthInexInserted)
                {
                    inexDetails = null;

                    Intent mainIntent = new Intent(getContext(), MainActivity.class);
                    startActivity(mainIntent);

                    Toast.makeText(getContext(), "Successfully added", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(getContext(), "Unsuccessfully added", Toast.LENGTH_SHORT).show();
                }
            }
        };

        //Observe the isMonthInexInserted LiveData
        monthViewModel.getMonthInexInserted().observe(getViewLifecycleOwner(), monthInexInsertedObserver);

        //OBSERVER FOR isDetailsInexEditted LiveData
        final Observer<Boolean> inexDetailsEdittedObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDetailsInexEditted) {
                Log.d("InexDetails Edit", String.valueOf(isDetailsInexEditted));

                if(isDetailsInexEditted)
                {
                    ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    insertDateInex();
                }
            }
        };

        detailsInexViewModel.getIsDetailsInexEditted().observe(getViewLifecycleOwner(), inexDetailsEdittedObserver);

        //OBSERVER FOR isDetailsInexDeleted LiveData
        final Observer<Boolean> inexDetailsDeletedObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDetailsInexDeleted) {
                Log.d("InexDetails Delete", String.valueOf(isDetailsInexDeleted));

                if(isDetailsInexDeleted)
                {
                    insertDetailsInex((int) inexDetails.getTotal(), inexDetails.getNotes());
                }
            }
        };

        detailsInexViewModel.getIsDetailsInexDeleted().observe(getViewLifecycleOwner(), inexDetailsDeletedObserver);

        inexSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!inexChoice.equals(parent.getItemAtPosition(position).toString()))
                {
                    categoriesItemList.clear();
                    categoriesAdapter.notifyDataSetChanged();
                    inexChoice = parent.getItemAtPosition(position).toString();
                    categoriesItemList = catViewModel.getCategoriesList(inexChoice, getContext());
                    showCategoriesList(0);

                    if(inexChoice.equals("Income"))
                    {
                        catTitleTv.setText(catViewModel.firstItemOnList);
                        catIconIv.setImageResource(R.drawable.icon_salary);
                        catTitle = "Salary";
                        catIcon = "salary";
                    } else if(inexChoice.equals("Expenses"))
                    {
                        catTitleTv.setText(catViewModel.firstItemOnList);
                        catIconIv.setImageResource(R.drawable.icon_food);
                        catTitle = "Food";
                        catIcon = "food";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Insert data when Done button is clicked
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totalInex = 0;
                String notesInex = "";

                if(totalInput.getText().toString().equals(""))
                {
                    Toast.makeText(getContext(), "Please add the total!", Toast.LENGTH_SHORT).show();
                } else
                {
                    totalInex = Integer.parseInt(totalInput.getText().toString());
                    notesInex = notesInput.getText().toString();

                    if(notesInex.equals(""))
                    {
                        notesInex = catTitle;
                    }

                    //Check if inexDetails is null
                    if(inexDetails == null)
                    {
                        insertDetailsInex(totalInex, notesInex);
                    } else
                    {
                        if(inexChoice.equals(inexDetails.getInex()))
                        {
                            inexDetails.setInex(inexChoice);
                            inexDetails.setCategories(catIcon);
                            inexDetails.setTotal(totalInex);
                            inexDetails.setNotes(notesInex);

                            editInexDetails(inexDetails);
                        } else
                        {
                            inexDetails.setTotal(totalInex);
                            inexDetails.setNotes(notesInex);

                            deleteInexDetails(inexDetails.getId(), inexDetails.getInex());
                        }
                    }
                }
            }
        });

        return view;
    }

    //Show CategoriesItem list
    public void showCategoriesList(int clickedPosition)
    {
        categoriesAdapter = new CategoriesAdapter(categoriesItemList, clickedPosition);
        categoriesRv.setAdapter(categoriesAdapter);

        catTitleTv.setText(catViewModel.firstItemOnList);

        categoriesAdapter.setOnItemClickCallback(new CategoriesAdapter.OnItemClickCallback() {
            @Override
            public void onItemClick(CategoriesItem categoriesItem) {
                catIcon = categoriesItem.getIcon();
                catTitle = categoriesItem.getTitle();

                catViewModel.setCatIcon(catIcon);

                catIconIv.setImageResource(catViewModel.catIconImg);
                catTitleTv.setText(catTitle);
            }
        });
    }

    //Insert details inex to the database
    public void insertDetailsInex(int totalInex, String notesInex)
    {
        Log.d("inex choice", inexSpinner.getSelectedItem().toString());

        InexDetails inexDetails = new InexDetails();
        inexDetails.setDateName(curDateName);
        inexDetails.setCategories(catIcon);
        inexDetails.setTotal(totalInex);
        inexDetails.setNotes(notesInex);

        if(inexSpinner.getSelectedItem().toString().equals("Income"))
        {
            detailsInexViewModel.insertInDetails(inexDetails);
        } else if(inexSpinner.getSelectedItem().toString().equals("Expenses"))
        {
            detailsInexViewModel.insertExDetails(inexDetails);
        }
    }

    //Insert date inex to the database
    public void insertDateInex()
    {
        dateViewModel.insertDate(curDateName);
    }

    //Insert month to the database
    public void insertMonth()
    {
        monthViewModel.insertMonth(monthName);
    }

    //Edit inex details
    public void editInexDetails(InexDetails inexDetails)
    {
        detailsInexViewModel.editInexDetails(inexDetails);
    }

    //Delete inex details
    public void deleteInexDetails(String idDel, String inexChoiceDel)
    {
        detailsInexViewModel.deleteInexDetails(idDel, inexChoiceDel);
    }
}
