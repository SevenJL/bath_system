package com.mallcloud.init.service.impl;

import com.mallcloud.init.mapper.SignInMapper;
import com.mallcloud.init.model.entity.Reservation;
import com.mallcloud.init.model.entity.SignIn;
import com.mallcloud.init.service.SignInService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Admin
 */
@Service
public class SignInServiceImpl implements SignInService {

    private final SignInMapper signInMapper;

    private final ReservationServiceImpl reservationService;

    public SignInServiceImpl(SignInMapper signInMapper, ReservationServiceImpl reservationService) {
        this.signInMapper = signInMapper;
        this.reservationService = reservationService;
    }

    // 签到
    @Override
    @Transactional
    public void signIn(int userId) throws Exception {
        // 检查用户是否已经签到
        SignIn signIn = signInMapper.findActiveSignInByUserId(userId);
        if (signIn != null) {
            throw new Exception("用户已经签到");
        }
        // 根据当前时间去查询预约表是否存在预约数据
        Reservation reservationByNowTime = reservationService.getReservationByNowTime(userId);
        if (reservationByNowTime != null) {
            // 创建签到记录
            int createSignIn = signInMapper.createSignIn(userId);
            if (createSignIn > 0) {
                // 删除预约记录
                reservationService.deleteReservation(reservationByNowTime.getReservationId());
            } else {
                throw new Exception("创建签到记录失败");
            }
        } else {
            throw new Exception("没有预约数据");
        }
    }

    // 完成签到
    @Override
    @Transactional
    public void completeSignIn(int userId) throws Exception {
        // 检查是否有正在进行的签到（status = 'active'）
        SignIn activeSignInByUserId = signInMapper.findActiveSignInByUserId(userId);
        if (activeSignInByUserId == null) {
            throw new Exception("没有正在进行的签到");
        }

        // 更新签到记录
        signInMapper.updateSignInStatusToCompleted(userId);
    }

    @Override
    public List<SignIn> getAllData() {
        // 获取今天的日期
        LocalDate today = LocalDate.now();

        // 查询今天的数据
        return signInMapper.getDataByDate(today);
    }
}
