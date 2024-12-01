import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Food implements QuesAns {
    private final Map<Integer, String> questionText;
    private final List<Map<String, String>> options;
    private final Map<Integer, String> selectedAnswer;
    private final int starting_quiz_number;
    private final int ending_quiz_number;

    public Food() {
        this.questionText = new HashMap<>();
        this.options = new ArrayList<>();
        this.selectedAnswer = new HashMap<>();
        starting_quiz_number = 1;
        ending_quiz_number = 3;

        questionText.put(1, "What best describes your diet?");
        questionText.put(2, "How often do you eat the following animal-based products?");
        questionText.put(3, "How often do you waste food or throw away uneaten leftovers?");

        // Add options for each question
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
        options.add(options3);

    }

    @Override
    public String getQuestionText(int questionIndex) {
        if (questionText.containsKey(questionIndex)) {
            return questionText.get(questionIndex);
        } else {
            throw new QuestionException("Invalid question index: " + questionIndex);
        }
    }

    @Override
    public Map<String, String> getOptions(int questionIndex) {
        if (questionIndex >= starting_quiz_number && questionIndex < ending_quiz_number) {
            return options.get(questionIndex - 1);
        } else {
            throw new QuestionException("Invalid question index: " + questionIndex);
        }
    }

    @Override
    public String getSelectedAnswer(int questionIndex) {
        if (selectedAnswer.containsKey(questionIndex)) {
            return selectedAnswer.get(questionIndex);
        } else {
            throw new QuestionException("No answer selected for question: " + questionIndex);
        }
    }

    @Override
    public void setSelectedAnswer(int index, String answer) {
        if (questionText.containsKey(index)) {
            selectedAnswer.put(index, answer);
        } else {
            throw new QuestionException("Invalid question index: " + index);
        }
    }

    @Override
    public int getEnding_quiz_number() {
        return ending_quiz_number;
    }

    @Override
    public int options_size(int number) {
        return options.get(number).size();
    }

    @Override
    public float getEmissions() {
        float total = 0;

        String ans1 = getSelectedAnswer(1);
        String ans2 = getSelectedAnswer(2);
        String ans3 = getSelectedAnswer(3);
        String ans4 = getSelectedAnswer(4);
        String ans5 = getSelectedAnswer(5);
        String ans6 = getSelectedAnswer(6);

        HashMap<String, Integer> ans1ToCO2 = new HashMap<>(Map.of(
                "Vegetarian", 1000,
                "Vegan", 500,
                "Pescatarian (fish/seafood)", 1500,
                "Meat-based (eat all types of animal products)", 0));

        HashMap<String, Integer> ans2ToCO2 = new HashMap<>(Map.of(
                "Daily", 2500,
                "Frequently (3-5 times/week)", 1900,
                "Occasionally (1-2 times/week)", 1300,
                "Never", 0));

        HashMap<String, Integer> ans3ToCO2 = new HashMap<>(Map.of(
                "Daily", 1450,
                "Frequently (3-5 times/week)", 860,
                "Occasionally (1-2 times/week)", 450,
                "Never", 0));

        HashMap<String, Integer> ans4ToCO2 = new HashMap<>(Map.of(
                "Daily", 950,
                "Frequently (3-5 times/week)", 600,
                "Occasionally (1-2 times/week)", 200,
                "Never", 0));

        HashMap<String, Integer> ans5ToCO2 = new HashMap<>(Map.of(
                "Daily", 800,
                "Frequently (3-5 times/week)", 500,
                "Occasionally (1-2 times/week)", 150,
                "Never", 0));

        HashMap<String, Float> ans6ToCO2 = new HashMap<>(Map.of(
                "Never", 0,
                "Rarely", 23.4,
                "Occasionally", 70.2,
                "Frequently", 140.4));

        total += ans1ToCO2.get(ans1);
        total += ans2ToCO2.get(ans2);
        total += ans3ToCO2.get(ans3);
        total += ans4ToCO2.get(ans4);
        total += ans5ToCO2.get(ans5);
        total += ans6ToCO2.get(ans6);

        return total / 1000;

    }
}