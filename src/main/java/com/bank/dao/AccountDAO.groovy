package com.bank.dao

import com.bank.entity.Account

/**
 * Created by Ник on 06.07.2017.
 */
interface AccountDAO {

   Account findByUsername(String username)
}