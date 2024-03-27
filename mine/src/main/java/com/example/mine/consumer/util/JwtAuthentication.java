package com.example.mine.consumer.util;

import com.example.mine.utils.JwtUtil;
import io.jsonwebtoken.Claims;

public class JwtAuthentication {
    public static Integer getUser (String token){
        Integer userId;
        try{
            Claims claims = JwtUtil.parseJWT(token);
            userId = Integer.parseInt(claims.getSubject());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return userId;
    }
}
