
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ManageQuesAns {
    private final Map<QuesAns, Map<Integer, String>> answersByCategory;
    public ManageQuesAns(List<QuesAns> questionBanks) {
        answersByCategory = new HashMap<>();
        for (QuesAns quesAns : questionBanks) {
            answersByCategory.put(quesAns, new HashMap<>());
        }
    }

    public String getQuestion(QuesAns quesAns, int questionIndex) {
        if (quesAns != null) {
            return quesAns.getQuestionText(questionIndex);
        }
        else {
            throw new QuestionException("Invalid QuesAns object or question index.");
        }
    }

    // Store an answer by category and question index
    public void storeAnswer(QuesAns quesAns, int questionIndex, String answer) {
        if (quesAns != null ) {
            // Ensure the QuesAns object exists before adding the answer
            if (answersByCategory.containsKey(quesAns)) {
                Objects.requireNonNull(answersByCategory.get(quesAns)).put(questionIndex, answer);
                quesAns.setSelectedAnswer(questionIndex,answer);
            } else {
                throw new QuestionException("QuesAns object not found in the category map.");
            }
        }
        else {
            throw new QuestionException("Invalid QuesAns object or question index.");
        }
    }

    public String getAnswer(QuesAns quesAns, int questionIndex){
        if (quesAns != null ) {
            // Ensure the QuesAns object exists before adding the answer
            if (answersByCategory.containsKey(quesAns)) {
                if(answersByCategory.get(quesAns).containsKey(questionIndex)){
                    return answersByCategory.get(quesAns).get(questionIndex);
                }
                else{
                    throw new QuestionException("Question index does not exist");
                }
            }     
            else {
                throw new QuestionException("QuesAns object not found in the category map.");
            }
        }
        else {
            throw new QuestionException("QuesAns object cannot be null.");
        }
    }

    public float getTotalEmissions(){
        float total = 0;
        for (QuesAns quesAns: answersByCategory.getKeys()){
            total += quesAns.getEmissions();
        }
        return total;
    }

}
