package org.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.example.reggie.entity.Employee;
import org.example.reggie.common.R;
import org.example.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        // 1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        var emp = employeeService.getOne(queryWrapper);

        // 3、如果没有查询到则返回登录失败结果
        if (emp == null) {
            return R.error("登录失败");
        }

        // 4、密码比对，如果不一-致则返回登录失败结果
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败");
        }

        //  5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0) {
            return R.error("账号已经被禁用");
        }

        //   6、登录成功，将员Tid存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());

        return R.success(emp);
    }


    /**
     * 员工退出
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        log.info("退出");
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     *
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工,员工信息:{}", employee.toString());

        // 设置初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

       /* employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //获取当前用户的id
        final var empId = (long) request.getSession().getAttribute("employee");

        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);*/

        // 新增用户
        employeeService.save(employee);

        return R.success("添加成功");
    }


    /**
     * 员工分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page:{},pageSize:{},name:{}",page,pageSize,name);

        // 构造分页构造器
        Page pageInfo =new Page(page,pageSize);

        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper();

        //添加过滤条件
        queryWrapper.like(StringUtils.isNotBlank(name),Employee::getName,name);

        // 添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 禁用功能
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());

        var empId = (long)request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);

        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工");
        final var employee = employeeService.getById(id);
        if (employee!=null){
            return R.success(employee);
        }else{
            return R.error("没有查询到该员工信息");
        }

    }
}
