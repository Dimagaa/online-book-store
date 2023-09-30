package com.app.onlinebookstore.config;

import com.app.onlinebookstore.model.Role;
import com.app.onlinebookstore.model.User;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

@RequiredArgsConstructor
@TestConfiguration
public class SpringSecurityTestConfig {

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        Role userRole = new Role(Role.RoleName.ROLE_USER);

        User harry = new User();
        harry.setId(1L);
        harry.setEmail("harry@example.com");
        harry.setPassword("$2a$10$eNDxAZLdjj7A42/3t6t/lu9YigNljFK2bk9HSabaM3wGEYMv.6dYK");
        harry.setFirstName("Harry");
        harry.setLastName("Potter");
        harry.setShippingAddress("4 Privet Drive, Little Whinging, England");
        harry.setRoles(Set.of(userRole));

        User frodo = new User();
        frodo.setId(2L);
        frodo.setEmail("frodo@example.com");
        frodo.setPassword("$2a$10$eNDxAZLdjj7A42/3t6t/lu9YigNljFK2bk9HSabaM3wGEYMv.6dYK");
        frodo.setFirstName("Frodo");
        frodo.setLastName("Baggins");
        frodo.setShippingAddress("Bag End, Hobbiton, The Shire");
        frodo.setRoles(Set.of(userRole));

        Role adminRole = new Role(Role.RoleName.ROLE_ADMIN);

        User admin = new User();
        admin.setId(3L);
        admin.setEmail("admin@example.com");
        admin.setPassword("$2a$10$eNDxAZLdjj7A42/3t6t/lu9YigNljFK2bk9HSabaM3wGEYMv.6dYK");
        admin.setFirstName("Super");
        admin.setLastName("Admin");
        admin.setShippingAddress("hidden");
        admin.setRoles(Set.of(adminRole));

        Map<String, User> users = Map.of(harry.getEmail(), harry,
                frodo.getEmail(), frodo,
                admin.getEmail(), admin
        );
        return users::get;
    }
}
