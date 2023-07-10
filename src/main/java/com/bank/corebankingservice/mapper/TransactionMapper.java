package com.bank.corebankingservice.mapper;

import com.bank.corebankingservice.entity.Transaction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface TransactionMapper {
    @Insert("INSERT INTO transactions(account_id, amount, currency, direction, description, balance, createdDate) " +
            "VALUES(#{accountId},#{amount},#{currency}::valid_currencies,#{direction}::valid_directions,#{description}," +
            "#{balance}, #{createdDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    long createTransaction(Transaction transaction);

}
