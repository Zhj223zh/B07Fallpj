package com.example.b07fall2024;

import static org.mockito.Mockito.*;

import com.example.b07fall2024.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {
    @Mock
    //private LoginContract.View mockView;
    private MainActivity mockView;

    @Mock
    private LoginModel mockModel;

    private LoginPresenter loginPresenter;

    @Before
    public void setUp() {
        mockModel = mock(LoginModel.class);
        mockView = mock(MainActivity.class);
        loginPresenter = new LoginPresenter(mockModel, mockView);
    }


    @Test
    public void testLoginWithEmptyEmail() {
        // 测试当 email 为空时
        loginPresenter.login("", "123456");

        // 验证 View 方法是否被正确调用
        verify(mockView).hideProgressBar();
        verify(mockView).showError("Please enter email");
        verifyNoMoreInteractions(mockView); // 确保没有其他方法被调用
    }

    @Test
    public void testLoginWithEmptyPassword() {
        // 测试当 password 为空时
        loginPresenter.login("user@example.com", "");

        // 验证 View 方法是否被正确调用
        verify(mockView).hideProgressBar();
        verify(mockView).showError("Please enter password");
        verifyNoMoreInteractions(mockView);
    }

    @Test//这个是我直接复制粘贴的张给的summer code，记得改
    public void testLoginSuccess() {
        User testUser = new User("thomas", "123");

        Task<User> mockedTask = mock(Task.class);
        when(loginModelMock.login("thomas", "123")).thenReturn(mockedTask);

        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                OnCompleteListener<User> listener = invocation.getArgument(0, OnCompleteListener.class);
                listener.onComplete(mockedTask);
                return null;
            }
        }).when(mockedTask).addOnCompleteListener(any());

        when(mockedTask.getResult()).thenReturn(testUser);

        loginPresenter.onButtonClick("thomas", "123");

        verify(loginViewMock).onSuccess();
    }

    @Test
    public void testLoginSuccess() {
        // 模拟 Model 的登录成功回调
        doAnswer(invocation -> {
            LoginModel.LoginCallback callback = invocation.getArgument(2);
            callback.onSuccess();
            return null;
        }).when(mockModel).loginUser(eq("user@example.com"), eq("123456"), any());

        // 调用 Presenter 的登录方法
        loginPresenter.login("user@example.com", "123456");

        // 验证 Model 的方法是否被调用
        verify(mockModel).loginPresenter(eq("user@example.com"), eq("123456"), any());

        // 验证 View 的成功显示和导航方法是否被调用
        verify(mockView).hideProgressBar();
        verify(mockView).showSuccess("Login Successful!");
        verify(mockView).jumpToDashboard(currentUser);
    }

    @Test
    public void testLoginFailure() {
        // 模拟 Model 的登录失败回调
        doAnswer(invocation -> {
            LoginModel.LoginCallback callback = invocation.getArgument(2);
            callback.onFailure("Invalid credentials");
            return null;
        }).when(mockModel).loginUser(eq("user@example.com"), eq("wrongpassword"), any());

        // 调用 Presenter 的登录方法
        presenter.login("user@example.com", "wrongpassword");

        // 验证 Model 的方法是否被调用
        verify(mockModel).loginUser(eq("user@example.com"), eq("wrongpassword"), any());

        // 验证 View 的失败显示方法是否被调用
        verify(mockView).hideProgressBar();
        verify(mockView).showError("Invalid credentials");
    }
}
