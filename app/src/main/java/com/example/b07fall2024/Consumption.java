import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Consumption implements QuesAns {
    private final Map<Integer, String> questionText;
    private final List<Map<String, String>> options;
    private final Map<Integer, String> selectedAnswer;

    private final int starting_quiz_number;
    private final int ending_quiz_number;

    public Consumption() {
        questionText = new HashMap<>();
        options = new ArrayList<>();
        selectedAnswer = new HashMap<>();
        starting_quiz_number = 1;
        ending_quiz_number = 4;

        // Add questions
        questionText.put(1, "How often do you buy new clothes");
        questionText.put(2, "Do you buy second-hand or eco-friendly products?");
        questionText.put(3, "How many electronic devices (phones, laptops, etc.) have you purchased in the past year?");
        questionText.put(4, "How often do you recycle?");

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
    public int getEnding_quiz_number(){
        return ending_quiz_number;
    }
    @Override
    public int options_size(int number){
        return options.get(number).size();
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
}
