package com.example.b07fall2024;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageQuesAns implements Parcelable {
    private final ArrayList<QuesAns> answersByCategory;
    private String country;

    public ManageQuesAns(List<QuesAns> questionBanks) {
        country = null;
        answersByCategory = new ArrayList<>();
        for (QuesAns quesAns : questionBanks) {
            answersByCategory.add(quesAns);
        }
    }

    public ManageQuesAns(ManageQuesAns mqa){
        answersByCategory = mqa.answersByCategory;
        country = null;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return this.country;
    }

    public String getQuestion(QuesAns quesAns, int questionIndex) {
        if (quesAns != null) {
            return quesAns.getQuestionText(questionIndex);
        } else {
            throw new QuestionException("Invalid QuesAns object or question index.");
        }
    }

    // Store an answer by category and question index
    public void storeAnswer(QuesAns quesAns, int questionIndex, String answer) {
        if (quesAns != null) {
            // Ensure the QuesAns object exists before adding the answer
            if (answersByCategory.contains(quesAns)) {
                quesAns.setSelectedAnswer(questionIndex, answer);
            } else {
                throw new QuestionException("QuesAns object not found in the category map.");
            }
        } else {
            throw new QuestionException("Invalid QuesAns object or question index.");
        }
    }

    public String getAnswer(QuesAns quesAns, int questionIndex) {
        if (quesAns != null) {
            // Ensure the QuesAns object exists before adding the answer
            if (answersByCategory.contains(quesAns)) {
                return quesAns.getSelectedAnswer(questionIndex);
            } else {
                throw new QuestionException("QuesAns object not found in the category map.");
            }
        } else {
            throw new QuestionException("QuesAns object cannot be null.");
        }
    }

    public HashMap<String, Float> getEmissionsByCategory() {
        HashMap<String, Float> EmissionsByCategory = new HashMap<>(Map.of(
                "transportation", answersByCategory.get(0).getEmissions(),
                "food", answersByCategory.get(1).getEmissions(),
                "housing", answersByCategory.get(2).getEmissions(),
                "consumption", answersByCategory.get(3).getEmissions()));
        return EmissionsByCategory;
    }

    public float getTotalEmissions() {
        float total = 0;
        for (QuesAns quesAns : answersByCategory) {
            total += quesAns.getEmissions();
        }
        return total;
    }


    // New method to determine if a question should be skipped
    public boolean shouldSkipQuestion(QuesAns quesAns, int questionIndex) {
        if (quesAns instanceof Transportation) {
            String questionText = quesAns.getQuestionText(questionIndex);
            String answer = getSelectedAnswerByQuestion(quesAns, "Do you own or regularly use a car?");

            // Enhanced Debug Print Statements
            System.out.println("Checking question: " + questionText);
            System.out.println("Answer to 'Do you own or regularly use a car?': " + answer);

            // Skip detailed car-related questions if the answer is "No"
            if ("No".equals(answer)) {
                System.out.println("Answer is 'No'. Checking if we should skip car-related questions.");
                if ("What type of car do you drive?".equals(questionText) ||
                        "How many kilometers/miles do you drive per year?".equals(questionText)) {
                    System.out.println("Skipping question: " + questionText);
                    return true;
                }
            }
        }
        if (quesAns instanceof Food) {
            String questionText = quesAns.getQuestionText(questionIndex);
            String answer = getSelectedAnswerByQuestion(quesAns, "What best describes your diet?");

            // Enhanced Debug Print Statements
            System.out.println("Checking question: " + questionText);
            System.out.println("Answer to What best describes your diet?': " + answer);

            // Skip detailed car-related questions if the answer is "No"
            if (!("Meat-based (eat all types of animal products)".equals(answer))) {
                System.out.println("Answer is 'not meat'. Checking if we should skip  meat related questions");
                if ("How often do you eat the following animal-based products?: Beef".equals(questionText)
                        || "How often do you eat the following animal-based products?: Pork".equals(questionText)
                        || "How often do you eat the following animal-based products?: Chicken".equals(questionText)
                        ||"How often do you eat the following animal-based products?: Fish/Seafood".equals(questionText)) {
                    System.out.println("Skipping question: " + questionText);
                    return true;
                }
            }
        }
        return false;
    }

    private int getAnswerIndex(QuesAns quesAns, String questionText){
        int i = 0;
        while(true){
            try{
                String currAnswer = quesAns.getQuestionText(i);
                if (currAnswer.equals(questionText)){
                    return i;
                }
            }
            catch (QuestionException e){
                break;
            }
            i++;
        }
        return -1;
    }
    private String getSelectedAnswerByQuestion(QuesAns quesAns, String questionText) {
        int questionIndex = getAnswerIndex(quesAns, questionText);
        return quesAns.getSelectedAnswer(questionIndex);

    }

    public int describeContents(){
        return 0;
    }

    public void writeToParcel (Parcel dest, int flags){
        dest.writeParcelable (this, 0);
    }

    public static final Parcelable.Creator<ManageQuesAns> CREATOR
            = new Parcelable.Creator<ManageQuesAns>() {
        public ManageQuesAns createFromParcel(Parcel in) {
            return new ManageQuesAns((ManageQuesAns)in.readParcelable(null));
        }
        public ManageQuesAns[] newArray(int size) {
            return new ManageQuesAns[size];
        }
    };

}


