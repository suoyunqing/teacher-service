package com.xuezhidao.teacherservice.controller;

import com.xuezhidao.teacherservice.dto.CourseDto;
import com.xuezhidao.teacherservice.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class CourseContractsController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/course-contracts/{cid}/course-recreations/{rid}/confirmation")
    public ResponseEntity<String> recreateCourse(@Valid @RequestBody CourseDto courseDto){
        courseService.recreateCourse(courseDto);
        return new ResponseEntity("重新提交成功", HttpStatus.OK);
    }
}
