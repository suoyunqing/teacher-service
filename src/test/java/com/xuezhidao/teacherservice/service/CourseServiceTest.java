package com.xuezhidao.teacherservice.service;

import com.xuezhidao.teacherservice.dto.CourseDto;
import com.xuezhidao.teacherservice.dto.CourseRecreationMessage;
import com.xuezhidao.teacherservice.entity.CoureseEntity;
import com.xuezhidao.teacherservice.eventbus.EventBus;
import com.xuezhidao.teacherservice.exception.CoureseNotExistException;
import com.xuezhidao.teacherservice.exception.CoureseStatusException;
import com.xuezhidao.teacherservice.repository.CourseRepository;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.KafkaException;

import java.util.Optional;

import static com.xuezhidao.teacherservice.service.CourseService.COURSE_STATUS_AVAILABLE;
import static com.xuezhidao.teacherservice.service.CourseService.COURSE_STATUS_TO_BE_UPDATED;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @InjectMocks
    CourseService courseService;

    @Mock
    CourseRepository mockCourseRepository;

    @Mock
    EventBus mockEventBus;


    @Test
    void shouldSaveDbAndPublishToKafkaWhenCallRecreateCourseGivenValidCourse() {
        //Given
        CourseDto courseDto = CourseDto.builder().courseId("aa").content("xxxx").submissionTime("2020-01-20 08:00:00").build();
        CoureseEntity existingCoureseEntity = CoureseEntity.builder().id("aa").content("ssss").submissionTime("2018-01-20 08:00:00").status(COURSE_STATUS_TO_BE_UPDATED).build();
        doReturn(Optional.of(existingCoureseEntity)).when(mockCourseRepository).findById("aa");

        CoureseEntity expectedCoureseEntity = CoureseEntity.builder().id("aa").content("xxxx").submissionTime("2020-01-20 08:00:00").status(COURSE_STATUS_AVAILABLE).build();
        CourseRecreationMessage expectedCourseRecreationMessage = CourseRecreationMessage.builder().courseId("aa").status(COURSE_STATUS_AVAILABLE).build();

        //When
        courseService.recreateCourse(courseDto);

        //Then
        verify(mockCourseRepository).save(refEq(expectedCoureseEntity));
        verify(mockEventBus).publish(refEq(expectedCourseRecreationMessage));
    }

    @Test
    void shouldThrowCoureseNotExistExceptionWhenCallRecreateCourseGivenCourseIdNotExist() {
        //Given
        CourseDto courseDto = CourseDto.builder().courseId("aa").content("xxxx").submissionTime("2020-01-20 08:00:00").build();
        doReturn(Optional.ofNullable(null)).when(mockCourseRepository).findById("aa");


        //When
        assertThrows(CoureseNotExistException.class, () -> courseService.recreateCourse(courseDto));

        //Then

        verify(mockCourseRepository, times(0)).save(any());
        verify(mockEventBus, times(0)).publish(any());
    }

    @Test
    void shouldThrowCoureseStatusExceptionWhenCallRecreateCourseGivenCourseStatusIsAvailable() {
        //Given
        CourseDto courseDto = CourseDto.builder().courseId("aa").content("xxxx").submissionTime("2020-01-20 08:00:00").build();
        CoureseEntity existingCoureseEntity = CoureseEntity.builder().id("aa").content("ssss").submissionTime("2018-01-20 08:00:00").status(COURSE_STATUS_AVAILABLE).build();

        doReturn(Optional.ofNullable(existingCoureseEntity)).when(mockCourseRepository).findById("aa");

        //When
        assertThrows(CoureseStatusException.class, () -> courseService.recreateCourse(courseDto));

        //Then

        verify(mockCourseRepository, times(0)).save(any());
        verify(mockEventBus, times(0)).publish(any());
    }

    @Test
    void shouldThrowExceptionWhenCallRecreateCourseGivenDbIsNotAvailable() {
        //Given
        CourseDto courseDto = CourseDto.builder().courseId("aa").content("xxxx").submissionTime("2020-01-20 08:00:00").build();

        doThrow(PersistenceException.class).when(mockCourseRepository).findById("aa");

        //When
        assertThrows(PersistenceException.class, () -> courseService.recreateCourse(courseDto));

        //Then

        verify(mockCourseRepository, times(0)).save(any());
        verify(mockEventBus, times(0)).publish(any());
    }

    @Test
    void shouldThrowExceptionWhenCallRecreateCourseGivenKafkaIsNotAvailable() {
        //Given
        CourseDto courseDto = CourseDto.builder().courseId("aa").content("xxxx").submissionTime("2020-01-20 08:00:00").build();
        CoureseEntity existingCoureseEntity = CoureseEntity.builder().id("aa").content("ssss").submissionTime("2018-01-20 08:00:00").status(COURSE_STATUS_TO_BE_UPDATED).build();
        doReturn(Optional.of(existingCoureseEntity)).when(mockCourseRepository).findById("aa");

        doThrow(KafkaException.class).when(mockEventBus).publish(any());

        //When
        assertThrows(KafkaException.class, () -> courseService.recreateCourse(courseDto));

        //Then

        verify(mockCourseRepository, times(1)).save(any());
    }
}