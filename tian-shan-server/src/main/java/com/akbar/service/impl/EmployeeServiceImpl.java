package com.akbar.service.impl;

import com.akbar.annotation.AutoFill;
import com.akbar.constant.MessageConstant;
import com.akbar.constant.PasswordConstant;
import com.akbar.constant.StatusConstant;
import com.akbar.dto.EmployeeLoginDTO;
import com.akbar.dto.EmployeePageQueryDTO;
import com.akbar.entity.Employee;
import com.akbar.enumeration.OperationType;
import com.akbar.exception.AccountLockedException;
import com.akbar.exception.AccountNotFoundException;
import com.akbar.exception.PasswordErrorException;
import com.akbar.mapper.EmployeeMapper;
import com.akbar.result.PageResult;
import com.akbar.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;


    /**
     * 登录
     */
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>().eq(Employee::getUsername, username);

        Employee employee = employeeMapper.selectOne(queryWrapper);
        // 验证账号是否存在
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 验证密码
        if (!BCrypt.checkpw(password, employee.getPassword())) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        // 验证账户状态
        if (!employee.getStatus().equals(StatusConstant.ENABLE)) {
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        return employee;
    }


    /**
     * 新增员工
     */
    @AutoFill(OperationType.INSERT)
    @Override
    public void insert(Employee employee) {

        // 设置帐号状态
        employee.setStatus(StatusConstant.ENABLE);
        // 设置初始密码
        employee.setPassword(BCrypt.hashpw(PasswordConstant.DEFAULT_PASSWORD, BCrypt.gensalt()));

        employeeMapper.insert(employee);
    }


    /**
     * 分页查询员工
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        Page<Employee> page = Page.of(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        // 根据姓名模糊查询
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>()
                .like(employeePageQueryDTO.getName() != null, Employee::getName, employeePageQueryDTO.getName())
                .orderByDesc(Employee::getCreateTime);
        employeeMapper.selectPage(page, queryWrapper);

        return new PageResult(page.getTotal(), page.getRecords());
    }


    /**
     * 启用禁用员工账号
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Employee employee = Employee.builder()
                .id(id)
                .status(status)
                .build();

        employeeMapper.updateById(employee);
    }


    /**
     * 回显员工信息
     */
    @Override
    public Employee getById(Long id) {
        return employeeMapper.selectById(id);
    }


    /**
     * 更新员工信息
     */
    @AutoFill(OperationType.UPDATE)
    @Override
    public void update(Employee employee) {

        employeeMapper.updateById(employee);
    }
}
