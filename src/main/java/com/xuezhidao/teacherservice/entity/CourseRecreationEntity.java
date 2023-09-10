package com.xuezhidao.teacherservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.validator.constraints.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "course_recreation")
public class CourseRecreationEntity {

    @Id
    @UUID
    private String id;

    private String courseId;

    private String submissionTime;

    private String content;
}
