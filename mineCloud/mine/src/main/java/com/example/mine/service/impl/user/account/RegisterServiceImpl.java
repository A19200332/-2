package com.example.mine.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mine.mapper.UserMapper;
import com.example.mine.pojo.User;
import com.example.mine.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Override
    public Map<String, String> register(String username, String password, String confirmPassword) {
        Map<String,String> map= new HashMap<>();
        if(username==null){
            map.put("error_massage","用户名不能为空");
            return map;
        }
        if(password==null||confirmPassword==null){
            map.put("error_massage","密码不能为空");
            return map;
        }
        username= username.trim();
        if(username.length()==0){
            map.put("error_massage","用户名不能为空");
            return map;
        }
        if(password.length()==0||confirmPassword.length()==0){
            map.put("error_massage","密码不能为空");
            return map;
        }
        if(username.length()>100){
            map.put("error_massage","用户名不能超过100");
            return map;
        }
        if(password.length()>100||confirmPassword.length()>100){
            map.put("error_massage","密码长度不能超过100");
            return map;
        }
        if(!password.equals(confirmPassword)){
            map.put("error_massage","两次密码不一致");
            return map;
        }
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        List<User>users = userMapper.selectList(queryWrapper);
        if(!users.isEmpty()){
             map.put("error_massage","用户名已存在");
             return map;
        }
        String encodepassword = passwordEncoder.encode(password);
        String photo = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201809%2F01%2F20180901214613_VXdRf.thumb.700_0.jpeg&refer=http%3A%2F%2Fb-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663728942&t=d87013cddc9983e615ae90ef74bf4487";
        String surface = password;
        User user =new User(null,username,encodepassword,photo,surface,1500);
        userMapper.insert(user);
        map.put("error_massage","success");
        return map;
    }
}
