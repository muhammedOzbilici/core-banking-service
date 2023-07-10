package com.bank.corebankingservice.mapper;

import com.bank.corebankingservice.entity.Account;
import com.bank.corebankingservice.entity.Transaction;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface AccountMapper {
    @Insert("INSERT INTO accounts (customer_id, country, balances, " +
            "version, created_date, updated_date) VALUES (#{customerId}, #{country}, " +
            "#{balances}, #{version}, #{createdDate}, #{updatedDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    long createAccount(Account account);

    @Select("SELECT * FROM accounts WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "customerId", column = "customer_id"),
            @Result(property = "country", column = "country"),
            @Result(property = "version", column = "version"),
            @Result(property = "createdDate", column = "created_date"),
            @Result(property = "updated_date", column = "updated_date"),
            @Result(property = "balances", column = "id",
                    javaType = List.class,
                    many = @Many(
                            select = "getBalances",
                            fetchType = FetchType.EAGER
                    ))
    })
    Account getAccountById(@Param("id") Long accountId);

    @Select("SELECT * FROM accounts WHERE customerId = #{customerId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "customerId", column = "customer_id"),
            @Result(property = "country", column = "country"),
            @Result(property = "version", column = "version"),
            @Result(property = "createdDate", column = "created_date"),
            @Result(property = "updated_date", column = "updated_date"),
            @Result(property = "balances", column = "id",
                    javaType = List.class,
                    many = @Many(
                            select = "getBalances",
                            fetchType = FetchType.EAGER
                    ))
    })
    Account getAccountByCustomerId(@Param("customerId") Long customerId);

    @Select("SELECT t.id, t.account_id, t.amount, t.currency, t.direction, t.description " +
            "FROM (SELECT * FROM transactions WHERE account_id=#{id}) AS t LEFT JOIN " +
            "(SELECT * FROM accounts WHERE id= #{id}) AS a " +
            "ON a.id = t.account_id " +
            "ORDER BY t.id DESC LIMIT #{limit} OFFSET #{offset}")
    @Results(value = {
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "amount", column = "amount"),
            @Result(property = "currency", column = "currency"),
            @Result(property = "direction", column = "direction"),
            @Result(property = "description", column = "description"),
    })
    List<Transaction> getTransactions(
            @Param("id") Long id,
            @Param("offset") int offset,
            @Param("limit") Short limit
    );
}
