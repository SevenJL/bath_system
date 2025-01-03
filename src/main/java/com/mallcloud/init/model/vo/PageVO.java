package com.mallcloud.init.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@RequiredArgsConstructor
public class PageVO<T> implements Serializable {

    /**
     * 当前页数据
     */
    private List<T> data;

    /**
     * 总页数
     */
    private Integer pageTotal;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
