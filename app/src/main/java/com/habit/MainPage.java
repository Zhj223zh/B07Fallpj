package com.habit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AlertDialog;

import com.b072024gr2.ecoproj.R;

import java.util.ArrayList;
import java.util.List;

public class MainPage extends AppCompatActivity {

    private RecyclerView recyclerViewHabits;
    private HabitAdapter habitAdapter;
    private List<Habit> habitList;
    private SearchView searchView;
    private Button btnFilterType, btnFilterImpact, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        // 初始化组件
        recyclerViewHabits = findViewById(R.id.recyclerViewHabits);
        searchView = findViewById(R.id.searchView);
        btnFilterType = findViewById(R.id.btnFilterType);
        btnFilterImpact = findViewById(R.id.btnFilterImpact);
        btnReset = findViewById(R.id.btnReset);

        // 初始化数据
        habitList = getDummyHabits();
        habitAdapter = new HabitAdapter(this, habitList);

        // 配置 RecyclerView
        recyclerViewHabits.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHabits.setAdapter(habitAdapter);

        // 搜索功能
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterHabitsByName(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterHabitsByName(newText);
                return false;
            }
        });

        // 按类别筛选
        btnFilterType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterByTypeDialog();
            }
        });

        // 按影响力筛选
        btnFilterImpact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterByImpactDialog();
            }
        });

        // 重置筛选
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilters();
            }
        });
    }

    /**
     * 示例数据：预定义的习惯列表
     */
    private List<Habit> getDummyHabits() {
        List<Habit> habits = new ArrayList<>();
        habits.add(new Habit("Walk to school", "Transportation", "减少驾驶，选择步行或骑行。", 5));
        habits.add(new Habit("Reduce Food Waste", "Food", "按需购买食物，避免浪费。", 4));
        habits.add(new Habit("Recycling", "Consumption", "回收和重复利用不需要的物品。", 3));
        habits.add(new Habit("Use Public Transportation", "Transportation", "更多使用公交或地铁，减少开车。", 4));
        return habits;
    }

    /**
     * 搜索功能：根据用户输入过滤习惯列表
     */
    private void filterHabitsByName(String query) {
        List<Habit> filteredList = new ArrayList<>();
        for (Habit habit : habitList) {
            if (habit.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(habit);
            }
        }
        updateRecyclerView(filteredList);
    }

    /**
     * 筛选功能：按类别过滤习惯
     */
    private void filterByType(String category) {
        List<Habit> filteredList = new ArrayList<>();
        for (Habit habit : habitList) {
            if (habit.getCategory().equalsIgnoreCase(category)) {
                filteredList.add(habit);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No habits found for category: " + category, Toast.LENGTH_SHORT).show();
        }

        updateRecyclerView(filteredList);
    }

    /**
     * 筛选功能：按影响力过滤习惯
     */
    private void filterByImpact(int minImpact) {
        List<Habit> filteredList = new ArrayList<>();
        for (Habit habit : habitList) {
            if (habit.getImpactLevel() >= minImpact) {
                filteredList.add(habit);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No habits found with impact >= " + minImpact, Toast.LENGTH_SHORT).show();
        }

        updateRecyclerView(filteredList);
    }

    /**
     * 重置筛选
     */
    private void resetFilters() {
        updateRecyclerView(habitList);
    }

    /**
     * 显示类别筛选对话框
     */
    private void showFilterByTypeDialog() {
        final String[] categories = {"Transportation", "Food", "Consumption"};
        new AlertDialog.Builder(this)
                .setTitle("Select a category")
                .setItems(categories, (dialog, which) -> {
                    filterByType(categories[which]); // 根据用户选择的类别进行筛选
                })
                .show();
    }

    /**
     * 显示影响力筛选对话框
     */
    private void showFilterByImpactDialog() {
        final String[] impacts = {"1", "2", "3", "4", "5"};
        new AlertDialog.Builder(this)
                .setTitle("Select minimum impact")
                .setItems(impacts, (dialog, which) -> {
                    filterByImpact(Integer.parseInt(impacts[which])); // 根据用户选择的影响力进行筛选
                })
                .show();
    }

    /**
     * 更新 RecyclerView 数据
     */
    private void updateRecyclerView(List<Habit> newList) {
        habitAdapter = new HabitAdapter(this, newList);
        recyclerViewHabits.setAdapter(habitAdapter);
    }
}
