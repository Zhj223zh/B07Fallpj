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
}
