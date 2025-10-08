package com.akbar.service;

import com.akbar.entity.AddressBook;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface AddressBookService extends IService<AddressBook> {
    List<AddressBook> getAddressBookList();

    void addAddressBook(AddressBook addressBook);

    AddressBook getDefault();

    void update(AddressBook addressBook);

    void deleteById(Long id);

    void setDefault(AddressBook addressBook);
}
