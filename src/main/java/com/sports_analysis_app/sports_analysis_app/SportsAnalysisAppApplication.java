package com.sports_analysis_app.sports_analysis_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SportsAnalysisAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportsAnalysisAppApplication.class, args);
	}

}
