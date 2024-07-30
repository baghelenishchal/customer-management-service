package com.wrkspot.assignment.customer_management_service.producer;

import com.wrkspot.assignment.customer_management_service.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    private static final String TOPIC = "customer_topic";

    @Autowired
    private KafkaTemplate<String, Customer> kafkaTemplate;

    public void sendMessage(Customer customer) {
        logger.info("Producing message: {}", customer);
        kafkaTemplate.send(TOPIC, customer);
    }
}
