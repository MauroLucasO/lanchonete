package br.edu.ifsudestemg.lanchonete.config;

import br.edu.ifsudestemg.lanchonete.security.JwtAuthFilter;
import br.edu.ifsudestemg.lanchonete.security.JwtService;
import br.edu.ifsudestemg.lanchonete.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter() {
        return new JwtAuthFilter(jwtService, usuarioService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(usuarioService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configure(http))
                .cors().disable()
                .csrf().disable()
                .authorizeRequests()

                .antMatchers("/api/v1/usuarios/**")
                .permitAll()

                .antMatchers("/api/v1/clientes/**")
                .hasRole("ADMIN")

                .antMatchers("/api/v1/produtos/**")
                .permitAll()

                .antMatchers("/api/v1/categorias/**")
                .hasRole("ADMIN")

                .antMatchers("/api/v1/cupons/**")
                .hasRole("ADMIN")

                .antMatchers("/api/v1/pedidos/**")
                .hasRole("ADMIN")

                .antMatchers("/api/v1/estabelecimentos/**")
                .hasRole("ADMIN")

                .antMatchers("/api/v1/gerentes/**")
                .hasRole("ADMIN")

                .antMatchers("/api/v1/estoques/**")
                .hasRole("ADMIN")

                .antMatchers("/api/v1/itenspedidos/**")
                .hasRole("ADMIN")

                .antMatchers("/api/v1/proprietarios/**")
                .hasRole("ADMIN")

                .anyRequest()
                .authenticated()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"
        );
    }
}