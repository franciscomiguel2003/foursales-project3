package br.com.foursales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "br.com.foursales.elasticsearch.dao")
@EnableJpaRepositories(basePackages = "br.com.foursales.dao")
public class FoursalesApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoursalesApplication.class, args);
    }
}
