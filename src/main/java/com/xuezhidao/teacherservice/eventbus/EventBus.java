package com.xuezhidao.teacherservice.eventbus;

import com.xuezhidao.teacherservice.dto.CourseRecreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventBus{

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${spring.kafka.producer.topics.course}")
    private String topic;
    public void publish(CourseRecreationMessage courseRecreationMessage) {
        kafkaTemplate.send(topic, courseRecreationMessage);
    }
}
