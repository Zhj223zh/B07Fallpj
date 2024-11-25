
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Transportation implements QuesAns{
    private final Map<Integer,String> questionText;
    private final List<Map<String,String>> options;
    private final Map<Integer,String> selectedAnswer;
    private final int starting_quiz_number;
    private final int ending_quiz_number;


    public Transportation(){
        //Initialize options and questionText array
        // questionText = new HashMap<>();
        // options = new ArrayList<>();
        // selectedAnswer = new HashMap<>();
        // starting_quiz_number = 1;
        // ending_quiz_number = 7;

        // questionText.put(1,"Do you own or regularly use a car?");
        // questionText.put(2,"What type of car do you drive?");
        // questionText.put(3,"How many kilometers/miles do you drive per year?");
        // questionText.put(4,"How often do you use public transportation (bus, train, subway)?");
        // questionText.put(5,"How much time do you spend on public transport per week (bus, train, subway)?");
        // questionText.put(6,"How many short-haul flights (less than 1,500 km / 932 miles) have you taken in the past year?");
        // questionText.put(7,"How many long-haul flights (more than 1,500 km / 932 miles) have you taken in the past year?");

        // Map<String,String> options1 = new HashMap<>();
        // options1.put("A","Yes");
        // options1.put("B","No");
        // options.add(options1);

        // Map<String,String> options2 = new HashMap<>();
        // options2.put("A","Gasoline");
        // options2.put("B","Diesel");
        // options2.put("C","Hybrid");
        // options2.put("D","Electric");
        // options2.put("E","I don’t know");
        // options.add(options2);

        // Map<String,String> options3 = new HashMap<>();
        // options3.put("A","Up to 5,000 km (3,000 miles)");
        // options3.put("B","5,000–10,000 km (3,000–6,000 miles)");
        // options3.put("C","10,000–15,000 km (6,000–9,000 miles)");
        // options3.put("D","15,000–20,000 km (9,000–12,000 miles)");
        // options3.put("E","20,000–25,000 km (12,000–15,000 miles)");
        // options3.put("F","More than 25,000 km (15,000 miles)");
        // options.add(options3);

        // Map<String,String> options4 = new HashMap<>();
        // options4.put("A","Never");
        // options4.put("B","Occasionally (1-2 times/week)");
        // options4.put("C","Frequently (3-4 times/week)");
        // options4.put("D","Always (5+ times/week)");
        // options.add(options4);

        // Map<String,String> options5 = new HashMap<>();
        // options5.put("A","Under 1 hour");
        // options5.put("B","1-3 hours");
        // options5.put("C","3-5 hours");
        // options5.put("D","5-10 hours");
        // options5.put("E","More than 10 hours");
        // options.add(options5);

        // Map<String,String> options6 = new HashMap<>();
        // options6.put("A","None");
        // options6.put("B","1-2 flights");
        // options6.put("C","3-5 flights");
        // options6.put("D","6-10 flights");
        // options6.put("E","More than 10 flights");
        // options.add(options6);

        // Map<String,String> options7 = new HashMap<>();
        // options7.put("A","None");
        // options7.put("B","1-2 flights");
        // options7.put("C","3-5 flights");
        // options7.put("D","6-10 flights");
        // options7.put("E","More than 10 flights");
        // options.add(options7);

        //TODO: refactor to fetch as json file from database
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
        String ans7 = getSelectedAnswer(7);

        if ans1 == "No":
            return 0;

        HashMap<String, float> ans2ToCO2 = Map.of(
            "Gasoline", 0.24,
            "Diesel", 0.27,
            "Hybrid", 0.16,
            "Electric", 0.05
        );
        HashMap<String, int> ans3ToMiles = Map.of(
            "Up to 5,000 km (3,000 miles)", 5000,
            "5,000–10,000 km (3,000–6,000 miles)", 10000,
            "10,000–15,000 km (6,000–9,000 miles)", 15000,
            "15,000–20,000 km (9,000–12,000 miles)", 20000,
            "20,000–25,000 km (12,000–15,000 miles)", 25000,
            "More than 25,000 km (15,000 miles)", 35000
        );

        //Answers 4 and 5
        HashMap<String, int> ans5Occasionally = Map.of(
            "Under 1 hour", 246,
            "1-3 hours", 819,
            "3-5 hours", 1638,
            "5-10 hours", 3071,
            "More than 10 hours", 9555
        );
        HashMap<String, int> ans5Frequently = Map.of(
            "Under 1 hour", 573,
            "1-3 hours",1911,
            "3-5 hours", 3822,
            "5-10 hours", 7166,
            "More than 10 hours", 9555
        );

        HashMap<String, int> ans4ToCO2 = Map.of(
            "Never", 0;
            "Occasionally", ans5Occasionally.get(ans5);
            "Frequently", ans5Frequently.get(ans5);
            "Always", ans5Frequently.get(ans5);
        )

        //answer 6
        HashMap<String, int> ans6ToCO2 = Map.of(
            "None", 0,
            "1-2 flights", 225,
            "3-5 flights", 600,
            "6-10 flights", 1200,
            "More than 10 flights", 1800
        );

        //answer 7
        HashMap<String, int> ans7ToCO2 = Map.of(
            "None", 0,
            "1-2 flights", 825,
            "3-5 flights", 2200,
            "6-10 flights", 4400,
            "More than 10 flights", 6600
        );

        int kgPerKm = ans2ToCO2.get(ans2);
        int milesDriven = ans3ToMiles.get(ans3);

        total += kgPerKm * milesDriven;
        total += ans4ToCO2.get(ans4);
        total += ans6ToCO2.get(ans6);
        total += ans7ToCO2.get(ans7); 

        return total;       
    }

}
