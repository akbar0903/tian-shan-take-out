package com.akbar.controller.admin;

import com.akbar.dto.SetmealDTO;
import com.akbar.dto.SetmealPageQueryDTO;
import com.akbar.result.PageResult;
import com.akbar.result.Result;
import com.akbar.service.SetmealService;
import com.akbar.vo.SetmealVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Tag(name = "套餐管理")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;


    /**
     * 添加套餐
     */
    @Operation(summary = "添加套餐")
    @PostMapping
    public Result<Void> save(@RequestBody SetmealDTO setmealDTO) {
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }


    /**
     * 分页获取套餐列表
     */
    @GetMapping("/page")
    @Operation(summary = "分页获取套餐列表")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 批量删除套餐
     */
    @DeleteMapping
    @Operation(summary = "批量删除套餐")
    public Result<Void> delete(@RequestParam List<Long> ids) {
        setmealService.deleteBatch(ids);
        return Result.success();
    }


    /**
     * 根据id查询套餐
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        SetmealVO setmealVO = setmealService.getByIdWithDish(id);
        return Result.success(setmealVO);
    }


    /**
     * 修改套餐
     */
    @PutMapping
    @Operation(summary = "修改套餐")
    public Result<Void> update(@RequestBody SetmealDTO setmealDTO) {
        setmealService.update(setmealDTO);
        return Result.success();
    }


    /**
     * 套餐起售停售
     */
    @PostMapping("/status/{status}")
    @Operation(summary = "套餐起售停售")
    public Result<Void> startOrStop(@PathVariable Integer status, Long id) {
        setmealService.startOrStop(status, id);
        return Result.success();
    }
}
