package com.service;

import com.dao.UserMapper;
import com.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @date 2020/2/6 - 12:14
 */
@Service
@CacheConfig(cacheNames = "user")
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TokenService tokenService;
    @Autowired
    UserRoleService userRoleService;

    public User get(String username, String password){
        //Map<String,Object> map = new HashMap<String,Object>();
        return userMapper.getbynameandpassword(username,password);
    }

    @Cacheable
    public User findUserById(int id){
        System.out.println("未进入缓存");
        return userMapper.findUserbyId(id);
    }

    public User getbyname(String name){
        return userMapper.getbyname(name);
    }

    public int register(User user) {
        String username = user.getUsername();
        String email = user.getEmail();
        String password = user.getPassword();
        String cover = user.getCover();

        user.setUsername(username);
        user.setEmail(email);
        user.setCover(cover);
        user.setEnable(true);

        if (username.equals("") || password.equals("")) {
            return 0;
        }
        boolean exist = isExist(username);
        if (exist) {
            return 2;
        }
        userMapper.save(user);
        User newuser = getbyname(username);
        userRoleService.insertUserRole(newuser.getId(),2);
        return 1;
    }

    boolean isExist(String username){
        User user = userMapper.getbyname(username);
        return user!=null;
    }

    public List<User> list(){
        return userMapper.findall();
    }

    public void UpdateUser(User userdao){
        //System.out.println(userdao.toString());
        userMapper.update(userdao);
    }

    public void deleteUser(int id){
        userMapper.delete(id);
    }

    public void updateUserStatus(HttpServletRequest request){
        User userdao = tokenService.getUser(request);
        userMapper.update(userdao);
    }

    public void resetPassword(HttpServletRequest request){
        User userdao = tokenService.getUser(request);
        userdao.setPassword("123");
        userMapper.update(userdao);
    }

}
