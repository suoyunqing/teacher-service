package com.xuezhidao.teacherservice.repository;

import com.xuezhidao.teacherservice.entity.CoureseEntity;
import com.xuezhidao.teacherservice.entity.CourseRecreationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRecreationRepository extends JpaRepository<CourseRecreationEntity, String> {

}
