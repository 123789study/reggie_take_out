package org.example.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.reggie.mapper.EmployeeMapper;
import org.example.reggie.entity.Employee;
import org.example.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
