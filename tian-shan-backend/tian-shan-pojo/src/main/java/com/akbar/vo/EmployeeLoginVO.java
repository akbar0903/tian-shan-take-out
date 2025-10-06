package com.akbar.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeLoginVO implements Serializable {

    private Long id;

    private String name;

    private String username;

    private String token;
}
