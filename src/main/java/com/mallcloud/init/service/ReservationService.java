package com.mallcloud.init.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mallcloud.init.model.dto.reservation.ReservationDTO;
import com.mallcloud.init.model.entity.Reservation;

import java.util.List;

/**
 * 预约服务
 * @author Admin
 */
public interface ReservationService  extends IService<Reservation> {

    /**
     * 创建预约
     */
    Reservation createReservation(ReservationDTO dto);

    /**
     * 取消预约
     */
    void cancelReservation(Integer reservationId);

    /**
     * 获取用户预约
     */
    List<Reservation> getUserReservations(Integer userId);

    /**
     * 根据当前时间获取预约
     */
    Reservation getReservationByNowTime(int userId);

    /**
     * 删除预约
     */
    void deleteReservation(int userId);

    /**
     * 分页获取用户预约
     */
    List<Reservation> getReservationsByPage(int page, int size);

    /**
     * 获取预约数量
     */
    Integer getCounts();

    /**
     * 获取所有预约
     */
    List<Reservation> getAllDataList();

    /**
     * 确认预约
     */
    void confirmReservation(Integer reservationId);
}
