package com.example.casher.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.casher.R;
import com.example.casher.model.objects.CategoriesItem;

import java.util.ArrayList;
import java.util.List;

public class CategoriesViewModel extends AndroidViewModel {

    private MutableLiveData<List<CategoriesItem>> categoriesItem;

    public String firstItemOnList;
    public int catIconImg = 0;

    public CategoriesViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<CategoriesItem>> getCategoriesItem()
    {
        if(categoriesItem == null)
        {
            categoriesItem = new MutableLiveData<>();
        }

        return categoriesItem;
    }

    public List<CategoriesItem> getCategoriesList(String inexChoice, Context context)
    {
        List<CategoriesItem> list = new ArrayList<>();
        String[] catIcon = new String[8];
        String[] catTitle = new String[8];

        if(inexChoice.equals("Income"))
        {
            catIcon = context.getResources().getStringArray(R.array.categories_icon_in);
            catTitle = context.getResources().getStringArray(R.array.categories_title_in);
        } else if(inexChoice.equals("Expenses"))
        {
            catIcon = context.getResources().getStringArray(R.array.categories_icon_ex);
            catTitle = context.getResources().getStringArray(R.array.categories_title_ex);
        }

        firstItemOnList = catTitle[0];

        for(int i=0;i<catIcon.length;i++)
        {
            CategoriesItem categoriesItem = new CategoriesItem();
            categoriesItem.setIcon(catIcon[i]);
            categoriesItem.setTitle(catTitle[i]);

            list.add(categoriesItem);
        }

        return list;
    }

    public void setCatIcon(String catIcon)
    {
        switch(catIcon)
        {
            //Income
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
}
