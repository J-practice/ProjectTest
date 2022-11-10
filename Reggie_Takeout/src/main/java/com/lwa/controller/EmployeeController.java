package com.lwa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lwa.common.R;
import com.lwa.entity.Employee;
import com.lwa.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.util.DigestUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

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
        //test
        System.out.println("11111");

        //1、将页面提交的密码password进行md5加密处理

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

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee.toString());
        //设置初始密码为123456，进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //（获取当前系统时间）这条记录的创建时间
        employee.setCreateTime(LocalDateTime.now());
        //这条记录的更新时间
        employee.setUpdateTime(LocalDateTime.now());
        //获得当前登录用户ID
        Long empId = (Long)request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        //使用异常处理器进行全局异常捕获

        employeeService.save(employee);

        return R.success("新增员工成功");
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize,String name){
        log.info("page = {}, pageSize = {}, name = {}",page, pageSize, name);

        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加一个过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);//查询名字用like
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }
}
