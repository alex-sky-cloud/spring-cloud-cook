package org.microserives.demo.eurekaclientsecondcopy.web;

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

    @Value("${eureka.client.service-url.defaultZone}")
    private String uriServiceDiscovery;

    @Value("${eureka.instance.instanceId}")
    private String idService;


    @GetMapping("id-second-service")
    public String getIdService(){

        return " ---- Id-service : " +
                idService +
                " ---- Port the application : " +
                environment.getProperty("local.server.port");
    }


    @GetMapping("info")
    public String getInfoSystemApplicationApi(){

        return "Hello. It's a microservice (name): " +
                nameService +
                " ---- Id-service : " +
                idService +
                " --- Uri a Discovery Service : " +
                uriServiceDiscovery +
                " --- PID the application : " +
                System.getProperty("PID") +
                " --- Port the application : " +
                environment.getProperty("local.server.port");
    }
}
