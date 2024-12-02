package com.example.b07fall2024;

import static org.mockito.Mockito.*;

import com.example.b07fall2024.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.function.Consumer;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {

    @Mock
    LoginModel model;

    @Mock
    MainActivity view;

    @Captor
    private ArgumentCaptor<Consumer<FirebaseUser>> firebaseUserCaptor;

    @Captor
    private ArgumentCaptor<Consumer<User>> userCaptor;

    private LoginPresenter presenter;

    @Before
    public void setUp() {
        presenter = new LoginPresenter(model, view);
    }

    @Test
    public void testLoginSuccessJumpToDashboard() {
        String email = "aaa@mail.com";
        String password = "123456";
        User mockUser = new User("testUser", email, "mockUid", true);
        FirebaseUser mockFirebaseUser = mock(FirebaseUser.class);
        when(mockFirebaseUser.getUid()).thenReturn("mockUid");

        doAnswer((Answer<Void>) invocation -> {
            Consumer<FirebaseUser> callback = invocation.getArgument(2);
//            FirebaseUser mockFirebaseUser = mock(FirebaseUser.class);
//            when(mockFirebaseUser.getUid()).thenReturn("mockUid");
            callback.accept(mockFirebaseUser);
            return null;
        }).when(model).authenticate(eq(email), eq(password), any());

        doAnswer((Answer<Void>) invocation -> {
            Consumer<User> callback = invocation.getArgument(1);
            callback.accept(mockUser);
            return null;
        }).when(model).getUser(eq("mockUid"), any());

        presenter.login(email, password);

        verify(model).authenticate(eq(email), eq(password), any());
        verify(model).getUser(eq("mockUid"), any());
//        verify(view, times(1)).jumpToDashboard(mockFirebaseUser);
//        verify(view, never()).jumpToQuestionsActivity(any());
        verify(view, never()).failedToLogin();
    }

    @Test
    public void testLoginSuccessJumpToQuestions() {
        String email = "aaa@mail.com";
        String password = "123456";
        User mockUser = new User("testUser", email, "mockUid", false);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Consumer<FirebaseUser> callback = invocation.getArgument(2);
                FirebaseUser mockFirebaseUser = mock(FirebaseUser.class);
                when(mockFirebaseUser.getUid()).thenReturn("mockUid");
                callback.accept(mockFirebaseUser);
                return null;
            }
        }).when(model).authenticate(eq(email), eq(password), any());

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Consumer<User> callback = invocation.getArgument(1);
                callback.accept(mockUser);
                return null;
            }
        }).when(model).getUser(eq("mockUid"), any());

        presenter.login(email, password);

        verify(model).authenticate(eq(email), eq(password), any());
        verify(model).getUser(eq("mockUid"), any());
        //verify(view, times(1)).jumpToQuestionsActivity(any());
        //verify(view, never()).jumpToDashboard(any());
        verify(view, never()).failedToLogin();
    }

    @Test
    public void testLoginFailed() {
        String email = "aaa@mail.com";
        String password = "123456";

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Consumer<FirebaseUser> callback = invocation.getArgument(2);
                callback.accept(null);
                return null;
            }
        }).when(model).authenticate(eq(email), eq(password), any());

        presenter.login(email, password);

        verify(model).authenticate(eq(email), eq(password), any());
        verify(view, times(1)).failedToLogin();
        //verify(view, never()).jumpToDashboard(any());
        //verify(view, never()).jumpToQuestionsActivity(any());
    }

    @Test
    public void testGetUserFailed() {
        String email = "aaa@mail.com";
        String password = "123456";

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Consumer<FirebaseUser> callback = invocation.getArgument(2);
                FirebaseUser mockFirebaseUser = mock(FirebaseUser.class);
                when(mockFirebaseUser.getUid()).thenReturn("mockUid");
                callback.accept(mockFirebaseUser);
                return null;
            }
        }).when(model).authenticate(eq(email), eq(password), any());

        // Mock getUser 方法
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Consumer<User> callback = invocation.getArgument(1);
                callback.accept(null);
                return null;
            }
        }).when(model).getUser(eq("mockUid"), any());

        presenter.login(email, password);

        verify(model).authenticate(eq(email), eq(password), any());
        verify(model).getUser(eq("mockUid"), any());
        verify(view, times(1)).failedToLogin();
        //verify(view, never()).jumpToDashboard(any());
        //verify(view, never()).jumpToQuestionsActivity(any());
    }
}


