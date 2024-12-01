package com.habit;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.b072024gr2.ecoproj.R;


import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.ViewHolder> {

    private final Context context;
    private final List<Habit> habitList;


    public HabitAdapter(Context context, List<Habit> habitList) {
        this.context = context;
        this.habitList = habitList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_habit, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Habit habit = habitList.get(position);

        holder.tvHabitName.setText(habit.getName());
        holder.tvHabitDescription.setText(habit.getDescription());
        holder.tvCategory.setText("Type: " + habit.getCategory());
        holder.tvImpactLevel.setText("Impact: " + habit.getImpact() + "/5");
        holder.tvProgress.setText("Progress: " + habit.getProgress());

        // Log Progress
        holder.btnLogProgress.setOnClickListener(v -> {
            showInputDialog(holder, habit);
        });

        // Adopt Habit
        holder.btnAdopt.setOnClickListener(v -> {
            habit.setAdopted(true);
            saveAdopted(habit);
            Toast.makeText(context, "Adopted: " + habit.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return habitList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHabitName, tvHabitDescription, tvCategory, tvImpactLevel, tvProgress;
        Button btnLogProgress, btnAdopt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHabitName = itemView.findViewById(R.id.tvHabitName);
            tvHabitDescription = itemView.findViewById(R.id.tvHabitDescription);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvImpactLevel = itemView.findViewById(R.id.tvImpactLevel);
            tvProgress = itemView.findViewById(R.id.tvProgress);
            btnLogProgress = itemView.findViewById(R.id.btnLogProgress);
            btnAdopt = itemView.findViewById(R.id.btnAdopt);
        }
    }

    private void showInputDialog(ViewHolder holder, Habit habit) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Log Progress");

        final EditText input = new EditText(context);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            String inputValue = input.getText().toString();
            if (!inputValue.isEmpty()) {
                int addedProgress = Integer.parseInt(inputValue);
                habit.increaseProgress(addedProgress);
                holder.tvProgress.setText("Progress: " + habit.getProgress());
                Toast.makeText(context, "Progress updated for: " + habit.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void saveAdopted(Habit habit) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AdoptedHabits", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(habit.getName(), habit.getCategory());
        editor.apply();
    }
}
