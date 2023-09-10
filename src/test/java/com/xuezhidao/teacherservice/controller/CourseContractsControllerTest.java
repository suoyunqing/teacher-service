package com.xuezhidao.teacherservice.controller;

import com.xuezhidao.teacherservice.exception.CoureseNotExistException;
import com.xuezhidao.teacherservice.exception.CoureseStatusException;
import com.xuezhidao.teacherservice.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.kafka.KafkaException;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseContractsController.class)
class CourseContractsControllerTest {

    public static final String COURSE_RECREATIONS_API = "/course-contracts/{cid}/course-recreations/{rid}/confirmation";
    public static final String COURSE_RECREATIONS_REQUESTBODY =
            """
                {
                    "courseId": "aa",
                    "content": "xxxxxxxxxxxxxxxxx",
                    "submissionTime": "2020-01-20 08:00:00"
                }
            """;

    public static final String COURSE_RECREATIONS_REQUESTBODY_HAS_MISSING_VALUE =
            """
                {
                    "courseId": "aa",
                    "submissionTime": "2020-01-20 08:00:00"
                }
            """
            ;

    public static final String COURSE_RECREATIONS_REQUESTBODY_COURSEID_NOT_EXIST =
            """
                {
                    "courseId": "aa-not-exist",
                    "content": "xxxxxxxxxxxxxxxxx",
                    "submissionTime": "2020-01-20 08:00:00"
                }
            """
            ;



    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService mockCourseService;

    @Test
    public void shouldReturn200WhenCallRecreateCourseApiGivenValidRequestbody() throws Exception {
        mockMvc.perform(post(COURSE_RECREATIONS_API, "aa", "aa-1")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(COURSE_RECREATIONS_REQUESTBODY))
                .andExpect(status().isOk())
                .andExpect(content().string("重新提交成功"));
    }

    @Test
    public void shouldReturn400WhenCallRecreateCourseApiGivenRequestbodyHasMissingValue() throws Exception {
        mockMvc.perform(post(COURSE_RECREATIONS_API, "aa", "aa-1")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(COURSE_RECREATIONS_REQUESTBODY_HAS_MISSING_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400WhenCallRecreateCourseApiGivenCourseidNotExist() throws Exception {
        doThrow(new CoureseNotExistException("课程aa-not-exist不存在")).when(mockCourseService).recreateCourse(any());

        mockMvc.perform(post(COURSE_RECREATIONS_API, "aa-not-exist", "aa-not-exist-1")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(COURSE_RECREATIONS_REQUESTBODY_COURSEID_NOT_EXIST))
                .andExpect(status().isNotFound())
                .andExpect(content().string("课程aa-not-exist不存在"));
    }

    @Test
    public void shouldReturn400WhenCallRecreateCourseApiGivenCourseStatusNotCorrect() throws Exception {
        doThrow(new CoureseStatusException("课程aa不允许重新提交")).when(mockCourseService).recreateCourse(any());

        mockMvc.perform(post(COURSE_RECREATIONS_API, "aa", "aa-1")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(COURSE_RECREATIONS_REQUESTBODY))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("课程aa不允许重新提交"));
    }

    @Test
    public void shouldReturn500WhenCallRecreateCourseApiGivenDbNotAvailable() throws Exception {
        doThrow(new QueryTimeoutException("DB不可用")).when(mockCourseService).recreateCourse(any());

        mockMvc.perform(post(COURSE_RECREATIONS_API, "aa", "aa-1")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(COURSE_RECREATIONS_REQUESTBODY))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("服务错误，请重试"));
    }

    @Test
    public void shouldReturn500WhenCallRecreateCourseApiGivenKafkaNotAvailable() throws Exception {
        doThrow(new KafkaException("Kafka不可用")).when(mockCourseService).recreateCourse(any());

        mockMvc.perform(post(COURSE_RECREATIONS_API, "aa", "aa-1")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(COURSE_RECREATIONS_REQUESTBODY))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("服务错误，请重试"));
    }


}