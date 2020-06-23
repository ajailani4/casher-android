package com.example.casher.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.casher.R;
import com.example.casher.model.objects.InexDetails;
import com.example.casher.view.fragment.Account;
import com.example.casher.view.fragment.Add;
import com.example.casher.view.fragment.Planning;
import com.example.casher.view.fragment.Home;
import com.example.casher.view.fragment.Statistics;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNav;
    private FloatingActionButton fab;
    private TextView actionBarTitle;
    private ImageView notifBell;

    private Home home;
    private Statistics statistics;
    private Add add;
    private Planning categories;
    private Account account;
    private InexDetails inexDetails;

    private boolean isHomeClicked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.custom_action_bar, null);
        actionBarTitle = actionBarView.findViewById(R.id.title_bar);
        notifBell = actionBarView.findViewById(R.id.bell);

        bottomNav = findViewById(R.id.bottom_nav);
        fab = findViewById(R.id.floating_btn);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(actionBarView);

        inexDetails = getIntent().getParcelableExtra("Details Info");
        String fragmentName = getIntent().getStringExtra("Fragment");

        if(fragmentName != null && fragmentName.equals("Add"))
        {
            add = new Add(inexDetails);
            loadFragment(add);

            bottomNav.getMenu().getItem(0).setCheckable(false);
        } else
        {
            home = new Home();
            loadFragment(home);
        }

        bottomNav.setOnNavigationItemSelectedListener(this);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(add == null)
                {
                    add = new Add(inexDetails);
                }

                isHomeClicked = false;

                loadFragment(add);

                for(int i=0;i<bottomNav.getMenu().size();i++)
                {
                    bottomNav.getMenu().getItem(i).setCheckable(false);
                }

                bottomNav.setSelectedItemId(R.id.nav_add);
            }
        });

        notifBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notifIntent = new Intent(getApplicationContext(), NotificationBell.class);
                startActivity(notifIntent);
            }
        });
    }

    //Change the ActionBar title based on BottomNav Item label
    public void setActionBarTitle(@NonNull String title)
    {
        actionBarTitle.setText(title);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.nav_home:
            {
                getSupportActionBar().setElevation(0);
                item.setCheckable(true);
                isHomeClicked = true;

                if(home == null)
                {
                    home = new Home();
                }

                loadFragment(home);
                break;
            }

            case R.id.nav_stats:
            {
                item.setCheckable(true);
                isHomeClicked = false;

                if(statistics == null)
                {
                    statistics = new Statistics();
                }

                loadFragment(statistics);
                break;
            }

            case R.id.nav_categories:
            {
                item.setCheckable(true);
                isHomeClicked = false;

                if(categories == null)
                {
                    categories = new Planning();
                }

                loadFragment(categories);
                break;
            }

            case R.id.nav_account:
            {
                item.setCheckable(true);
                isHomeClicked = false;

                if(account == null)
                {
                    account = new Account();
                }

                loadFragment(account);
                break;
            }
        }

        return true;
    }

    //Set visibility of Notification Bell
    public void setBellVisibility(boolean visible)
    {
        if(visible)
        {
            notifBell.setVisibility(View.VISIBLE);
        } else
        {
            notifBell.setVisibility(View.GONE);
        }
    }

    //Load Fragment
    public boolean loadFragment(Fragment fragment)
    {
        if(fragment != null)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();

            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed()
    {
        if(isHomeClicked)
        {
            if(isTaskRoot())
            {
                Log.d("Home", "finish");
                finishAffinity();
                finish();
            } else
            {
                //Check if inexDetails is null
                if(inexDetails != null)
                {
                    inexDetails = null;
                    super.onBackPressed();
                } else
                {
                    finishAffinity();
                    finish();
                }
            }
        } else
        {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }
    }
}
