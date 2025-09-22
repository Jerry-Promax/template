package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    private Integer flightId;
    private String flightNumber;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime departureTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime arrivalTime;
    private String departure;
    private String arrival;
    private Integer totalSeats;
    private Integer availableSeats;
    private Double price;

}