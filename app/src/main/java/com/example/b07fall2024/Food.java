package com.example.b07fall2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Food implements QuesAns {
    private final List<String> questionText;
    private final List<Map<String, String>> options;
    private final Map<String, String> selectedAnswer;

    public Food() {
        // Initialize options and questionText arrays
        questionText = new ArrayList<>();
        options = new ArrayList<>();
        selectedAnswer = new HashMap<>();

        questionText.add("What best describes your diet?");
        questionText.add("How often do you eat the following animal-based products?: Beef");
        questionText.add("How often do you eat the following animal-based products?: Pork");
        questionText.add("How often do you eat the following animal-based products?: Chicken");
        questionText.add("How often do you eat the following animal-based products?: Fish/Seafood");
        questionText.add("How often do you waste food or throw away uneaten leftovers?");

        Map<String, String> options1 = new HashMap<>();
        options1.put("A", "Vegetarian");
        options1.put("B", "Vegan");
        options1.put("C", "Pescatarian (fish/seafood)");
        options1.put("D", "Meat-based (eat all types of animal products)");
        options.add(options1);

        Map<String, String> options2 = new HashMap<>();
        options2.put("A", "Daily");
        options2.put("B", "Frequently (3-5 times/week)");
        options2.put("C", "Occasionally (1-2 times/week)");
        options2.put("D", "Never");
        options.add(options2);

        Map<String, String> options3 = new HashMap<>();
        options3.put("A", "Daily");
        options3.put("B", "Frequently (3-5 times/week)");
        options3.put("C", "Occasionally (1-2 times/week)");
        options3.put("D", "Never");
        options.add(options3);

        Map<String, String> options4 = new HashMap<>();
        options4.put("A", "Daily");
        options4.put("B", "Frequently (3-5 times/week)");
        options4.put("C", "Occasionally (1-2 times/week)");
        options4.put("D", "Never");
        options.add(options4);

        Map<String, String> options5 = new HashMap<>();
        options5.put("A", "Daily");
        options5.put("B", "Frequently (3-5 times/week)");
        options5.put("C", "Occasionally (1-2 times/week)");
        options5.put("D", "Never");
        options.add(options5);

        Map<String, String> options6 = new HashMap<>();
        options6.put("A", "Never");
        options6.put("B", "Rarely");
        options6.put("C", "Occasionally");
        options6.put("D", "Frequently");
        options.add(options6);
    }

    @Override
    public String getQuestionText(int questionIndex) {
        if (questionIndex >= 0 && questionIndex < questionText.size()) {
            return questionText.get(questionIndex);
        } else {
            throw new QuestionException("Invalid question index: " + questionIndex);
        }
    }

    @Override
    public Map<String, String> getOptions(int questionIndex) {
        if (questionIndex >= 0 && questionIndex < options.size()) {
            return options.get(questionIndex);
        } else {
            throw new QuestionException("Invalid question index: " + questionIndex);
        }
    }

    @Override
    public String getSelectedAnswer(String question) {
        if (selectedAnswer.containsKey(question)) {
            return selectedAnswer.get(question);
        } else {
            throw new QuestionException("No answer selected for question: " + question);
        }
    }

    @Override
    public void setSelectedAnswer(String question, String key) {
        if (questionText.contains(question)) {
            int questionIndex = questionText.indexOf(question);
            Map<String, String> questionOptions = options.get(questionIndex);
            if (questionOptions.containsKey(key)) {
                String value = questionOptions.get(key);
                selectedAnswer.put(question, value);
                System.out.println("Saved in food " + question + "answer " + value);
            } else {
                throw new QuestionException("Invalid answer: " + key + " for question: " + question);
            }
        } else {
            throw new QuestionException("Invalid question: " + question);
        }
    }

    // Function to verify if the key exists in the options
    public boolean isValidOption(int questionIndex, String answer) {
        if (questionIndex >= 0 && questionIndex < options.size()) {
            return options.get(questionIndex).containsValue(answer);
        }
        return false;
    }
    @Override
    public int questionTextSize(){
        return questionText.size();
    }
}
