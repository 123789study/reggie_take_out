package org.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.reggie.dto.DishDto;
import org.example.reggie.entity.Dish;
import org.example.reggie.entity.DishFlavor;
import org.example.reggie.mapper.DishMapper;
import org.example.reggie.service.DishFlavorSerive;
import org.example.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorSerive dishFlavorSerive;

    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        // 保存菜品的基本信息到dish表格中
        this.save(dishDto);

        //菜品id
        var dishId = dishDto.getId();

        var flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) ->
                {
                    item.setDishId(dishId);
                    return item;
                }
        ).collect(Collectors.toList());

        dishFlavorSerive.saveBatch(flavors);
    }

    /**
     * 根据id查询菜品对应信息和口味信息
     *
     * @param id
     * @return
     */
    public DishDto getByIdWithFlavor(Long id) {

        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        var flavors = dishFlavorSerive.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    /**
     * 根据id修改菜品对应信息和口味信息
     *
     * @param dishDto
     * @return
     */
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);

        // 先删后加
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());

        dishFlavorSerive.remove(queryWrapper);

        var flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorSerive.saveBatch(flavors);
    }
}
