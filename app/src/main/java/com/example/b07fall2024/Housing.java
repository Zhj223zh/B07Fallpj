import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Housing implements QuesAns {
    private final Map<Integer, String> questionText;
    private final List<Map<String, String>> options;
    private final Map<Integer, String> selectedAnswer;
    private final int starting_quiz_number;
    private final int ending_quiz_number;

    public Housing() {
        questionText = new HashMap<>();
        options = new ArrayList<>();
        selectedAnswer = new HashMap<>();
        starting_quiz_number = 1;
        ending_quiz_number = 7;

        // Adding questions
        questionText.put(1, "What type of home do you live in?");
        questionText.put(2, "How many people live in your household?");
        questionText.put(3, "What is the size of your home?");
        questionText.put(4, "What type of energy do you use to heat your home?");
        questionText.put(5, "What is your average monthly electricity bill?");
        questionText.put(6, "What type of energy do you use to heat water in your home?");
        questionText.put(7, "Do you use any renewable energy sources for electricity or heating (e.g., solar, wind)?");

        // Adding options
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

    public float getEmissions(){
        String homeType = getSelectedAnswer(1);
        String householdSize = getSelectedAnswer(2);
        String houseSize = getSelectedAnswer(3);
        String heatingType = getSelectedAnswer(4);
        String energyBill = getSelectedAnswer(5);
        String waterType = getSelectedAnswer(6);
        String renewables = getSelectedAnswer(7);

        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("/Users/User/Desktop/course.json"));
            JSONObject jsonObject = (JSONObject)obj;
            //TODO: figure out how to get the four options as json objects
          
        } catch(Exception e) {
            e.printStackTrace();
        }

        float total;
        HashMap<String, <String, <String, <String, int>>>> standin;
        float heatingCO2 = standin.get(houseSize.get(householdSize.get(energyBill.get(heatingType))));
        float waterCO2 = standin.get(houseSize.get(householdSize.get(energyBill.get(waterType))));

        total = total + heatingCO2 + waterCO2;
        if (heatingType != waterType){total += 233;}
        if (renewables == "Yes, primarily (more than 50% of energy use)"){total -= 6000;}
        if (renewables == "Yes, partially (less than 50% of energy use)"){total -= 4000;}

    }
}
