package com.lwa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lwa.common.R;
import com.lwa.entity.Employee;
import com.lwa.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired(required = false)
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    //通过request对象getSession(Session存放用户id)
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        /**
         *1、将页面提交的密码password进行md5加密处理
         * 2、根据页面提交的用户名username查询数据库
         * 3、如果没有查询到则返回登录失败结果
         * 4、密码对比，如果不一样则返回登陆失败结果
         * 5、查看员工状态，如果已禁用，则返回员工已禁用结果
         * 6、登陆成功，将员工id存入Session并返回登陆成功结果
         */

        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());//用此方法将获取的密码转为Byte数

        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3、如果没有查询到则返回登录失败结果
        if(emp == null){
            return R.error("登陆失败");
        }

        //4、密码对比，如果不一样则返回登陆失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("密码错误，登陆失败");
        }

        //5、查看员工状态，如果已禁用，则返回员工已禁用结果
        if(emp.getStatus() == 0){//0表示禁用，1表示启用
            return R.error("账号已禁用，登陆失败");
        }

        //6、登陆成功，将员工id存入Session并返回登陆成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);

    }
}
