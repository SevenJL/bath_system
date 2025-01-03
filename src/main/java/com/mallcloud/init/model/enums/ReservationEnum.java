package com.mallcloud.init.model.enums;

import lombok.Getter;

/**
 * @author Admin
 */

@Getter
public enum ReservationEnum {
    PENDING(0, "pending"),
    CONFIRMED(1, "confirmed"),
    CANCELLED(2, "cancelled");

    private final Integer value;

    private final String text;

    ReservationEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

}
