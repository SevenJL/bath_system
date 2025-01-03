package com.mallcloud.init.utils;

import com.mallcloud.init.constant.MagicConstant;
import com.mallcloud.init.model.entity.SignIn;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Admin
 */
public class TimeClassifierUtil {

    public static Map<String, Integer> classifySignInsByTime(List<SignIn> signIns) {
        Map<String, Integer> timeSlots = new HashMap<>();
        timeSlots.put(MagicConstant.TIME_SLOT_6_9, 0);
        timeSlots.put(MagicConstant.TIME_SLOT_9_12, 0);
        timeSlots.put(MagicConstant.TIME_SLOT_12_15, 0);
        timeSlots.put(MagicConstant.TIME_SLOT_15_18, 0);
        timeSlots.put(MagicConstant.TIME_SLOT_18_21, 0);
        timeSlots.put(MagicConstant.TIME_SLOT_21_AFTER, 0);
        for (SignIn signIn : signIns) {
            LocalTime signInTime = signIn.getSignInTime().toLocalTime();
            if (isInTimeRange(signInTime, LocalTime.of(6, 0), LocalTime.of(9, 0))) {
                timeSlots.put(MagicConstant.TIME_SLOT_6_9, timeSlots.get(MagicConstant.TIME_SLOT_6_9) + 1);
            }
            if (isInTimeRange(signInTime, LocalTime.of(9, 0), LocalTime.of(12, 0))) {
                timeSlots.put(MagicConstant.TIME_SLOT_9_12, timeSlots.get(MagicConstant.TIME_SLOT_9_12) + 1);
            }
            if (isInTimeRange(signInTime, LocalTime.of(12, 0), LocalTime.of(15, 0))) {
                timeSlots.put(MagicConstant.TIME_SLOT_12_15, timeSlots.get(MagicConstant.TIME_SLOT_12_15) + 1);
            }
            if (isInTimeRange(signInTime, LocalTime.of(15, 0), LocalTime.of(18, 0))) {
                timeSlots.put(MagicConstant.TIME_SLOT_15_18, timeSlots.get(MagicConstant.TIME_SLOT_15_18) + 1);
            }
            if (isInTimeRange(signInTime, LocalTime.of(18, 0), LocalTime.of(21, 0))) {
                timeSlots.put(MagicConstant.TIME_SLOT_18_21, timeSlots.get(MagicConstant.TIME_SLOT_18_21) + 1);
            }
            if (isInTimeRange(signInTime, LocalTime.of(21, 0), LocalTime.of(23, 59))) {
                timeSlots.put(MagicConstant.TIME_SLOT_21_AFTER, timeSlots.get(MagicConstant.TIME_SLOT_21_AFTER) + 1);
            }
        }
        return timeSlots;
    }

    private static boolean isInTimeRange(LocalTime time, LocalTime rangeStart, LocalTime rangeEnd) {
        return (time.equals(rangeStart) || time.isAfter(rangeStart)) && (time.isBefore(rangeEnd) || time.equals(rangeEnd));
    }

}
