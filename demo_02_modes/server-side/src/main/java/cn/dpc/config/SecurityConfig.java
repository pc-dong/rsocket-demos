package cn.dpc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;

@Configuration
@EnableRSocketSecurity
public class SecurityConfig {

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        final UserDetails user = User
                .withUsername("jdoe")
                .password("{noop}rsocket")
                .roles("USER")
                .build();
        return new MapReactiveUserDetailsService(user);
    }

    @Bean
    public PayloadSocketAcceptorInterceptor authorization(RSocketSecurity rsocket) {
        return rsocket
                .authorizePayload(authorize -> authorize
                        .route("file.upload").authenticated()
                        .anyExchange().permitAll())
                .simpleAuthentication(Customizer.withDefaults())
                .build();
    }
}
