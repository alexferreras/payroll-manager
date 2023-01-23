package com.payroll.config;

import com.payroll.controller.request.PayrollUploadRequest;
import java.util.Collections;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserDetailConfig {

    final String username;
    final  String password;

    public UserDetailConfig(@Value("${api.user.username}") String username,@Value("${api.user.password}") String password) {
        this.username = username;
        this.password = password;
    }

    public void authenticate(PayrollUploadRequest payrollUploadRequest) {
        if (!isValidCredential(payrollUploadRequest)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        Authentication
            auth = new UsernamePasswordAuthenticationToken(payrollUploadRequest.getUsername(),
            payrollUploadRequest.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private boolean isValidCredential(PayrollUploadRequest payrollUploadRequest) {
        return username.equals(payrollUploadRequest.getUsername()) &&
            password.equals(payrollUploadRequest.getPassword());
    }
}
