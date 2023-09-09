package com.xuezhidao.teacherservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "course")
public class CoureseEntity {

    @Id
    private String id;

    private String content;

    private String submissionTime;

    private String status;
}
