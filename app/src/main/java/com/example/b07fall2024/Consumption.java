package com.example.b07fall2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Consumption implements QuesAns {
    private final List<String> questionText;
    private final List<Map<String, String>> options;
    private final Map<String, String> selectedAnswer;

    public Consumption() {
        questionText = new ArrayList<>();
        options = new ArrayList<>();
        selectedAnswer = new HashMap<>();

        // Add questions
        questionText.add("How often do you buy new clothes?");
        questionText.add("Do you buy second-hand or eco-friendly products?");
        questionText.add("How many electronic devices (phones, laptops, etc.) have you purchased in the past year?");
        questionText.add("How often do you recycle?");

        // Add options for each question
        Map<String, String> options1 = new HashMap<>();
        options1.put("A", "Monthly");
        options1.put("B", "Quarterly");
        options1.put("C", "Annually");
        options1.put("D", "Rarely");
        options.add(options1);

        Map<String, String> options2 = new HashMap<>();
        options2.put("A", "Yes, regularly");
        options2.put("B", "Yes, occasionally");
        options2.put("C", "No");
        options.add(options2);

        Map<String, String> options3 = new HashMap<>();
        options3.put("A", "None");
        options3.put("B", "1");
        options3.put("C", "2");
        options3.put("D", "3 or more");
        options.add(options3);

        Map<String, String> options4 = new HashMap<>();
        options4.put("A", "Never");
        options4.put("B", "Occasionally");
        options4.put("C", "Frequently");
        options4.put("D", "Always");
        options.add(options4);
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
                System.out.println("Saved in consumption " + question + "answer " + value);
            } else {
                throw new QuestionException("Invalid answer: " + key + " for question: " + question);
            }
        } else {
            throw new QuestionException("Invalid question: " + question);
        }
    }
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

    public float getEmissions() {
        float total = 0;

        String ans1 = getSelectedAnswer("How often do you buy new clothes?");
        String ans2 = getSelectedAnswer("Do you buy second-hand or eco-friendly products?");
        String ans3 = getSelectedAnswer("How many electronic devices (phones, laptops, etc.) have you purchased in the past year?");
        String ans4 = getSelectedAnswer("How often do you recycle?");

        HashMap<String, Float> ans2ToMultiplier = new HashMap<>(Map.of(
                "Yes, regularly", 0.5f,
                "Yes, occasionally", 0.7f,
                "No", 1f,
                "",0f
        ));

        HashMap<String, Integer> ans3ToCO2 = new HashMap<>(Map.of(
                "None", 0,
                "1", 300,
                "2", 600,
                "3 or more", 900,
                "",0
        ));

        HashMap<String, Integer> ans4ToCO2 = new HashMap<>(Map.of(
                "Never", 0,
                "Occasionally", 0,
                "Frequently", 30,
                "Always", 50,
                "",0
        ));

        HashMap<String, Float> ans4ToCO2Rarely = new HashMap<>(Map.of(
                "Never", 0f,
                "Occasionally", 0.75f,
                "Frequently", 1.5f,
                "Always", 2.5f,
                "",0f
        ));

        total += 5 * ans2ToMultiplier.get(ans2);
        total += ans3ToCO2.get(ans3);
        if (ans1.equals("Rarely")) {
            return total - ans4ToCO2Rarely.get(ans4);
        }

        total = total - ans4ToCO2.get(ans4);
        return total / 1000;

    }
}
