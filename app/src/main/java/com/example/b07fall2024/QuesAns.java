import java.util.Map;

public interface QuesAns {
    String getQuestionText(int questionIndex);

    Map<String,String> getOptions(int questionIndex);

    String getSelectedAnswer(int questionIndex);
    void setSelectedAnswer(int index,String answer);
    int getEnding_quiz_number();
    int options_size(int number);
}
