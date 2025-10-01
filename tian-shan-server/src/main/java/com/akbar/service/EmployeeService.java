package com.akbar.service;

import com.akbar.dto.EmployeeDTO;
import com.akbar.dto.EmployeeLoginDTO;
import com.akbar.dto.EmployeePageQueryDTO;
import com.akbar.entity.Employee;
import com.akbar.result.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface EmployeeService extends IService<Employee> {

    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void save(EmployeeDTO employeeDTO);

    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    void startOrStop(Integer status, Long id);

    Employee getById(Long id);

    void updateById(EmployeeDTO employeeDTO);
}
