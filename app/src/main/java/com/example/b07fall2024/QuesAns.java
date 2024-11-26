package com.example.b07fall2024;

import java.util.Map;

public interface QuesAns {
    String getQuestionText(int questionIndex);

    Map<String,String> getOptions(int questionIndex);

    String getSelectedAnswer(String question);
    void setSelectedAnswer(String question,String answer);
    boolean isValidOption(int questionIndex,String answer);
     int questionTextSize();

}

