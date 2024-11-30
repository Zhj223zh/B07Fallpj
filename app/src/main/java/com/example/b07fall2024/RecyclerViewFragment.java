
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.widget.Button;
import android.widget.Toast;


public class RecyclerViewFragment extends Fragment implements View.OnClickListener {

    // UI elements
    TextView totalQuestionsTextView;
    TextView questionTextview;
    Button submitBtn;
    private final Consumption consumption = new Consumption();
    private int currentQuestionIndex = 0;
    private String selectedAnswer = "";

    // Total number of questions in the quiz
    private int totalQuestions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        // Initialize your UI elements here
        totalQuestionsTextView = view.findViewById(R.id.total_question);
        questionTextview = view.findViewById(R.id.question);
        submitBtn = view.findViewById(R.id.submit_btn);

        // Set listeners for the buttons
        submitBtn.setOnClickListener(this);

        // Get the total number of questions
        totalQuestions = consumption.getEnding_quiz_number();
        totalQuestionsTextView.setText("Total questions: " + totalQuestions);

        // Load the first question
        loadNewQuestion(view);

        return view;
    }

    // Method to load a new question
    public void loadNewQuestion(View view) {
        if (currentQuestionIndex >= totalQuestions) {
            finishQuiz();
            return;
        }

        // Get the current question and options from the consumption object
        String question = consumption.getQuestionText(currentQuestionIndex);
        Map<String, String> options = consumption.getOptions(currentQuestionIndex);

        // Update the UI with the new question
        questionTextview.setText(question);

        // Dynamically create and add buttons for the options
        LinearLayout answersContainer = view.findViewById(R.id.answers_container);
        if (answersContainer != null) {
            answersContainer.removeAllViews(); // Clear previous options

            // Dynamically create buttons for each option
            for (Map.Entry<String, String> entry : options.entrySet()) {
                Button button = createOptionButton(entry);
                answersContainer.addView(button);
            }
        }
    }

    // Create a button for each option
    private Button createOptionButton(Map.Entry<String, String> entry) {
        Button button = new Button(getContext());
        button.setText(entry.getValue()); // Set the option text
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        // Set the onClickListener for each option button
        button.setOnClickListener(v -> {
            selectedAnswer = entry.getValue(); // Store the selected answer
            consumption.setSelectedAnswer(currentQuestionIndex, selectedAnswer);

            // Clear button highlights and highlight the selected one
            clearButtonHighlights();
            button.setBackgroundColor(Color.MAGENTA); // Highlight the selected button
        });

        return button;
    }

    // Reset the background color of all answer buttons
    private void clearButtonHighlights() {
        LinearLayout answersContainer = getView().findViewById(R.id.answers_container);
        if (answersContainer != null) {
            for (int i = 0; i < answersContainer.getChildCount(); i++) {
                View child = answersContainer.getChildAt(i);
                if (child instanceof Button) {
                    child.setBackgroundColor(Color.WHITE); // Reset to default color
                }
            }
        }
    }

    // Handle submit button click
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit_btn) {
            // Ensure an answer is selected before proceeding
            if (selectedAnswer.isEmpty()) {
                Toast.makeText(getContext(), "Please select an answer!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Move to the next question
            currentQuestionIndex++;
            loadNewQuestion(getView()); // Reload the next question
        }
    }

    // Finish the quiz and show a dialog to restart
    private void finishQuiz() {
        submitData();
        new AlertDialog.Builder(getActivity())
                .setMessage("Quiz Finished!")
                .setPositiveButton("Restart", (dialog, which) -> restartQuiz())
                .setCancelable(false)
                .show();

    }

    private void submitData(){
        float transportation, food, housing, consumption; //TODO: fill in
        mDatabase.child("Users").child(userId).child("AnnualCF").child("Transportation").setValue(transportation);
        mDatabase.child("Users").child(userId).child("AnnualCF").child("Food").setValue(food);
        mDatabase.child("Users").child(userId).child("AnnualCF").child("Housing").setValue(housing);
        mDatabase.child("Users").child(userId).child("AnnualCF").child("Consmption").setValue(consumption);
    }

    // Restart the quiz by resetting the question index
    private void restartQuiz() {
        currentQuestionIndex = 0;
        loadNewQuestion(getView()); // Reload the first question
    }
}
//    Food food = new Food();
//    Housing housing = new Housing();
//    Transportation transportation = new Transportation();
//
//    List<QuesAns> list = new ArrayList<>();

