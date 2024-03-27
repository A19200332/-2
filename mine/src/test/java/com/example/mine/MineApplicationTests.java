package com.example.mine;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mine.mapper.UserMapper;
import com.example.mine.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

@SpringBootTest
@RestController
class MineApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Test
    void contextLoads() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String ps = passwordEncoder.encode("123");
        System.out.println(ps);
    }
    @Test
    void add100(){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        for(int i=2;i<=100;i++)
        {
            String username = new String();
            String password = new String();
            for(int j=1;j<=4;j++)
            {
                username+=(char)((int)(Math.random()*26)%26+'a');
            }
            for(int j=1;j<=3;j++){
                password+=(char)((int)(Math.random()*26)%26+'a');
            }
            for(int j=1;j<=3;j++){
                password+=(char)((int)(Math.random()*9)%9+'0');
            }
            QueryWrapper queryWrapper = new QueryWrapper<>();
            String inner = passwordEncoder.encode(password);
            User user = new User(i,username,inner,null,password);
            userMapper.insert(user);
        }
        return;
    }
    @Test
    void delete100()
    {
        for(int i=2;i<=100;i++)
        {
            userMapper.deleteById(i);
        }
        return;
    }




}
