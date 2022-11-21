package com.rabbitmq.subscriberservice.configuration;

public class MessageException extends RuntimeException{
    public MessageException(String message) {
        super(message);
    }
}
