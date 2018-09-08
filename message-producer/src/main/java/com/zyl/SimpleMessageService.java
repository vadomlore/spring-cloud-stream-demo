package com.zyl;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface SimpleMessageService {

    @Output("rabbitmq2018") //channelName
    MessageChannel rabitMq(); //destination = test2018


    @Output("mns2018") //channelName
    MessageChannel mnsMq(); //destination = mns-test
}
