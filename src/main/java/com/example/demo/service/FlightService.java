package com.example.demo.service;

import com.example.demo.common.Page;
import com.example.demo.entity.Flight;

import java.util.List;

/**
 * 功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-12
 */
public interface FlightService {
    Page selectAllFlight(Integer pageNum, Integer pageSize, String flightNumber);

    void updateFlight(Flight flight);

    void addFlight(Flight flight);

    void deleteFlight(Long id);

    void deleteBatchFlight(List<Long> ids);
}
