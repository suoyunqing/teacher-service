package com.xuezhidao.teacherservice.service;

import com.xuezhidao.teacherservice.dto.CourseDto;
import com.xuezhidao.teacherservice.dto.CourseRecreationMessage;
import com.xuezhidao.teacherservice.entity.CoureseEntity;
import com.xuezhidao.teacherservice.eventbus.EventBus;
import com.xuezhidao.teacherservice.exception.CoureseNotExistException;
import com.xuezhidao.teacherservice.exception.CoureseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseService {

    public static final String COURSE_STATUS_TO_BE_UPDATED = "TO BE UPDATED";
    public static final String COURSE_STATUS_AVAILABLE  = "AVAILABLE";

    @Autowired
    private CourseRepository courseRepository;


    @Autowired
    private EventBus eventBus;

    public void recreateCourse(CourseDto courseDto) {
        Optional<CoureseEntity> coureseEntityOptional = courseRepository.findById(courseDto.getCourseId());
        CoureseEntity coureseEntity = coureseEntityOptional.orElseThrow(() -> new CoureseNotExistException(String.format("课程 %s 不存在", courseDto.getCourseId())));
        if (!COURSE_STATUS_TO_BE_UPDATED.equals(coureseEntity.getStatus())) {
            throw new CoureseStatusException(String.format("课程 %s 不允许重新提交", courseDto.getCourseId()));
        }

        coureseEntity.setContent(courseDto.getContent());
        coureseEntity.setStatus(COURSE_STATUS_AVAILABLE);
        coureseEntity.setSubmissionTime(courseDto.getSubmissionTime());

        courseRepository.save(coureseEntity);

        CourseRecreationMessage courseRecreationMessage = CourseRecreationMessage.builder().courseId(courseDto.getCourseId()).status(COURSE_STATUS_AVAILABLE).build();

        eventBus.publish(courseRecreationMessage);
    }
}
