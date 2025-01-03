package com.mallcloud.init.model.dto.reservation;

/**
 * @author Admin
 */

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationDTO {

    private Integer userId;

    private LocalDateTime time;
}
