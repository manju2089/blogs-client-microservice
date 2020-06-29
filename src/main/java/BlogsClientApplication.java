/**
 * Created by manju on 28-06-2020.
 */


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableEurekaClient
@EnableJpaRepositories
@EntityScan("blogs.microservice")
@ComponentScan({ "blogs.microservice" })

public class BlogsClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogsClientApplication.class, args);
    }

}
