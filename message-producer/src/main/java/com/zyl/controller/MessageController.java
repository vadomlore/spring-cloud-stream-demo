package com.zyl.controller;

import com.zyl.SimpleMessageService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MessageController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SimpleMessageService service;

    @GetMapping
    public String send(@RequestParam String message){
        rabbitTemplate.convertAndSend("rabitDes01","", message + "Hello, World");
        return "ok";
    }

    @GetMapping("/stream/rabitmq/{message}")
    public boolean sendStream(@PathVariable("message") String message){
        MessageChannel messageChannel = service.rabitMq();
        Map<String, Object> headers = new HashMap<>();
        headers.put("charset-encoding", "UTF-8");
        headers.put("content-type", MediaType.TEXT_PLAIN_VALUE);
        return messageChannel.send(new GenericMessage(message, headers));
    }

    @GetMapping("/stream/mns/{message}")
    public boolean sendMNSStream(@PathVariable("message") String message){
        MessageChannel messageChannel = service.mnsMq();
        Map<String, Object> headers = new HashMap<>();
        headers.put("charset-encoding", "UTF-8");
        headers.put("content-type", MediaType.TEXT_PLAIN_VALUE);
        return messageChannel.send(new GenericMessage(message, headers));
    }

}
