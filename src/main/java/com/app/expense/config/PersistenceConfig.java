package com.app.expense.config;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import com.app.expense.entity.Categories;
import com.app.expense.repo.CategoryRepo;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAwareProvier")
public class PersistenceConfig {

	@Bean
	AuditorAware<String> auditorAwareProvier() {

		return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
				.map(auth -> auth.getName())
				.or(() -> Optional.of("System User"));

	}

	@Bean
	CommandLineRunner runner(CategoryRepo repo) {
		return _ -> {
			if (repo.count() == 0) {

				var names = List.of("Groceries", "Leisure", "Electronics", "Utilities", "Clothing", "Health" , "Others");

				names.forEach(name -> {
					var cat = new Categories();
					cat.setName(name);
					repo.save(cat);
				});
			}
		};
	}

}
