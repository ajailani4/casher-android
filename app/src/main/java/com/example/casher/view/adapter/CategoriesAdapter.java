package com.example.casher.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.casher.R;
import com.example.casher.model.objects.CategoriesItem;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    List<CategoriesItem> categoriesItemList;
    public String catIcon;
    public int catIconImg;
    String catTitle;
    int clickedPosition;

    OnItemClickCallback onItemClickCallback;

    public CategoriesAdapter(List<CategoriesItem> categoriesItemList, int clickedPosition)
    {
        this.categoriesItemList = categoriesItemList;
        this.clickedPosition = clickedPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_categories, parent, false);
        return new CategoriesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final CategoriesItem categoriesItemData = categoriesItemList.get(position);
        catIcon = categoriesItemData.getIcon();
        catTitle = categoriesItemData.getTitle();

        getClickedIcon(position, clickedPosition);

        Glide.with(holder.itemView.getContext())
                .load(catIconImg)
                .apply(new RequestOptions().override(Target.SIZE_ORIGINAL))
                .into(holder.catIconIv);
        holder.catTitleTv.setText(catTitle);

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                clickedPosition = position;
                onItemClickCallback.onItemClick(categoriesItemData);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView catIconIv;
        TextView catTitleTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            catIconIv = itemView.findViewById(R.id.cat_icon);
            catTitleTv = itemView.findViewById(R.id.cat_title);
        }
    }

    public void getClickedIcon(int position, int clickedPosition)
    {
        if(position == clickedPosition)
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
        } else
        {
            switch(catIcon)
            {
                //Income
                case "salary":
                {
                    catIconImg = R.drawable.unclicked_icon_salary;
                    break;
                }

                case "awards":
                {
                    catIconImg = R.drawable.unclicked_icon_awards;
                    break;
                }

                case "grants":
                {
                    catIconImg = R.drawable.unclicked_icon_grants;
                    break;
                }

                case "sale":
                {
                    catIconImg = R.drawable.unclicked_icon_sale;
                    break;
                }

                case "rental":
                {
                    catIconImg = R.drawable.unclicked_icon_rental;
                    break;
                }

                case "coupons":
                {
                    catIconImg = R.drawable.unclicked_icon_coupons;
                    break;
                }

                case "invest":
                {
                    catIconImg = R.drawable.unclicked_icon_invest;
                    break;
                }

                case "others_in":
                {
                    catIconImg = R.drawable.unclicked_icon_others;
                    break;
                }

                //Expenses
                case "food":
                {
                    catIconImg = R.drawable.unclicked_icon_food;
                    break;
                }

                case "shopping":
                {
                    catIconImg = R.drawable.unclicked_icon_shopping;
                    break;
                }

                case "transport":
                {
                    catIconImg = R.drawable.unclicked_icon_transport;
                    break;
                }

                case "entertain":
                {
                    catIconImg = R.drawable.unclicked_icon_entertain;
                    break;
                }

                case "clothes":
                {
                    catIconImg = R.drawable.unclicked_icon_clothes;
                    break;
                }

                case "education":
                {
                    catIconImg = R.drawable.unclicked_icon_education;
                    break;
                }

                case "office":
                {
                    catIconImg = R.drawable.unclicked_icon_office;
                    break;
                }

                case "others_ex":
                {
                    catIconImg = R.drawable.unclicked_icon_others;
                    break;
                }
            }
        }
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback)
    {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback
    {
        void onItemClick(CategoriesItem categoriesItem);
    }
}
