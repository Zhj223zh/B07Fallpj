package com.example.b07fall2024;

import androidx.annotation.NonNull;

import com.example.b07fall2024.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.function.Consumer;

public class LoginModel {
    private FirebaseAuth mAuth;
    private static LoginModel instance;
    private DatabaseReference userRef;// 初始化 Firebase 数据库引用

    public LoginModel() {
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();
    }

    public static LoginModel getInstance() {
        if (instance == null)
            instance = new LoginModel();
        return instance;
    }

    public void getUser(String userID, Consumer<User> callback) {
        userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                callback.accept(user);
            }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) {}
        });
    }

    public void authenticate(String email, String password, Consumer<
            FirebaseUser> callback) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    callback.accept(null);
                }
                else {
                    System.out.println("hhhh");
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    System.out.println(currentUser.getUid());
                    callback.accept(currentUser);
//                    userRef.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                        //System.out.println("aaaaaa");
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            System.out.println("wwwww");
//                            User user = snapshot.getValue(User.class);
//                            callback.accept(user);
//                        }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
                }
            }
        });
    }

    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }
}

//    public interface LoginCallback {
//        void onSuccess();
//        void onFailure(String errorMessage);
//    }
//
//    public void loginUser(String email, String password, LoginCallback callback) {
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>()  {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            callback.onSuccess();
//                        } else {
//                            String error = task.getException() != null ? task.getException().getMessage() : "Unknown error";
//                            callback.onFailure(error);
//                        }
//                    }
//                });
//    }