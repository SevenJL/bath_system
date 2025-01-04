package com.mallcloud.init.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mallcloud.init.model.entity.Feedback;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Admin
 */
public interface FeedbackMapper extends BaseMapper<Feedback> {

    @Select("select * from feedback limit #{offset},#{size}")
    List<Feedback> getReservationByPage(int offset, int size);

    @Select("select count(*) from feedback")
    Integer getCounts();

    @Delete("delete from feedback where feedback_id = #{id}")
    int deleteFeedbackById(Long id);
}
