package com.agagrzebyk.java.spring;

import com.agagrzebyk.java.security.MySavedRequestAwareAuthenticationSuccessHandler;
import com.agagrzebyk.java.security.MyUserDetailsService;
import com.agagrzebyk.java.security.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private MyUserDetailsService userDetailsService;
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler;

    @Autowired
    public SecurityConfig(MyUserDetailsService userDetailsService,
                          RestAuthenticationEntryPoint restAuthenticationEntryPoint,
                          MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.mySuccessHandler = mySuccessHandler;
    }

    private SimpleUrlAuthenticationFailureHandler myFailureHandler = new SimpleUrlAuthenticationFailureHandler();

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()

                .and()

                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)

                .and()

                .authorizeRequests()
                .antMatchers("/", "/course/all").permitAll()
               // .antMatchers().authenticated()
                .antMatchers("/admin/**", "/course/**").hasRole("ADMIN")
                .antMatchers("/student/**").hasAnyRole("STUDENT","ADMIN")

                .antMatchers("/api/test1").hasAuthority("ACCESS_TEST1")
                .antMatchers("/api/test2").hasAuthority("ACCESS_TEST2")
                .antMatchers("/api/users").hasRole("ADMIN")

                .and()

                .formLogin()
                .successHandler(mySuccessHandler)
                .failureHandler(myFailureHandler)

                .and()
                .httpBasic()
                .and()

                .logout();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
