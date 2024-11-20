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
        options2.put("A", "Beef");
        options2.put("B", "Pork");
        options2.put("C", "Chicken");
        options2.put("D", "Fish/Seafood");
        options.add(options2);

        Map<String, String> options3 = new HashMap<>();
        options3.put("A", "Never");
        options3.put("B", "Rarely");
        options3.put("C", "Occasionally");
        options3.put("D", "Frequently");
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
}