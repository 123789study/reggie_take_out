package org.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.reggie.common.CustomException;
import org.example.reggie.entity.Category;
import org.example.reggie.entity.Dish;
import org.example.reggie.entity.Setmeal;
import org.example.reggie.mapper.CategoryMapper;
import org.example.reggie.service.CategoryService;
import org.example.reggie.service.DishService;
import org.example.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long ids) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();

        // 添加查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, ids);
        int count1 = dishService.count(dishLambdaQueryWrapper);

        //查询当前分类是否关联了菜品
        if (count1 > 0) {
            throw new CustomException("当前分类下关联菜品,不能删除");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, ids);
        int count2 = setmealService.count();

        if (count2 > 0) {
            throw new CustomException("当前分类下关联套餐,不能删除");
        }
        super.removeById(ids);
    }

}
