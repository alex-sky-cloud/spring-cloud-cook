package com.rabbitmq.dlqprocessor.service;

import com.rabbitmq.dlqprocessor.model.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DlqService {

    /** Название очереди, откуда нужно забирать сообщения, которые вызывают ошибку
     * будет иметь формат
     * <name-destination>.<name-group>.dlq.
     * name-destination - это значение свойства destination
     * spring.cloud.stream.bindings.subscribe-in-0.destination = ...
     * name-group - это значение свойства group
     * spring.cloud.stream.bindings.subscribe-in-0.group = ...
     * Эта очередь будет автоматически создаваться,
     * в rabbitmq, когда приложение начнет инициализироваться и spring начнет создавать
     * конфигурацию, на основе данных представленных в application.yml, сервиса Subscriber
     */
    private static final String SUBSCRIBER_QUEUE_NAME = "inputSubscriber.inputSubscriberGroup";

    private static final String SUBSCRIBER_DLQ_NAME = SUBSCRIBER_QUEUE_NAME + ".dlq";

    /* private final RabbitTemplate rabbitTemplate;*/

    /**
     * В данном методе можно организовать отправку сообщений, полученных из очереди
     * DLQ либо в базу данных, либо в другой сервис-агрегатор и т.д.
     * @param failedMessage - сообщение, которое находится в очереди DLQ
     */
    @RabbitListener(queues = {SUBSCRIBER_DLQ_NAME})
    public void processFailedMessage(MessageDto failedMessage){

      log.error("Message cannot be processed. Write to database: " + failedMessage.toString());
    }
}