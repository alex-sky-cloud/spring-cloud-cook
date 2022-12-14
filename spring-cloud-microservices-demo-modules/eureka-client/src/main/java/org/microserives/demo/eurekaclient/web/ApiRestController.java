package org.microserives.demo.eurekaclient.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class ApiRestController {

    private final Environment environment;

    @Value("${spring.application.name}")
    private String nameService;


    @Value("${eureka.instance.instance-id}")
    private String instanceId;

    @GetMapping("id")
    public String getIdService(){

        return " ---- Instance Id service : " +
                instanceId +
                " ---- Port the application : " +
                environment.getProperty("local.server.port");
    }


    @GetMapping("info")
    public String getInfoSystemApplicationApi(){

        return "Hello. It's a microservice (name): " +
                nameService +
                " --- PID the application : " +
                System.getProperty("PID") +
                " --- Port the application : " +
                environment.getProperty("local.server.port");
    }
}
