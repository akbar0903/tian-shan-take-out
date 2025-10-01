package com.akbar;

import com.akbar.constant.JwtClaimsConstant;
import com.akbar.entity.Employee;
import com.akbar.mapper.EmployeeMapper;
import com.akbar.properties.JwtProperties;
import com.akbar.utils.JwtUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
public class TianShanApplicationTest {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private EmployeeMapper employeeMapper;


    /**
     * 随机密钥生成测试
     * 然后写入到application.yml中
     */
    @Test
    public void generateRandomKeyTest() {
        String key = JwtUtil.generateRandomKey();
        System.out.println(key);
    }

    /**
     * 测试JWT生成
     */
    @Test
    public void jwtTest() {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, 1);

        // 生成token
        String token = JwtUtil.createJwt(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims
        );
        log.info("生成的token:{}", token);

        // 解析token
        Claims afterClaims = JwtUtil.parseJwt(jwtProperties.getAdminSecretKey(), token);
        log.info("解析后的claims:{}", afterClaims);
    }

    /**
     * bcrypt生成密码
     */
    @Test
    public void PasswordEncoderTest() {
        String hashpwd = BCrypt.hashpw("123456", BCrypt.gensalt());
        System.out.println("生成的密码：" + hashpwd);
        System.out.println("密码长度：" + hashpwd.length());
    }


    /**
     * mybatis-plus分页查询
     */
    @Test
    public void mybatisPlusPageTest() {
        Page<Employee> page = new Page<>(1, 10, true);

        employeeMapper.selectPage(page, null);

        List<Employee> records = page.getRecords();
        System.out.println("===========================================================");
        records.forEach(System.out::println);
        System.out.println("总记录数：" + page.getTotal());
    }
}