//    int totalQuestion = consumption.getEnding_quiz_number();
//    int currentQuestionIndex = 1;
//    String selectedAnswer = "";
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
//
//        totalQuestionsTextView = view.findViewById(R.id.total_question);
//        questionTextView = view.findViewById(R.id.question);
//
//        submitBtn = view.findViewById(R.id.submit_btn);
//        submitBtn.setOnClickListener(this);
//        String totalquestion = "Total questions: " + totalQuestion;
//
//        totalQuestionsTextView.setText(totalquestion);
//
//        loadNewQuestion();
//
//        return view;
//    }
//
//    @Override
//    public void onClick(View view) {
//        if(view.getId() == R.id.submit_btn) {
//            // Check if an answer is selected before moving to the next question
//            if (selectedAnswer.isEmpty()) {
//                Toast.makeText(getContext(), "Please select an answer!", Toast.LENGTH_SHORT).show();
//                return;  // Do not proceed to the next question if no answer is selected
//            }
//
//            // Move to the next question after selection
//            currentQuestionIndex++;
//            loadNewQuestion();
//        } else if (view instanceof Button) {
//            Button clickedButton = (Button) view;
//            selectedAnswer = clickedButton.getText().toString();
//            consumption.setSelectedAnswer(currentQuestionIndex, selectedAnswer);
//
//            clearButtonHighlights();
//            clickedButton.setBackgroundColor(Color.MAGENTA); // Highlight the selected button
//        }
//    }
//    private void clearButtonHighlights(){
//        LinearLayout answersContainer = requireView().findViewById(R.id.answers_container);
//        if(answersContainer != null) {
//            for (int i = 0; i < answersContainer.getChildCount(); i++) {
//                View child = answersContainer.getChildAt(i);
//                if (child instanceof Button) {
//                    child.setBackgroundColor(Color.WHITE);
//                }
//            }
//        }
//    }
//
//
//    private void loadNewQuestion(){
//
//        if (currentQuestionIndex > totalQuestion) {
//            finishQuiz();
//            return;
//        }
//
//        String question = consumption.getQuestionText(currentQuestionIndex);
//        Map<String, String> options = consumption.getOptions(currentQuestionIndex);
//
//        questionTextView.setText(question);
//
//        LinearLayout linearLayout = requireView().findViewById(R.id.answers_container);
//        if (linearLayout != null) {
//            linearLayout.removeAllViews(); // Clear previous options
//
//            // Dynamically create buttons for each option
//            for (Map.Entry<String, String> entry : options.entrySet()) {
//                Button button = createOptionButton(entry);
//                linearLayout.addView(button);
//            }
//
//        }
//    }
//    private Button createOptionButton(Map.Entry<String, String> entry) {
//        Button button = new Button(getContext());
//        String text = entry.getValue(); // Only display the answer
//        button.setText(text);
//        button.setLayoutParams(new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT));
//
//        // Set the onClickListener for each button
//        button.setOnClickListener(v -> {
//            // When a button is clicked, update the selected answer
//            selectedAnswer = entry.getValue(); // Store the selected answer
//            consumption.setSelectedAnswer(currentQuestionIndex, selectedAnswer);
//
//            // Clear button highlights and highlight the selected one
//            clearButtonHighlights();
//            button.setBackgroundColor(Color.MAGENTA); // Highlight the selected button
//        });
//
//        return button;
//    }
//
//
//    void finishQuiz(){
//        new AlertDialog.Builder(getActivity())
//                .setPositiveButton("Restart", (dialogInterface, i) -> restartQuiz())
//                .setCancelable(false)
//                .show();
//
//    }
//
//    void restartQuiz(){
//        currentQuestionIndex = 1;
//        loadNewQuestion();
//    }


