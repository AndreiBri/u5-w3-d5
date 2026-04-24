package andreibri.u5_w3_d5.config;

import andreibri.u5_w3_d5.security.JwtFilter;
import andreibri.u5_w3_d5.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    // 1. PASSWORD ENCODER — dice a Spring come hashare e confrontare le password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. AUTH MANAGER — usato nel login per avviare il processo di autenticazione
    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 3. AUTH PROVIDER — qui era il tuo bug!
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Passa lo userDetailsService direttamente nel costruttore
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        // Imposta solo il passwordEncoder separatamente
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // 4. SECURITY FILTER CHAIN — definisce chi può accedere a cosa
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()                        // login e register sono pubblici
                .requestMatchers("/events/**").hasRole("ORGANIZER")             // solo gli organizzatori
                .requestMatchers("/bookings/**").hasAnyRole("USER", "ORGANIZER") // utenti e organizzatori
                .anyRequest().authenticated()                                   // tutto il resto richiede login
        );

        // Registra il nostro auth provider
        http.authenticationProvider(authenticationProvider());

        // Aggiunge il filtro JWT PRIMA del filtro standard di Spring
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}