package com.example.b07fall2024;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.b072024gr2.ecoproj.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView question;
    private TextView timerTextView;
    private AppCompatButton nextBtn;
    private ImageView backBtn;
    private ManageQuesAns questionbank;
    private List<QuesAns> questions;
    private int currentCategoryIndex = 0;
    private Map<QuesAns, Integer> currentQuestionIndexMap;
    private Map<String, String> currentOptions;
    private LinearLayout optionsContainer;
    private TimeManager timeManager;
    private String country;
    private String selectedAnswer;

    private Spinner mySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz2);

        initUIComponents();
        initManagers();

        displayCurrentCategoryQuestion();

        nextBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    private void initUIComponents() {
        backBtn = findViewById(R.id.backBtn);
        timerTextView = findViewById(R.id.timer);
        question = findViewById(R.id.question);
        optionsContainer = findViewById(R.id.optionsContainer);
        nextBtn = findViewById(R.id.nextBtn);
        mySpinner = findViewById(R.id.my_spinner);
    }

    private void initManagers() {
        timeManager = new TimeManager(timerTextView, this);
        timeManager.startTimer();
        setSelectedCountry();
        initQuestionManager();
    }

    public void setSelectedCountry() {
        TextView selectedCountry = findViewById(R.id.country_name);
        country = selectedCountry.getText().toString();
        System.out.println("COUNTRY SET TO" + country);
        String getSelectedCountry = getIntent().getStringExtra("selected category");
        System.out.println("SELECTED COUNTRY SET TO " + getSelectedCountry);
        country = getSelectedCountry;
        System.out.println("COUNTRY SET TO " + country);
        if (getSelectedCountry != null && !getSelectedCountry.isEmpty()) {
            selectedCountry.setText(getSelectedCountry);
        } else {
            selectedCountry.setText(R.string.select_country);
        }
    }

    private void initQuestionManager() {
        questions = List.of(new QuizTransportation(), new Food(), new Housing(), new Consumption());
        questionbank = new ManageQuesAns(questions);
        questionbank.setCountry(country);

        currentQuestionIndexMap = new HashMap<>();
        for (QuesAns qa : questions) {
            currentQuestionIndexMap.put(qa, 0);
        }
    }
    /////fix here
    private void displayCurrentCategoryQuestion() {
        try {
            QuesAns currentQuesAns = questions.get(currentCategoryIndex);
            Integer currentQuestionIndex = currentQuestionIndexMap.get(currentQuesAns);

            System.out.println("Current question index: " + currentQuestionIndex);
            if (currentQuestionIndex == null) {
                throw new QuestionException("Current question index is null");
            }
            while (questionbank.shouldSkipQuestion(currentQuesAns, currentQuestionIndex)) {
                updateQuestionIndex(currentQuesAns, currentQuestionIndex);
                currentQuestionIndex = currentQuestionIndexMap.get(currentQuesAns); // Update index
                System.out.println("Index updated " + currentQuestionIndex);
            }

            String questionText = currentQuesAns.getQuestionText(currentQuestionIndex);
            currentOptions = currentQuesAns.getOptions(currentQuestionIndex);
            question.setText(questionText);
            addOptionButtons(currentOptions);


            System.out.println("QuizActivity: Displayed question: " + questionText);

        } catch (Exception e) {
            Toast.makeText(this, "An error occurred while displaying the question.", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateSpinner(List<String> options) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);
    }



    private void addOptionButtons(Map<String, String> options) {
        optionsContainer.removeAllViews();
        for (Map.Entry<String, String> entry : options.entrySet()) {
            AppCompatButton optionButton = new AppCompatButton(this);
            optionButton.setText(entry.getValue());
            optionButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            optionButton.setTag(entry.getKey());
            optionButton.setOnClickListener(this);
            optionsContainer.addView(optionButton);
        }
    }

    private void selectOption(String option) {
        for (int i = 0; i < optionsContainer.getChildCount(); i++) {
            View child = optionsContainer.getChildAt(i);
            if (child instanceof AppCompatButton) {
                child.setSelected(false);
                child.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        for (int i = 0; i < optionsContainer.getChildCount(); i++) {
            View child = optionsContainer.getChildAt(i);
            if (child instanceof AppCompatButton && option.equals(child.getTag())) {
                child.setSelected(true);
                child.setBackgroundColor(Color.parseColor("#009999"));
            }
        }
    }

    private String getSelectedAnswerFromOptions() {
        for (int i = 0; i < optionsContainer.getChildCount(); i++) {
            View child = optionsContainer.getChildAt(i);
            if (child instanceof AppCompatButton && child.isSelected()) {
                return (String) child.getTag();
            }
        }
        return "";
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timeManager.stopTimer();
        startActivity(new Intent(QuizActivity.this, StartActivity.class));
        finish();
    }

    private void disableQuizOptions() {
        for (int i = 0; i < optionsContainer.getChildCount(); i++) {
            View child = optionsContainer.getChildAt(i);
            if (child instanceof AppCompatButton) {
                child.setEnabled(false);
            }
        }
        nextBtn.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        handleButtonClick(view);
    }

    private void handleButtonClick(View view) {
        int id = view.getId();
        if (id == R.id.nextBtn) {
            handleNextButtonClick();
        }
        else if(id == R.id.backBtn) {
            handleBackButtonClick();
        }
        else if(view instanceof AppCompatButton) {
            handleOptionButtonClick((AppCompatButton) view);
        } else {
            Toast.makeText(this, "Invalid click detected", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleBackButtonClick() {
        QuesAns currentQuesAns = questions.get(currentCategoryIndex);

        if (currentQuesAns != null) {
            Integer currentQuestionIndex = currentQuestionIndexMap.get(currentQuesAns);

            if (currentQuestionIndex != null && currentQuestionIndex > 0) {
                // Go back to the previous question in the same category
                currentQuestionIndex--;
                currentQuestionIndexMap.put(currentQuesAns, currentQuestionIndex);
                displayCurrentCategoryQuestion();
                Toast.makeText(this, "Moved to previous question.", Toast.LENGTH_SHORT).show();
            } else if (currentCategoryIndex > 0) {
                // Move to the last question of the previous category
                currentCategoryIndex--;
                currentQuesAns = questions.get(currentCategoryIndex);
                int lastQuestionIndex = getLastQuestionIndex(currentQuesAns);
                currentQuestionIndexMap.put(currentQuesAns, lastQuestionIndex);
                displayCurrentCategoryQuestion();
                Toast.makeText(this, "Moved to previous category: " + currentCategoryIndex, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "This is the first question of the first category.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Error: currentQuesAns is null", Toast.LENGTH_SHORT).show();
        }
    }



    // Helper method to get the last question index
    private int getLastQuestionIndex(QuesAns quesAns) {
        int index = 0;
        while (true) {
            try {
                quesAns.getQuestionText(index);
                index++;
            } catch (QuestionException e) {
                break;
            }
        }
        return index - 1;  // Return the index of the last valid question
    }
    private void handleNextButtonClick() {
        String selectedAnswer = getSelectedAnswerFromOptions();
        if (mySpinner.getVisibility() == View.VISIBLE) {
            selectedAnswer = mySpinner.getSelectedItem().toString();
        } else {
            selectedAnswer = getSelectedAnswerFromOptions();
        }
        if (selectedAnswer != null && !selectedAnswer.isEmpty()) {
            QuesAns currentQuesAns = questions.get(currentCategoryIndex);
            if (currentQuesAns != null) {
                Integer currentQuestionIndex = currentQuestionIndexMap.get(currentQuesAns);
                if (currentQuestionIndex != null) {
                    questionbank.setAnswer(currentQuesAns, currentQuestionIndex, selectedAnswer);
                    updateQuestionIndex(currentQuesAns, currentQuestionIndex);
                    displayCurrentCategoryQuestion();
                    System.out.println("QuizActivity" +"Question Index Updated: " + currentQuestionIndex);
                } else {
                    Toast.makeText(this, "Error: currentQuestionIndex is null", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error: currentQuesAns is null", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateQuestionIndex(QuesAns currentQuesAns, int currentQuestionIndex) {
        if (currentQuestionIndex < getLastQuestionIndex(currentQuesAns)) {
            currentQuestionIndex++;
            currentQuestionIndexMap.put(currentQuesAns, currentQuestionIndex);
            System.out.println("QuizActivity"+ "Next Question Index: " + currentQuestionIndex);
        } else {
            if (currentCategoryIndex < questions.size() - 1) {
                currentCategoryIndex++;
                currentQuestionIndexMap.put(questions.get(currentCategoryIndex), 0); // Start from the first question of the next category
                System.out.println("QuizActivity"+ "Next Category Index: " + currentCategoryIndex);
            } else {
                Intent intent = new Intent(QuizActivity.this, FinishActivity.class);

                // Pass emissionsByCategory to FinishActivity
                for (Map.Entry<String, Float> entry : questionbank.getEmissionsByCategory().entrySet()) {
                    intent.putExtra(entry.getKey(), entry.getValue());
                }
                if (country == null || country.isEmpty()){
                    Log.d("QuizActivity", "country is null");
                    System.out.println("SUBMITTING: NULL COUNTRY");
                    intent.putExtra("country", "Canada");
                }
                else {
                    intent.putExtra("country", country);
                    System.out.println("SUBMITTING: COUNTRY = " + country);
                }

                startActivity(intent);
                finish();
            }
        }
    }

    //Source: QuizApplication | Android Studio Tutorial | 2024
    private void handleOptionButtonClick(AppCompatButton clickedButton) {
        selectedAnswer = clickedButton.getTag().toString();
        selectOption(selectedAnswer); // Ensure only one option is selected
    }
}
