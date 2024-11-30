package com.habit;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.b072024gr2.ecoproj.R;

import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private Context context;
    private List<Habit> habitList;

    public HabitAdapter(Context context, List<Habit> habitList) {
        this.context = context;
        this.habitList = habitList;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_habit, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit habit = habitList.get(position);

        holder.tvHabitName.setText(habit.getName());
        holder.tvHabitDescription.setText(habit.getDescription());
        holder.tvCategory.setText("类别: " + habit.getCategory());
        holder.tvImpactLevel.setText("影响力: " + habit.getImpactLevel() + "/5");
    }

    @Override
    public int getItemCount() {
        return habitList.size();
    }

    public static class HabitViewHolder extends RecyclerView.ViewHolder {

        TextView tvHabitName, tvHabitDescription, tvCategory, tvImpactLevel;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHabitName = itemView.findViewById(R.id.tvHabitName);
            tvHabitDescription = itemView.findViewById(R.id.tvHabitDescription);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvImpactLevel = itemView.findViewById(R.id.tvImpactLevel);
        }
    }
}
