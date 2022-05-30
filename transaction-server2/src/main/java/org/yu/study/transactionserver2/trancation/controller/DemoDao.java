package org.yu.study.transactionserver2.trancation.controller;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DemoDao {

    @Insert("insert into yhy_user(name) values(#{name})")
    public void insert(@Param("name") String name);
}
