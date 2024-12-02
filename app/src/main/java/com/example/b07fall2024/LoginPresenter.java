package com.example.b07fall2024;

import com.example.b07fall2024.model.User;
import com.google.firebase.auth.FirebaseUser;

public class LoginPresenter implements LoginContract.Presenter{
    //private final LoginContract.View view;
    final LoginModel model;
    private MainActivity view;

    public LoginPresenter(LoginModel model, MainActivity view) {
        this.model = model;
        this.view = view;
    }

//    public boolean isUserLoggedIn() {//其实我也不知道这是干啥的，没用上啊
//        return model.isUserLoggedIn();
//    }

    @Override
    public void login(String email, String password) {
        model.authenticate(email, password, (FirebaseUser user) -> {
            if (user == null) {
                view.failedToLogin();
            } else {
                model.getUser(user.getUid(), (User currentUser) -> {
                    if (currentUser == null) {
                        view.failedToLogin();
                    } else if (!currentUser.isQuestionsCompleted) {
                        view.jumpToQuestionsActivity(user);
                    }else {
                        view.jumpToDashboard(user);
                    }
                });
            }
        });
    }

//    @Override
//    public void login(String email, String password) {
//        model.authenticate(email, password, (
//                FirebaseUser user) -> {
//            if (user == null) view.failedToLogin();
//            else if (!currentUser.isQuestionsCompleted){view.jumpToQuestionsActivity();}
//            else view.jumpToDashboard(user);
//        });
//    }
}
