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
            //throw new QuestionException("No answer selected for question: " + question);
            return "";
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

    @Override
    public float getEmissions() {
        float total = 0;

        String ans1 = getSelectedAnswer("What best describes your diet?");
        String ans2 = getSelectedAnswer("How often do you eat the following animal-based products?: Beef");
        String ans3 = getSelectedAnswer("How often do you eat the following animal-based products?: Pork");
        String ans4 = getSelectedAnswer("How often do you eat the following animal-based products?: Chicken");
        String ans5 = getSelectedAnswer("How often do you eat the following animal-based products?: Fish/Seafood");
        String ans6 = getSelectedAnswer("How often do you waste food or throw away uneaten leftovers?");

        HashMap<String, Integer> ans1ToCO2 = new HashMap<>(Map.of(
                "Vegetarian", 1000,
                "Vegan", 500,
                "Pescatarian (fish/seafood)", 1500,
                "Meat-based (eat all types of animal products)", 0,
                "",0
        ));

        HashMap<String, Integer> ans2ToCO2 = new HashMap<>(Map.of(
                "Daily", 2500,
                "Frequently (3-5 times/week)", 1900,
                "Occasionally (1-2 times/week)", 1300,
                "Never", 0,
                "",0
        ));

        HashMap<String, Integer> ans3ToCO2 = new HashMap<>(Map.of(
                "Daily", 1450,
                "Frequently (3-5 times/week)", 860,
                "Occasionally (1-2 times/week)", 450,
                "Never", 0,
                "",0
        ));

        HashMap<String, Integer> ans4ToCO2 = new HashMap<>(Map.of(
                "Daily", 950,
                "Frequently (3-5 times/week)", 600,
                "Occasionally (1-2 times/week)", 200,
                "Never", 0,
                "",0
        ));

        HashMap<String, Integer> ans5ToCO2 = new HashMap<>(Map.of(
                "Daily", 800,
                "Frequently (3-5 times/week)", 500,
                "Occasionally (1-2 times/week)", 150,
                "Never", 0,
                "",0
        ));

        HashMap<String, Float> ans6ToCO2 = new HashMap<>(Map.of(
                "Never", 0f,
                "Rarely", 23.4f,
                "Occasionally", 70.2f,
                "Frequently", 140.4f,
                "",0f
        ));

        total += ans1ToCO2.get(ans1);
        total += ans2ToCO2.get(ans2);
        total += ans3ToCO2.get(ans3);
        total += ans4ToCO2.get(ans4);
        total += ans5ToCO2.get(ans5);
        total += ans6ToCO2.get(ans6);

        return total / 1000;

    }
}
