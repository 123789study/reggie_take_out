package org.example.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.reggie.entity.Category;
import org.example.reggie.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;

public interface CategoryService extends IService<Category> {
    void remove(Long ids);
}
