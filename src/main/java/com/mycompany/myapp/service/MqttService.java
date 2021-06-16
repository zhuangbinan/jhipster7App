package com.mycompany.myapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

@Service
public class MqttService implements ApplicationEventPublisherAware {

    @Value("${application.mqtt-client-config.client-id}")
    private  String clientId;

    private ApplicationEventPublisher applicationEventPublisher;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }




}
