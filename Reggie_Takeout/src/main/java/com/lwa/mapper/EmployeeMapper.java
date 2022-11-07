package com.lwa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lwa.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
