package com.duantn.configs;

<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
=======
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
>>>>>>> 6167dc0c9113ce285e6d0f0142867a23dbe71d02
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

<<<<<<< HEAD
import com.duantn.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private CustomLoginSuccessHandler loginSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/photos/**", "/favicon.ico").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/giangvien/**").hasRole("GIANGVIEN")
                        .requestMatchers("/nhanvien/**").hasRole("NHANVIEN")
                        .requestMatchers("/hocvien/**").hasRole("HOCVIEN")
                        .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login/form")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(loginSuccessHandler)
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll());
        http.csrf(AbstractHttpConfigurer::disable);
=======
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/css/**", "/js/**", "/home", "/instructor/register").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login?logout"))
                .csrf(csrf -> csrf.disable()); // nếu dùng API hoặc để đơn giản dev

>>>>>>> 6167dc0c9113ce285e6d0f0142867a23dbe71d02
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
<<<<<<< HEAD
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
}
=======
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
>>>>>>> 6167dc0c9113ce285e6d0f0142867a23dbe71d02
