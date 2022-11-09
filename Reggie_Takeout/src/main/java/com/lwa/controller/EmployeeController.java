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
        System.out.println("11111");
        //DigestUtils是一个工具类，里面的方法都使用了static进行修饰
        // md5DigestAsHex里面应该传的是一个数组，因此，我们应该使用password.getBytes()把String转换成数组。
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());//用此方法将获取的密码转为Byte数

        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        //数据库中Username标记了unique
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

        //参数为true时，若存在会话，则返回该会话，否则新建一个会话；
        //参数为false时，如存在会话，则返回该会话，否则返回NULL；
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);

    }


    /**
     * 员工退出
     * 用户点击页面中的退出按钮，发送请求，请求地址为/employee/logout，请求方式为POST。
     * 1、清理Session中的用户id
     * 2、返回结果
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中的用户id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
}
