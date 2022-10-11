package org.microserives.demo.eurekaclient.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApiRestController {

    @Value("${spring.application.name}")
    private String nameService;

    @Value("${eureka.client.service-url.defaultZone}")
    private String uriServiceDiscovery;


    private final Environment environment;


    @GetMapping("api")
    public String getInfoSystemApplicationApi(){

        return "Hello. It's a microservice (name): " +
                nameService +
                " --- Uri a Discovery Service : " +
                uriServiceDiscovery +
                " --- PID the application : " +
                System.getProperty("PID") +
                " --- Port the application : " +
                environment.getProperty("local.server.port");
    }
}
