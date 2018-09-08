package com.zyl;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface SimpleMessageReceiver {

    @Input("rabbitmq2018") //channelName
    SubscribableChannel rabitMq(); //destination = test2018

    @Input("mns2018") //channelName
    SubscribableChannel mns(); //destination = mns-test

}
