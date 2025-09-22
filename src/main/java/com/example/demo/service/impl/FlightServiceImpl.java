package com.example.demo.service.impl;

import com.example.demo.common.Page;
import com.example.demo.entity.Flight;
import com.example.demo.entity.User;
import com.example.demo.mapper.FlightMapper;
import com.example.demo.service.FlightService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-12
 */
@Service
public class FlightServiceImpl implements FlightService {
    @Resource
    FlightMapper flightMapper;
    @Override
    public Page selectAllFlight(Integer pageNum, Integer pageSize, String flightNumber) {
        Integer skipNum = (pageNum-1) * pageSize;
        List<Flight> flightList = flightMapper.selectAllFlight(skipNum,pageSize,flightNumber);
        Integer count = flightMapper.selectCountByPage(flightNumber);
        Page page = new Page();
        page.setList(flightList);
        page.setTotal(count);
        return page;
    }

    @Override
    public void updateFlight(Flight flight) {
        flightMapper.updateFlight(flight);
    }

    @Override
    public void addFlight(Flight flight) {
        flightMapper.addFlight(flight);
    }

    @Override
    public void deleteFlight(Long id) {
        flightMapper.deleteFlight(id);
    }

    @Override
    public void deleteBatchFlight(List<Long> ids) {
        for (Long id:ids) {
            flightMapper.deleteFlight(id);
        }
    }
}