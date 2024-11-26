package com.example.b07fall2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Transportation implements QuesAns {
    private final List<String> questionText;
    private final List<Map<String, String>> options;
    private final Map<String, String> selectedAnswer;

    public Transportation() {
        // Initialize options and questionText arrays
        questionText = new ArrayList<>();
        options = new ArrayList<>();
        selectedAnswer = new HashMap<>();

        questionText.add("Do you own or regularly use a car?");
        questionText.add("What type of car do you drive?");
        questionText.add("How many kilometers/miles do you drive per year?");
        questionText.add("How often do you use public transportation (bus, train, subway)?");
        questionText.add("How much time do you spend on public transport per week (bus, train, subway)?");
        questionText.add("How many short-haul flights (less than 1,500 km / 932 miles) have you taken in the past year?");
        questionText.add("How many long-haul flights (more than 1,500 km / 932 miles) have you taken in the past year?");

        Map<String, String> options1 = new HashMap<>();
        options1.put("A", "Yes");
        options1.put("B", "No");
        options.add(options1);

        Map<String, String> options2 = new HashMap<>();
        options2.put("A", "Gasoline");
        options2.put("B", "Diesel");
        options2.put("C", "Hybrid");
        options2.put("D", "Electric");
        options2.put("E", "I don’t know");
        options.add(options2);

        Map<String, String> options3 = new HashMap<>();
        options3.put("A", "Up to 5,000 km (3,000 miles)");
        options3.put("B", "5,000–10,000 km (3,000–6,000 miles)");
        options3.put("C", "10,000–15,000 km (6,000–9,000 miles)");
        options3.put("D", "15,000–20,000 km (9,000–12,000 miles)");
        options3.put("E", "20,000–25,000 km (12,000–15,000 miles)");
        options3.put("F", "More than 25,000 km (15,000 miles)");
        options.add(options3);

        Map<String, String> options4 = new HashMap<>();
        options4.put("A", "Never");
        options4.put("B", "Occasionally (1-2 times/week)");
        options4.put("C", "Frequently (3-4 times/week)");
        options4.put("D", "Always (5+ times/week)");
        options.add(options4);

        Map<String, String> options5 = new HashMap<>();
        options5.put("A", "Under 1 hour");
        options5.put("B", "1-3 hours");
        options5.put("C", "3-5 hours");
        options5.put("D", "5-10 hours");
        options5.put("E", "More than 10 hours");
        options.add(options5);

        Map<String, String> options6 = new HashMap<>();
        options6.put("A", "None");
        options6.put("B", "1-2 flights");
        options6.put("C", "3-5 flights");
        options6.put("D", "6-10 flights");
        options6.put("E", "More than 10 flights");
        options.add(options6);

        Map<String, String> options7 = new HashMap<>();
        options7.put("A", "None");
        options7.put("B", "1-2 flights");
        options7.put("C", "3-5 flights");
        options7.put("D", "6-10 flights");
        options7.put("E", "More than 10 flights");
        options.add(options7);
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
                System.out.println("Saved in transportation " + question + "answer " + value);
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
