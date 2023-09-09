package com.xuezhidao.teacherservice.dto;


import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDto {

    @NotBlank
    private String courseId;

    @NotBlank
    private String content;

    @NotBlank
    private String submissionTime;
}