package org.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.reggie.common.R;
import org.example.reggie.entity.Category;
import org.example.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    /**
     * 菜品类型
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category {}",category);
        categoryService.save(category);
        return R.success("添加分类成功");
    }


    /**
     * 菜品分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        log.info("执行分页操作");

        // 构造分页构造器
        Page<Category> pageInfo =new Page<>(page,pageSize);

        // 构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper();

        // 添加排序
        queryWrapper.orderByAsc(Category::getSort);

        // 进行分页查询
        categoryService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据id删除菜品分类
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("删除分类,id为:{}",ids);

        categoryService.remove(ids);

        return R.success("分类信息删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息:{}",category);

        categoryService.updateById(category);
        return R.success("修改分类成功");
    }

    /**
     * 查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){

        // 条件构造器
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        // 添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        return  R.success(categoryService.list(queryWrapper));
    }
}
