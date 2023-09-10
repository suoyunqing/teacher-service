package com.xuezhidao.teacherservice.service;

import com.xuezhidao.teacherservice.dto.CourseDto;
import com.xuezhidao.teacherservice.dto.CourseRecreationMessage;
import com.xuezhidao.teacherservice.entity.CoureseEntity;
import com.xuezhidao.teacherservice.entity.CourseRecreationEntity;
import com.xuezhidao.teacherservice.eventbus.EventBus;
import com.xuezhidao.teacherservice.exception.CoureseNotExistException;
import com.xuezhidao.teacherservice.exception.CoureseStatusException;
import com.xuezhidao.teacherservice.repository.CourseRecreationRepository;
import com.xuezhidao.teacherservice.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CourseService {

    public static final String COURSE_STATUS_TO_BE_UPDATED = "TO BE UPDATED";
    public static final String COURSE_STATUS_AVAILABLE  = "AVAILABLE";

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseRecreationRepository courseRecreationRepository;


    @Autowired
    private EventBus eventBus;

    @Transactional
    public void recreateCourse(CourseDto courseDto) {
        Optional<CoureseEntity> coureseEntityOptional = courseRepository.findById(courseDto.getCourseId());
        CoureseEntity coureseEntity = coureseEntityOptional.orElseThrow(() -> new CoureseNotExistException(String.format("课程 %s 不存在", courseDto.getCourseId())));
        if (!COURSE_STATUS_TO_BE_UPDATED.equals(coureseEntity.getStatus())) {
            throw new CoureseStatusException(String.format("课程 %s 不允许重新提交", courseDto.getCourseId()));
        }

        CourseRecreationEntity courseRecreationEntity = CourseRecreationEntity.builder()
                .submissionTime(courseDto.getSubmissionTime())
                .courseId(courseDto.getCourseId())
                .content(courseDto.getContent()).build();
        courseRecreationRepository.save(courseRecreationEntity);

        CourseRecreationMessage courseRecreationMessage = CourseRecreationMessage.builder().courseId(courseDto.getCourseId()).status(COURSE_STATUS_AVAILABLE).build();

        eventBus.publish(courseRecreationMessage);
    }
}
