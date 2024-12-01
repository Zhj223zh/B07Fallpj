package com.example.b07fall2024;

public interface LoginContract {
    interface View {
        void showProgressBar();
        void hideProgressBar();
        void showSuccess(String message);
        void showError(String message);
        void jumpToEcotrackerActivity();
        void jumpToForgotPasswordActivity();
        void jumpToSignUpActivity();
    }

    interface Presenter {
        void login(String email, String password);
    }
}
