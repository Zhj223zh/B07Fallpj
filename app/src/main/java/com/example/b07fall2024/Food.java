import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Food  implements QuesAns{
    private final Map<Integer,String> questionText;
    private final List<Map<String,String>> options;
    private final Map<Integer,String> selectedAnswer;
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
    public Map<String,String> getOptions(int questionIndex) {
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
    public float getEmissions(){
        float total = 0;

        String ans1 = getSelectedAnswer(1);
        String ans2 = getSelectedAnswer(2);
        String ans3 = getSelectedAnswer(3);
        String ans4 = getSelectedAnswer(4);
        String ans5 = getSelectedAnswer(5);
        String ans6 = getSelectedAnswer(6);

        HashMap<String, int> ans1ToCO2 = Map.of(
            "Vegetarian", 1000,
            "Vegan", 500,
            "Pescatarian (fish/seafood)", 1500,
            "Meat-based (eat all types of animal products)", 0
        );

        HashMap<String, int> ans2ToCO2 = Map.of(
            
        );

        total += ans1ToCO2.get(ans1);

    }
}