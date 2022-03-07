package com.wsc.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public class AlphaDaoMyBatisIMpl implements AlphaDao{
    @Override
    public String select() {
        return "Mybatis";
    }
}
