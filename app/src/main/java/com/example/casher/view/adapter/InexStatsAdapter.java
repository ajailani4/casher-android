package com.example.casher.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.casher.R;
import com.example.casher.model.objects.InexDetails;
import com.example.casher.model.objects.InexStatistics;

import java.text.DecimalFormat;
import java.util.List;

public class InexStatsAdapter extends RecyclerView.Adapter<InexStatsAdapter.ViewHolder> {
    List<InexStatistics> inexStatsList;

    public InexStatsAdapter(List<InexStatistics> inexStatsList)
    {
        this.inexStatsList = inexStatsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_piechart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InexStatistics inexStatsData = inexStatsList.get(position);
        int colorCat = inexStatsData.getColor();
        String inexCat;
        double percentageCat = inexStatsData.getPercentage();

        if(inexStatsData.getCategories().equals("others_in") || inexStatsData.getCategories().equals("others_ex"))
        {
            inexCat = inexStatsData.getCategories().substring(0, 1).toUpperCase() + inexStatsData.getCategories().substring(1, 6);
        } else
        {
            inexCat = inexStatsData.getCategories().substring(0, 1).toUpperCase() + inexStatsData.getCategories().substring(1);
        }

        holder.colorTv.setBackgroundColor(colorCat);
        holder.categoriesTv.setText(inexCat);
        holder.percentageTv.setText(new DecimalFormat("##.##").format(percentageCat) + "%");
    }

    @Override
    public int getItemCount() {
        return inexStatsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View colorTv;
        TextView categoriesTv;
        TextView percentageTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            colorTv = itemView.findViewById(R.id.color_cat_stat);
            categoriesTv = itemView.findViewById(R.id.inex_cat_stat);
            percentageTv = itemView.findViewById(R.id.percentage_cat_stat);
        }
    }
}
