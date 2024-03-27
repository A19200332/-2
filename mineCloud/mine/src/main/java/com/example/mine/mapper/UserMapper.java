package com.example.mine.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mine.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
