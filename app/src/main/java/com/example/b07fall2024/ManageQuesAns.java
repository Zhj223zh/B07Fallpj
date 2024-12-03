package com.example.b07fall2024;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageQuesAns implements Parcelable{
    private final Map<QuesAns, Map<String, String>> answersByCategory;
    private String country;

    public ManageQuesAns(List<QuesAns> questionBanks) {
        country = null;
        answersByCategory = new HashMap<>();
        for (QuesAns quesAns : questionBanks) {
            answersByCategory.put(quesAns, new HashMap<>());
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

    public void setAnswer(QuesAns quesAns, int questionIndex, String answerKey) {
        if (quesAns != null) {
            if (answersByCategory.containsKey(quesAns)) {
                String question = quesAns.getQuestionText(questionIndex);
                Map<String, String> options = quesAns.getOptions(questionIndex);
                if(options.containsKey(answerKey)){
                    String answerValue = options.get(answerKey);
                    answersByCategory.get(quesAns).put(question, answerValue);
                    System.out.println("The question is: " + question + " and the answer is: " + answerValue);
                    quesAns.setSelectedAnswer(question, answerKey);
                }
                else {
                    throw new QuestionException("Invalid answer key: " + answerKey);
                }
            } else {
                throw new QuestionException("QuesAns object not found in the category map.");
            }
        } else {
            throw new QuestionException("Invalid QuesAns object.");
        }
    }

    public String getAnswer(QuesAns quesAns, int questionIndex) {
        if (quesAns != null) {
            if (answersByCategory.containsKey(quesAns)) {
                String question = quesAns.getQuestionText(questionIndex);
                Map<String, String> answers = answersByCategory.get(quesAns);
                if (answers != null && question != null && answers.containsKey(question)) {
                    return answers.get(question);
                } else {
                    throw new QuestionException("Question index does not exist.");
                }
            } else {
                throw new QuestionException("QuesAns object not found in the category map.");
            }
        } else {
            throw new QuestionException("QuesAns object cannot be null.");
        }
    }

    // New method to determine if a question should be skipped
    public boolean shouldSkipQuestion(QuesAns quesAns, int questionIndex) {
        if (quesAns instanceof QuizTransportation) {
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


    private String getSelectedAnswerByQuestion(QuesAns quesAns, String questionText) {
        Map<String, String> answers = answersByCategory.get(quesAns);
        return answers != null ? answers.get(questionText) : null;
    }

    public HashMap<String, Float> getEmissionsByCategory() {
        ArrayList<QuesAns> quesAnsList = new ArrayList<QuesAns>();
        for (QuesAns quesAns: answersByCategory.keySet()){
            quesAnsList.add(quesAns);
        }

        HashMap<String, Float> EmissionsByCategory = new HashMap<>(Map.of(
                "transportation", quesAnsList.get(0).getEmissions(),
                "food", quesAnsList.get(1).getEmissions(),
                "housing", quesAnsList.get(2).getEmissions(),
                "consumption", quesAnsList.get(3).getEmissions()));
        return EmissionsByCategory;
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


