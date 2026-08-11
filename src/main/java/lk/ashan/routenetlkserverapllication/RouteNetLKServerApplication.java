package lk.ashan.routenetlkserverapllication;

import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.security.CustomUserPrincipal;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@SpringBootApplication
//@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class RouteNetLKServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RouteNetLKServerApplication.class, args);
	}

}
