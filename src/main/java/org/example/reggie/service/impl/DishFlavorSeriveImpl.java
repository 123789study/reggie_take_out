package org.example.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.reggie.entity.DishFlavor;
import org.example.reggie.mapper.DishFlavorMapper;
import org.example.reggie.service.DishFlavorSerive;
import org.example.reggie.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorSeriveImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorSerive {

}
