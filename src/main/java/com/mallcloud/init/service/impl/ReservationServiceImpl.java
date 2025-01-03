package com.mallcloud.init.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mallcloud.init.common.ErrorCode;
import com.mallcloud.init.exception.BusinessException;
import com.mallcloud.init.mapper.ReservationMapper;
import com.mallcloud.init.model.dto.reservation.ReservationDTO;
import com.mallcloud.init.model.entity.Reservation;
import com.mallcloud.init.model.enums.ReservationEnum;
import com.mallcloud.init.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Admin
 */
@Service
@Slf4j
public class ReservationServiceImpl  extends ServiceImpl<ReservationMapper, Reservation> implements ReservationService {

    private final ReservationMapper reservationMapper;

    public ReservationServiceImpl(ReservationMapper reservationMapper) {
        this.reservationMapper = reservationMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Reservation createReservation(ReservationDTO dto) {
        // 构建预约对象
        Reservation reservation = new Reservation();
        reservation.setUserId(dto.getUserId());
        reservation.setStartTime(dto.getTime());
        // 在原有时间加上2小时
        reservation.setEndTime(dto.getTime().plusHours(2));
        reservation.setStatus(ReservationEnum.PENDING.getText());

        // 检查用户是否预约过了 (待确认)
        if (reservationMapper.whetherUserHasReservation(dto.getUserId(), ReservationEnum.PENDING.getText()) > 5) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户已预约,不能多次预约");
        }

        // 插入数据库
        int insert = reservationMapper.insertReservation(reservation);
        if (insert != 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }

        // 发送预约确认通知
        try {
            reservation.setConfirmationSentAt(LocalDateTime.now());
            int i = reservationMapper.updateConfirmationSentTime(reservation.getReservationId(),
                    reservation.getConfirmationSentAt());
            if (i != 1) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR);
            }
        } catch (Exception e) {
            log.error("发送预约确认通知失败", e);
        }

        return reservation;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelReservation(Integer reservationId) {
        // 查询当前时间段的预约
        Reservation reservation = reservationMapper.findById(reservationId);
        if (reservation == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 预约已取消
        if (Objects.equals(reservation.getStatus(), ReservationEnum.CANCELLED.getText())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "预约已取消");
        }

        // 更新状态
        int i = reservationMapper.updateStatus(reservationId, ReservationEnum.CANCELLED);
        if (i != 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
    }

    @Override
    public List<Reservation> getUserReservations(Integer userId) {
        // 查询预约
        return reservationMapper.findByUserId(userId);
    }

    @Override
    public Reservation getReservationByNowTime(int userId) {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        return reservationMapper.getReservationByNowTime(userId, now);
    }

    @Override
    public void deleteReservation(int reservationId) {
        reservationMapper.deleteReservation(reservationId);
    }

    @Override
    public List<Reservation> getReservationsByPage(int page, int size) {
        // 1. 计算偏移量
        int offset = (page - 1) * size;
        // 2. 查询用户列表
        return reservationMapper.getReservationByPage(offset, size);
    }

    @Override
    public Integer getCounts() {
        return reservationMapper.getCounts();
    }
    @Override
    public List<Reservation> getAllDataList() {
        return reservationMapper.getAllDataList();
    }
}