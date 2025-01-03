package com.mallcloud.init.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mallcloud.init.model.entity.Reservation;
import com.mallcloud.init.model.enums.ReservationEnum;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Admin
 */
@Mapper
public interface ReservationMapper extends BaseMapper<Reservation> {


    @Insert("INSERT INTO reservations (user_id, start_time, end_time, status, confirmation_sent_at) " +
            "VALUES (#{userId}, #{startTime}, #{endTime}, #{status}, #{confirmationSentAt})")
    @Options(useGeneratedKeys = true, keyProperty = "reservationId")
    int insertReservation(Reservation reservation);

    @Select("SELECT * FROM reservations WHERE user_id = #{userId}")
    List<Reservation> findByUserId(@Param("userId") Integer userId);

    @Select("SELECT * FROM reservations WHERE reservation_id = #{id}")
    Reservation findById(@Param("id") Integer id);

    @Update("UPDATE reservations SET status = #{status} WHERE reservation_id = #{id}")
    int updateStatus(@Param("id") Integer id, @Param("status") ReservationEnum status);

    @Update("UPDATE reservations SET confirmation_sent_at = #{sentAt} " +
            "WHERE reservation_id = #{id}")
    int updateConfirmationSentTime(@Param("id") Integer id,
                                   @Param("sentAt") LocalDateTime sentAt);

    @Select("SELECT COUNT(*) FROM reservations WHERE user_id = #{userId} AND status = #{status}")
    int whetherUserHasReservation(Integer userId, String status);

    @Select("SELECT * FROM reservations WHERE user_id = #{userId} AND " +
            "start_time <= #{now} AND end_time >= #{now} AND status = 'confirmed' ")
    Reservation getReservationByNowTime(int userId, LocalDateTime now);

    @Delete("DELETE FROM reservations WHERE reservation_id = #{reservationId}")
    void deleteReservation(int reservationId);

    @Select("SELECT * FROM reservations LIMIT #{offset}, #{size}")
    List<Reservation> getReservationByPage(int offset, int size);

    @Select("SELECT COUNT(*) FROM reservations")
    Integer getCounts();

    @Select("SELECT * FROM reservations")
    List<Reservation> getAllDataList();
}