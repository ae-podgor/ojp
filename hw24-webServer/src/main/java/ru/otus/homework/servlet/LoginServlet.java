package ru.otus.homework.servlet;

import ru.otus.homework.services.UserAuthService;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@SuppressWarnings({"java:S1989"})
public class LoginServlet extends HttpServlet {

    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final int MAX_INACTIVE_INTERVAL = 30;

    private final transient UserAuthService userAuthService;

    public LoginServlet(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var name = request.getParameter(PARAM_LOGIN);
        var password = request.getParameter(PARAM_PASSWORD);

        if (userAuthService.authenticate(name, password)) {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            response.sendRedirect("/clients");
        } else {
            response.setStatus(SC_UNAUTHORIZED);
        }
    }
}
