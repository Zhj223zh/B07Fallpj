package com.example.b07fall2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Housing implements QuesAns {
    private final List<String> questionText;
    private final List<Map<String, String>> options;
    private final Map<String, String> selectedAnswer;

    public Housing() {
        questionText = new ArrayList<>();
        options = new ArrayList<>();
        selectedAnswer = new HashMap<>();

        // Initialize questions
        questionText.add("What type of home do you live in?");
        questionText.add("How many people live in your household?");
        questionText.add("What is the size of your home?");
        questionText.add("What type of energy do you use to heat your home?");
        questionText.add("What is your average monthly electricity bill?");
        questionText.add("What type of energy do you use to heat water in your home?");
        questionText.add("Do you use any renewable energy sources for electricity or heating (e.g., solar, wind)?");

        // Initialize options for each question
        Map<String, String> options1 = new HashMap<>();
        options1.put("A", "Detached house");
        options1.put("B", "Semi-detached house");
        options1.put("C", "Townhouse");
        options1.put("D", "Condo/Apartment");
        options1.put("E", "Other");
        options.add(options1);

        Map<String, String> options2 = new HashMap<>();
        options2.put("A", "1");
        options2.put("B", "2");
        options2.put("C", "3-4");
        options2.put("D", "5 or more");
        options.add(options2);

        Map<String, String> options3 = new HashMap<>();
        options3.put("A", "Under 1000 sq. ft.");
        options3.put("B", "1000-2000 sq. ft.");
        options3.put("C", "Over 2000 sq. ft.");
        options.add(options3);

        Map<String, String> options4 = new HashMap<>();
        options4.put("A", "Natural Gas");
        options4.put("B", "Electricity");
        options4.put("C", "Oil");
        options4.put("D", "Propane");
        options4.put("E", "Wood");
        options4.put("F", "Other");
        options.add(options4);

        Map<String, String> options5 = new HashMap<>();
        options5.put("A", "Under $50");
        options5.put("B", "$50-$100");
        options5.put("C", "$100-$150");
        options5.put("D", "$150-$200");
        options5.put("E", "Over $200");
        options.add(options5);

        Map<String, String> options6 = new HashMap<>();
        options6.put("A", "Natural Gas");
        options6.put("B", "Electricity");
        options6.put("C", "Oil");
        options6.put("D", "Propane");
        options6.put("E", "Solar");
        options6.put("F", "Other");
        options.add(options6);

        Map<String, String> options7 = new HashMap<>();
        options7.put("A", "Yes, primarily (more than 50% of energy use)");
        options7.put("B", "Yes, partially (less than 50% of energy use)");
        options7.put("C", "No");
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
                String value = questionOptions.get(key); // Get the value corresponding to the key
                 selectedAnswer.put(question, value);
                System.out.println("Saved in housing " + question + "answer " + value);
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

