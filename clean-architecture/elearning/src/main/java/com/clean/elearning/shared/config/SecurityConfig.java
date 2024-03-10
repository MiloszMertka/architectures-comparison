package com.clean.elearning.shared.config;

import com.clean.elearning.shared.view.LoginFormView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@EnableWebSecurity
class SecurityConfig extends VaadinWebSecurity {

    private final UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.userDetailsService(userDetailsService);
        super.configure(http);
        setLoginView(http, LoginFormView.class);
    }

}
