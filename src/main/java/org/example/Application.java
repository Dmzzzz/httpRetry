package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.service.MyService2;
import org.example.service.MyService3;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Application.class);

        MyService3 bean = run.getBean(MyService3.class);

            bean.send();


        while (true) {


        }
    }
}