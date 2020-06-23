package com.example.casher.view.adapter;

import android.content.Intent;
import android.util.Log;
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
import com.example.casher.model.objects.InexDetails;
import com.example.casher.view.activity.DetailsInexInfo;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InexAdapter extends RecyclerView.Adapter<InexAdapter.ViewHolder> {
    private List<InexDetails> inexDetailsList;
    private int catIconImg;
    private String iconCategories;
    private String fragmentName;

    public InexAdapter(List<InexDetails> inexDetailsList)
    {
        this.inexDetailsList = inexDetailsList;
    }

    public InexAdapter(List<InexDetails> inexDetailsList, String fragmentName)
    {
        this.inexDetailsList = inexDetailsList;
        this.fragmentName = fragmentName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_inex, parent, false);
        return new InexAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final InexDetails inexData = inexDetailsList.get(position);

        Locale localeID = new Locale("in", "ID");
        NumberFormat rpFormat = NumberFormat.getCurrencyInstance(localeID);

        iconCategories = inexData.getCategories();

        setCatIconImg(iconCategories);

        Glide.with(holder.itemView.getContext())
                .load(catIconImg)
                .apply(new RequestOptions().override(Target.SIZE_ORIGINAL))
                .into(holder.iconImgIv);

        holder.categoriesTv.setText(inexData.getNotes());
        BigDecimal inexTotal = new BigDecimal(inexData.getTotal());
        holder.totalTv.setText(rpFormat.format(inexTotal));

        if(fragmentName == null)
        {
            //When item is clicked
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent detailsInfoIntent = new Intent(holder.itemView.getContext(), DetailsInexInfo.class);
                    detailsInfoIntent.putExtra("Details Info", inexData);
                    holder.itemView.getContext().startActivity(detailsInfoIntent);
                }
            });
        } else if(fragmentName.equals("Statistics"))
        {
            holder.itemView.setClickable(false);
            holder.itemView.setFocusable(false);
        }
    }

    @Override
    public int getItemCount() {
        return inexDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImgIv;
        TextView categoriesTv;
        TextView totalTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iconImgIv = itemView.findViewById(R.id.inex_icon);
            categoriesTv = itemView.findViewById(R.id.inex_categories);
            totalTv = itemView.findViewById(R.id.inex_total);
        }
    }

    public void setCatIconImg(String iconCategories)
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
}
