package io.f12.notionlinkedblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NotionLinkedBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotionLinkedBlogApplication.class, args);
	}

}
