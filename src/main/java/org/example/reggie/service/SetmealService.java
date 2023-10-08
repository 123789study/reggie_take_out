package org.example.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.reggie.dto.SetmealDto;
import org.example.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐,而且保留套餐与菜品的关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐时也要删除被关联的数字
     * @param ids
     */
    public void deleteWithDish(List<Long> ids);
}
