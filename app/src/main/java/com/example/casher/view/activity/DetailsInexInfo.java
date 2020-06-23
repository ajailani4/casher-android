package com.example.casher.view.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casher.R;
import com.example.casher.model.objects.InexDetails;
import com.example.casher.view.adapter.InexAdapter;
import com.example.casher.viewmodel.DateViewModel;
import com.example.casher.viewmodel.DetailsInexViewModel;
import com.example.casher.viewmodel.MonthViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DetailsInexInfo extends AppCompatActivity {

    private InexDetails inexDetails;
    private MonthViewModel monthViewModel;
    private DateViewModel dateViewModel;
    private DetailsInexViewModel detailsInexViewModel;

    private ImageView iconDetails;
    private TextView titleDetails, dateValue, inexCatValue, totalValue, notesValue;
    private FloatingActionButton deleteFab, editFab;

    private int catIconImg;
    private String title;
    private String date;
    private String inexCat;
    private BigDecimal total;
    private String notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailsinex_info);

        getSupportActionBar().setTitle(getResources().getString(R.string.details_inex_info));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        iconDetails = findViewById(R.id.icon_details);
        titleDetails = findViewById(R.id.title_details);
        dateValue = findViewById(R.id.date_value);
        inexCatValue = findViewById(R.id.inex_category_value);
        totalValue = findViewById(R.id.total_value);
        notesValue = findViewById(R.id.notes_value);
        deleteFab = findViewById(R.id.delete_fab);
        editFab = findViewById(R.id.edit_fab);

        detailsInexViewModel = new ViewModelProvider(this).get(DetailsInexViewModel.class);
        dateViewModel = new ViewModelProvider(this).get(DateViewModel.class);
        monthViewModel = new ViewModelProvider(this).get(MonthViewModel.class);

        Locale localeID = new Locale("in", "ID");
        NumberFormat rpFormat = NumberFormat.getCurrencyInstance(localeID);

        inexDetails = getIntent().getParcelableExtra("Details Info");
        getCatIconImg(inexDetails.getCategories());

        if(inexDetails.getCategories().equals("others_in") || inexDetails.getCategories().equals("others_ex"))
        {
            title = inexDetails.getCategories().substring(0, 1).toUpperCase() + inexDetails.getCategories().substring(1, 6);
        } else
        {
            title = inexDetails.getCategories().substring(0, 1).toUpperCase() + inexDetails.getCategories().substring(1);
        }

        date = inexDetails.getDateName();
        inexCat = inexDetails.getInex();
        total = new BigDecimal(inexDetails.getTotal());
        notes = inexDetails.getNotes();

        iconDetails.setImageResource(catIconImg);
        titleDetails.setText(title);
        dateValue.setText(date);
        inexCatValue.setText(inexCat);
        totalValue.setText(rpFormat.format(total));
        notesValue.setText(notes);

        /* Observe all of WorkManager LiveData through each ViewModel */

        //OBSERVER FOR isDetailsInexDeleted LiveData
        final Observer<Boolean> inexDetailsDeletedObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDetailsInexDeleted) {
                Log.d("InexDetails Delete", String.valueOf(isDetailsInexDeleted));

                if(isDetailsInexDeleted)
                {
                    ProgressDialog progressDialog = new ProgressDialog(DetailsInexInfo.this);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    dateViewModel.insertDate(inexDetails.getDateName());
                }
            }
        };

        //Observe the isDetailsInexDeleted LiveData
        detailsInexViewModel.getIsDetailsInexDeleted().observe(this, inexDetailsDeletedObserver);

        //OBSERVER FOR isDateInexInserted
        final Observer<Boolean> dateInexInsertedObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDateInexInserted) {
                Log.d("Date Update", String.valueOf(isDateInexInserted));

                if(isDateInexInserted)
                {
                    dateViewModel.deleteDate(inexDetails.getDateName());
                } else
                {
                    Toast.makeText(getApplicationContext(), "Unsuccessfully deleted", Toast.LENGTH_SHORT).show();
                }
            }
        };

        //Observe the isDateInexInserted LiveData
        dateViewModel.getDateInexInserted().observe(this, dateInexInsertedObserver);

        //OBSERVER FOR isDateInexDeleted LiveData
        final Observer<Boolean> dateInexDeletedObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDateInexDeleted) {
                Log.d("Date Delete if 0", String.valueOf(isDateInexDeleted));

                if(isDateInexDeleted)
                {
                    //Get monthName
                    String monthName = "";
                    try
                    {
                        SimpleDateFormat inSdf = new SimpleDateFormat("dd-MM-yyyy");
                        Date dateForMonth = inSdf.parse(inexDetails.getDateName());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dateForMonth);
                        int month = cal.get(Calendar.MONTH);
                        monthName = MonthViewModel.monthArray[month];
                    } catch(Exception ex)
                    {

                    }

                    monthViewModel.insertMonth(monthName);
                }
            }
        };

        dateViewModel.getDateInexDeleted().observe(this, dateInexDeletedObserver);

        //OBSERVER FOR isMonthInexInserted LiveData
        final Observer<Boolean> monthInexInsertedObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isMonthInexInserted) {
                Log.d("Month Update", String.valueOf(isMonthInexInserted));

                if(isMonthInexInserted)
                {
                    Toast.makeText(getApplicationContext(), "Successfully deleted", Toast.LENGTH_SHORT).show();

                    Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainIntent);
                } else
                {
                    Toast.makeText(getApplicationContext(), "Unsuccessfully deleted", Toast.LENGTH_SHORT).show();
                }
            }
        };

        //Observe the isMonthInexInserted LiveData
        monthViewModel.getMonthInexInserted().observe(this, monthInexInsertedObserver);

        detailsInexViewModel.getIsDetailsInexDeleted().observe(this, inexDetailsDeletedObserver);

        //Edit Button is clicked
        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                mainIntent.putExtra("Fragment", "Add");
                mainIntent.putExtra("Details Info", inexDetails);
                startActivity(mainIntent);
            }
        });

        //Delete Button is clicked
        deleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog();
            }
        });
    }

    public void getCatIconImg(String iconCategories)
    {
        switch(iconCategories)
        {
            case "salary":
            {
                catIconImg = R.drawable.icon_salary;
                break;
            }

            case "awards":
            {
                catIconImg = R.drawable.icon_awards;
                break;
            }

            case "grants":
            {
                catIconImg = R.drawable.icon_grants;
                break;
            }

            case "sale":
            {
                catIconImg = R.drawable.icon_sale;
                break;
            }

            case "rental":
            {
                catIconImg = R.drawable.icon_rental;
                break;
            }

            case "coupons":
            {
                catIconImg = R.drawable.icon_coupons;
                break;
            }

            case "invest":
            {
                catIconImg = R.drawable.icon_invest;
                break;
            }

            case "others_in":
            {
                catIconImg = R.drawable.icon_others_in;
                break;
            }

            //Expenses
            case "food":
            {
                catIconImg = R.drawable.icon_food;
                break;
            }

            case "shopping":
            {
                catIconImg = R.drawable.icon_shopping;
                break;
            }

            case "transport":
            {
                catIconImg = R.drawable.icon_transport;
                break;
            }

            case "entertain":
            {
                catIconImg = R.drawable.icon_entertain;
                break;
            }

            case "clothes":
            {
                catIconImg = R.drawable.icon_clothes;
                break;
            }

            case "education":
            {
                catIconImg = R.drawable.icon_education;
                break;
            }

            case "office":
            {
                catIconImg = R.drawable.icon_office;
                break;
            }

            case "others_ex":
            {
                catIconImg = R.drawable.icon_others_ex;
                break;
            }
        }
    }

    public void showConfirmDialog()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Delete")
                .setMessage("Are you sure you want to delete this one?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        detailsInexViewModel.deleteInexDetails(inexDetails.getId(), inexDetails.getInex());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
