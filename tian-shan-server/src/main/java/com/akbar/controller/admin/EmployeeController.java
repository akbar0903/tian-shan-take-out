package com.akbar.controller.admin;

import com.akbar.constant.JwtClaimsConstant;
import com.akbar.dto.EmployeeDTO;
import com.akbar.dto.EmployeeLoginDTO;
import com.akbar.dto.EmployeePageQueryDTO;
import com.akbar.entity.Employee;
import com.akbar.properties.JwtProperties;
import com.akbar.result.PageResult;
import com.akbar.result.Result;
import com.akbar.service.EmployeeService;
import com.akbar.utils.JwtUtil;
import com.akbar.vo.EmployeeLoginVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtProperties jwtProperties;


    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        Employee employee = employeeService.login(employeeLoginDTO);

        // 生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJwt(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims
        );

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .username(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }


    /**
     * 登出
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success();
    }


    /**
     * 新增员工
     */
    @PostMapping
    public Result<Void> save(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        employeeService.insert(employee);
        return Result.success();
    }


    /**
     * 员工分页查询
     */
    @GetMapping("/page")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {

        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);

        return Result.success(pageResult);
    }


    /**
     * 启用禁用员工账号
     */
    @PostMapping("/status/{status}")
    public Result<Void> startOrStop(@PathVariable("status") Integer status, Long id) {
        employeeService.startOrStop(status, id);
        return Result.success();
    }


    /**
     * 回显员工信息
     */
    @GetMapping("/{id}")
    public Result<Employee> get(@PathVariable("id") Long id) {
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }


    /**
     * 更新员工信息
     */
    @PutMapping
    public Result<Void> update(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        employeeService.update(employee);
        return Result.success();
    }
}
