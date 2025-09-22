package com.example.demo.mapper;

import com.example.demo.entity.Flight;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-12
 */
@Mapper
public interface FlightMapper {
    List<Flight> selectAllFlight(Integer skipNum, Integer pageSize, String flightNumber);

    Integer selectCountByPage(String flightNumber);

    void updateFlight(Flight flight);

    void addFlight(Flight flight);

    @Delete("delete from flights where flight_id = #{id};")
    void deleteFlight(Long id);
}
