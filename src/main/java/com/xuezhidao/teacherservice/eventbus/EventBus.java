package com.xuezhidao.teacherservice.eventbus;

import com.xuezhidao.teacherservice.dto.CourseRecreationMessage;
import com.xuezhidao.teacherservice.entity.CoureseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class EventBus{
    public void publish(CourseRecreationMessage courseRecreationMessage) {
    }
}
