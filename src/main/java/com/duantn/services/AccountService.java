package com.duantn.services;

import com.duantn.entitys.Account;

public interface AccountService {
    Account login(String email, String password);

    Account register(Account account);
}
