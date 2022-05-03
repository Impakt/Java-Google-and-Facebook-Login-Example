package com.impakt.social.login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.impakt.social.login.servlet.SocialLoginServlet;

@SpringBootApplication
public class JavaSocialSignIn {

    public static void main( String[] args ) {
        SpringApplication.run( JavaSocialSignIn.class, args );
    }

    @Bean
    public ServletRegistrationBean<SocialLoginServlet> servletRegistrationBean() {
        return new ServletRegistrationBean<>( new SocialLoginServlet(), "/social-login" );
    }

}
