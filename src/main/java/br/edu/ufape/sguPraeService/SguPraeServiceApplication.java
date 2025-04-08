package br.edu.ufape.sguPraeService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "br.edu.ufape.sguPraeService.auth")
public class SguPraeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SguPraeServiceApplication.class, args);
    }

}
