package example.rest.config;

import example.rest.domain.User;
import example.rest.repo.UserDetailsRepo;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.time.LocalDate;

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login**", "/js/**", "/error**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and().logout().logoutSuccessUrl("/").permitAll()
                .and().csrf().disable();
    }


    /**
     *Нужен для того ,чтобы после авторизации пользователя записать его в БД
     * @param userDetailsRepo
     * @return
     */
    @Bean
    public PrincipalExtractor principalExtractor(UserDetailsRepo userDetailsRepo) {
        return map -> {
            String id = (String) map.get("sub");
            User user = userDetailsRepo.findById(id).orElseGet(() -> {
                User newUser = new User();

                newUser.setId(id);
                newUser.setName((String) map.get("name"));
                newUser.setEmail((String) map.get("email"));
                newUser.setGender((String) map.get("gender"));
                newUser.setLocale((String) map.get("locale"));
                newUser.setUserPic((String) map.get("picture"));
                return newUser;
            });
            user.setLocalVisit(LocalDate.now());
            return userDetailsRepo.save(user).getId();
        };
    }
}
