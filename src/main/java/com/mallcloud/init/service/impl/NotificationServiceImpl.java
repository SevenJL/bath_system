package com.mallcloud.init.service.impl;

import com.mallcloud.init.model.entity.Reservation;
import com.mallcloud.init.model.enums.ReservationEnum;
import com.mallcloud.init.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Admin
 */
@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {


    @Async
    @Override
    public void sendConfirmation(Reservation reservation) {
        log.info("发送确认邮件");
        reservation.setConfirmationSentAt(LocalDateTime.now());
        reservation.setStatus(ReservationEnum.PENDING.getText());
    }
}