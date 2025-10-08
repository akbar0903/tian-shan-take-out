package com.akbar.controller.user;

import com.akbar.entity.AddressBook;
import com.akbar.result.Result;
import com.akbar.service.AddressBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Tag(name = "C端地址簿接口")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;


    /**
     * 查询当前登录用户的所有地址信息
     */
    @GetMapping("/list")
    @Operation(summary = "查询当前登录用户的所有地址信息")
    public Result<List<AddressBook>> list() {
        List<AddressBook> list = addressBookService.getAddressBookList();
        return Result.success(list);
    }


    /**
     * 新增收获地址
     */
    @PostMapping
    @Operation(summary = "新增收获地址")
    public Result<Void> add(@RequestBody AddressBook addressBook) {
        addressBookService.addAddressBook(addressBook);
        return Result.success();
    }


    /**
     * 查询默认地址
     */
    @GetMapping("/default")
    @Operation(summary = "查询默认地址")
    public Result<AddressBook> getDefault() {
        AddressBook addressBook = addressBookService.getDefault();
        return Result.success(addressBook);
    }


    /**
     * 修改收货地址
     */
    @PutMapping
    @Operation(summary = "修改收货地址")
    public Result<Void> update(@RequestBody AddressBook addressBook) {
        addressBookService.update(addressBook);
        return Result.success();
    }


    /**
     * 回显收货地址
     */
    @GetMapping("/{id}")
    @Operation(summary = "回显收货地址")
    public Result<AddressBook> get(@PathVariable("id") Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 根据id删除收货地址
     * 这里一定要有"/"，不然删除失败，这应该前端的问题
     */
    @DeleteMapping("/")
    @Operation(summary = "根据id删除收货地址")
    public Result<Void> delete(Long id) {
        addressBookService.deleteById(id);
        return Result.success();
    }


    /**
     * 设置默认地址
     */
    @PutMapping("/default")
    @Operation(summary = "设置默认地址")
    public Result<Void> setDefault(@RequestBody AddressBook addressBook) {
        log.info("设置默认地址：{}", addressBook);
        addressBookService.setDefault(addressBook);
        return Result.success();
    }
}
