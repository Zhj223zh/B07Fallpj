package com.example.b07fall2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizTransportation implements QuesAns {
    private final List<String> questionText;
    private final List<Map<String, String>> options;
    private final Map<String, String> selectedAnswer;

    public QuizTransportation() {
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

    @Override
    public float getEmissions() {
        float total = 0;

        String ans1 = getSelectedAnswer("Do you own or regularly use a car?");
        String ans2 = getSelectedAnswer("What type of car do you drive?");
        String ans3 = getSelectedAnswer("How many kilometers/miles do you drive per year?");
        String ans4 = getSelectedAnswer("How often do you use public transportation (bus, train, subway)?");
        String ans5 = getSelectedAnswer("How much time do you spend on public transport per week (bus, train, subway)?");
        String ans6 = getSelectedAnswer("How many short-haul flights (less than 1,500 km / 932 miles) have you taken in the past year?");
        String ans7 = getSelectedAnswer("How many long-haul flights (more than 1,500 km / 932 miles) have you taken in the past year?");

        if (ans1.equals("No")) {
            return 0;
        }

        HashMap<String, Float> ans2ToCO2 = new HashMap<>(Map.of(
                "Gasoline", 0.24f,
                "Diesel", 0.27f,
                "Hybrid", 0.16f,
                "Electric", 0.05f));
        HashMap<String, Integer> ans3ToMiles = new HashMap<>(Map.of(
                "Up to 5,000 km (3,000 miles)", 5000,
                "5,000–10,000 km (3,000–6,000 miles)", 10000,
                "10,000–15,000 km (6,000–9,000 miles)", 15000,
                "15,000–20,000 km (9,000–12,000 miles)", 20000,
                "20,000–25,000 km (12,000–15,000 miles)", 25000,
                "More than 25,000 km (15,000 miles)", 35000));

        // Answers 4 and 5
        HashMap<String, Integer> ans5Occasionally = new HashMap<>(Map.of(
                "Under 1 hour", 246,
                "1-3 hours", 819,
                "3-5 hours", 1638,
                "5-10 hours", 3071,
                "More than 10 hours", 9555));
        HashMap<String, Integer> ans5Frequently = new HashMap<>(Map.of(
                "Under 1 hour", 573,
                "1-3 hours", 1911,
                "3-5 hours", 3822,
                "5-10 hours", 7166,
                "More than 10 hours", 9555));

        HashMap<String, Integer> ans4ToCO2 = new HashMap<>(Map.of(
                "Never", 0,
                "Occasionally", ans5Occasionally.get(ans5),
                "Frequently", ans5Frequently.get(ans5),
                "Always", ans5Frequently.get(ans5)));

        // answer 6
        HashMap<String, Integer> ans6ToCO2 = new HashMap<>(Map.of(
                "None", 0,
                "1-2 flights", 225,
                "3-5 flights", 600,
                "6-10 flights", 1200,
                "More than 10 flights", 1800));

        // answer 7
        HashMap<String, Integer> ans7ToCO2 = new HashMap<>(Map.of(
                "None", 0,
                "1-2 flights", 825,
                "3-5 flights", 2200,
                "6-10 flights", 4400,
                "More than 10 flights", 6600));

        float kgPerKm = ans2ToCO2.get(ans2);
        int milesDriven = ans3ToMiles.get(ans3);

        total += kgPerKm * milesDriven;
        total += ans4ToCO2.get(ans4);
        total += ans6ToCO2.get(ans6);
        total += ans7ToCO2.get(ans7);

        return total / 1000;
    }
}
