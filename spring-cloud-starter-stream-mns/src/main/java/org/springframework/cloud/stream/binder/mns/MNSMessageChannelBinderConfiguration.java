package org.springframework.cloud.stream.binder.mns;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MNSProperties.class)
public class MNSMessageChannelBinderConfiguration {

    @Autowired
    private MNSProperties mnsProperties;

    @Bean
    public MNSMessageChannelBinder mnsMessageChannelBinder(){
        return new MNSMessageChannelBinder(mnsProperties);
    }

}
