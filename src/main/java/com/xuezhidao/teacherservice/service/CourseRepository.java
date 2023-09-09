package com.xuezhidao.teacherservice.service;

import com.xuezhidao.teacherservice.entity.CoureseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository  extends JpaRepository<CoureseEntity, String> {

}
