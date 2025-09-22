package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Logs {
    private Integer id;
    private String operation;
    private String type;
    private String ip;
    private String user;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime time;

}