package org.example.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.reggie.dto.DishDto;
import org.example.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    // 新增菜品,同时插入菜品对应的口味数据,操作两张表dish,dish_flover
    public void saveWithFlavor(DishDto dishDto);

    // 根据id查询菜品对应信息和口味信息
    public DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);
}
