package org.microserives.demo.eurekaclientsecondcopy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class EurekaClientSecondCopyApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientSecondCopyApplication.class, args);
    }

}
