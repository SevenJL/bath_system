package com.mallcloud.init.model.dto.timeslot;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Admin
 */
@Data
public class TimeSlotDTO {
    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
