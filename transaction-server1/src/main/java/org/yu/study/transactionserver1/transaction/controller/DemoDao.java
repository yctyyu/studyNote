package org.yu.study.transactionserver1.transaction.controller;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DemoDao {

    @Insert("insert into user_info(name,id) values(#{name},1)")
    void insert(@Param("name") String name);
}
