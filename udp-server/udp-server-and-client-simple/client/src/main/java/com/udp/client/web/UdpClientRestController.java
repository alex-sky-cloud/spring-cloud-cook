package com.udp.client.web;

import com.udp.client.service.EchoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.udp.client.utils.ConvertUtils.addStringWhiteSpaces;

@RestController
@RequestMapping("udp/client")
@RequiredArgsConstructor
public class UdpClientRestController {

    private final EchoClient echoClient;

    @Value("${app.size.message}")
    private Integer lengthMessageMax;

    @Value("${app.signal.server}")
    private String signalCloseServer;

    @GetMapping("/request/{request}")
    public String getMessage(@PathVariable String request){

        String requestToUdpServer = addStringWhiteSpaces(request, lengthMessageMax);
        return echoClient.sendEcho(requestToUdpServer);
    }

    @GetMapping("close")
    public ResponseEntity<Object> closeSocket(){

        echoClient.sendEcho(signalCloseServer);
        echoClient.closeSocket();

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
