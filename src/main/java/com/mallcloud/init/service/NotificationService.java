package com.mallcloud.init.service;

import com.mallcloud.init.model.entity.Reservation;

/**
 * @author Admin
 */
public interface NotificationService {

    void sendConfirmation(Reservation reservation);
}
