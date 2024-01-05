package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.service.MyService2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Application.class);

        MyService2 bean = run.getBean(MyService2.class);

            bean.send();


        while (true) {


        }
    }
}