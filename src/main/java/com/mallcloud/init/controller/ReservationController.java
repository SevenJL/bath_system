package com.mallcloud.init.controller;

import com.mallcloud.init.common.BaseResponse;
import com.mallcloud.init.common.ErrorCode;
import com.mallcloud.init.common.PageRequest;
import com.mallcloud.init.common.ResultUtils;
import com.mallcloud.init.exception.BusinessException;
import com.mallcloud.init.model.dto.reservation.ReservationDTO;
import com.mallcloud.init.model.entity.Reservation;
import com.mallcloud.init.model.entity.SignIn;
import com.mallcloud.init.model.vo.PageVO;
import com.mallcloud.init.service.ReservationService;
import com.mallcloud.init.service.impl.SignInServiceImpl;
import com.mallcloud.init.utils.TimeClassifierUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预约控制器
 *
 * @author Admin
 */
@Slf4j
@RestController
@RequestMapping("/reservations")
@Validated
public class ReservationController {

    private final ReservationService reservationService;
    private final SignInServiceImpl signInService;


    public ReservationController(ReservationService reservationService, SignInServiceImpl signInService) {
        this.reservationService = reservationService;
        this.signInService = signInService;
    }

    /**
     * 创建预约
     */
    @PostMapping
    public BaseResponse createReservation(@RequestBody ReservationDTO reservationDTO) {
        if (reservationDTO == null || reservationDTO.getUserId() == null
                || reservationDTO.getTime() == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        Reservation reservation = reservationService.createReservation(reservationDTO);
        if (reservation == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(reservation);
    }

    /**
     * 确认预约
     */
    @PutMapping("/{id}/confirm")
    public BaseResponse confirmReservation(@PathVariable("id") Integer reservationId) {
        if (reservationId == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        reservationService.confirmReservation(reservationId);
        return ResultUtils.success("更新成功");
    }

    /**
     * 取消预约
     */
    @PutMapping("/{id}/cancel")
    public BaseResponse cancelReservation(@PathVariable("id") Integer reservationId) {
        if (reservationId == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        reservationService.cancelReservation(reservationId);
        return ResultUtils.success(true);
    }

    /**
     * 获取用户预约记录
     */
    @GetMapping("/user/{userId}")
    public BaseResponse getUserReservations(@PathVariable Integer userId) {
        if (userId == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        List<Reservation> userReservations = reservationService.getUserReservations(userId);
        if (userReservations == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(userReservations);
    }

    /**
     * 分页查询预约记录
     *
     * @return 分页结果
     */
    @PostMapping("/page")
    public BaseResponse<PageVO<Reservation>> getReservationsByPage(@RequestBody PageRequest pageRequest) {

        int page = pageRequest.getPage();
        int size = pageRequest.getSize();

        // 1. 调用查询方法
        List<Reservation> reservationList = reservationService.getReservationsByPage(page, size);

        // 2. 判断分页数据是否为空
        if (reservationList.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "没有查询到数据！");
        }

        // 3. 查询总数
        Integer total = reservationService.getCounts();
        PageVO<Reservation> pageVO = new PageVO<>();
        pageVO.setData(reservationList);
        pageVO.setPageTotal(total);

        // 4. 返回分页结果
        log.info("分页查询成功！:{}", pageVO);
        return ResultUtils.success(pageVO);
    }


}