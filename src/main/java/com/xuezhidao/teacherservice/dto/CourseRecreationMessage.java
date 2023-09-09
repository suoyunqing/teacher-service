package com.xuezhidao.teacherservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CourseRecreationMessage {
    private String courseId;
    private String status;
}
