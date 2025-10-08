package com.akbar.service.impl;

import com.akbar.constant.MessageConstant;
import com.akbar.context.BaseContext;
import com.akbar.entity.AddressBook;
import com.akbar.exception.DefaultAddressNotFoundException;
import com.akbar.mapper.AddressBookMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements com.akbar.service.AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 查询当前登录用户的所有地址信息
     */
    @Override
    public List<AddressBook> getAddressBookList() {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<AddressBook>()
                .eq(AddressBook::getUserId, userId)
                .orderByDesc(AddressBook::getIsDefault);
        return addressBookMapper.selectList(wrapper);
    }


    /**
     * 新增收获地址
     */
    @Override
    public void addAddressBook(AddressBook addressBook) {
        Long userId = BaseContext.getCurrentId();
        addressBook.setUserId(userId);
        addressBookMapper.insert(addressBook);
    }


    /**
     * 查询默认地址
     */
    @Override
    public AddressBook getDefault() {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<AddressBook>()
                .eq(AddressBook::getUserId, userId)
                .eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = addressBookMapper.selectOne(wrapper);
        if (addressBook == null) {
            throw new DefaultAddressNotFoundException(MessageConstant.DEFAULT_ADDRESS_NOT_FOUND);
        }
        return addressBook;
    }


    /**
     * 修改收货地址
     */
    @Override
    public void update(AddressBook addressBook) {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<AddressBook>()
                .eq(AddressBook::getUserId, userId)
                .eq(AddressBook::getId, addressBook.getId());
        addressBookMapper.update(addressBook, wrapper);
    }


    /**
     * 根据id删除收货地址
     */
    @Override
    public void deleteById(Long id) {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<AddressBook>()
                .eq(AddressBook::getUserId, userId)
                .eq(AddressBook::getId, id);
        addressBookMapper.delete(wrapper);
    }


    /**
     * 设置默认地址
     */
    @Override
    public void setDefault(AddressBook addressBook) {
        Long userId = BaseContext.getCurrentId();

        // 先将所有地址的默认值设置为0
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<AddressBook>()
                .eq(AddressBook::getUserId, userId)
                .eq(AddressBook::getIsDefault, 1);
        AddressBook adsb = new AddressBook();
        adsb.setIsDefault(0);
        addressBookMapper.update(adsb, wrapper);

        // 设置指定id的地址为默认地址
        addressBook = AddressBook.builder()
                .id(addressBook.getId())
                .userId(userId)
                .isDefault(1)
                .build();
        addressBookMapper.updateById(addressBook);
    }
}
