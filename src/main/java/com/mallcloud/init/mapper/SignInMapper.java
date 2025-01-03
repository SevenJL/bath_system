package com.mallcloud.init.mapper;

import com.mallcloud.init.model.entity.SignIn;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Admin
 */
public interface SignInMapper {
    // 根据用户ID查找当前是否有未完成的签到记录
    @Select("SELECT * FROM sign_ins WHERE user_id = #{userId} AND status = 'active' LIMIT 1")
    SignIn findActiveSignInByUserId(@Param("userId") int userId);

    // 创建新的签到记录
    @Insert("INSERT INTO sign_ins (user_id, status) VALUES (#{userId}, 'active')")
    int createSignIn(@Param("userId") int userId);

    // 更新签到状态为已完成
    @Update("UPDATE sign_ins SET status = 'completed', sign_in_time = CURRENT_TIMESTAMP WHERE user_id = #{userId} AND status = 'active'")
    void completeSignIn(@Param("userId") int userId);

    // 更新签到状态
    @Update("UPDATE sign_ins SET status = 'completed', sign_in_time = CURRENT_TIMESTAMP WHERE user_id = #{userId} AND status = 'active'")
    void updateSignInStatusToCompleted(@Param("userId") int userId);

    // 获取所有签到记录
    @Select("SELECT * FROM sign_ins")
    List<SignIn> getAllData();

    @Select("SELECT * FROM sign_ins WHERE DATE(sign_in_time) = #{today}")
    List<SignIn> getDataByDate(LocalDate today);
}
