package com.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @date 2020/2/15 - 14:41
 */
@Service
@CacheConfig(cacheNames = "token")
public class TokenService {
    @Autowired
    UserService userService;

    // 将 user id 保存到 token 里面 以 password 作为 token 的密钥

    public String getToken(User user) {
        String token="";
        String userid = String.valueOf(user.getId());
        token= JWT.create().withAudience(userid)
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }


    @Cacheable
    public User getUser(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("token");
        String userId = JWT.decode(token).getAudience().get(0);
        User user = userService.findUserById(Integer.valueOf(userId).intValue());
        return user;
    }
}
