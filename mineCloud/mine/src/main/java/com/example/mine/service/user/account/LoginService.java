package com.example.mine.service.user.account;

import java.util.HashMap;
import java.util.Map;

public interface LoginService {
    public Map<String,String> getToken (String username,String password);
}
