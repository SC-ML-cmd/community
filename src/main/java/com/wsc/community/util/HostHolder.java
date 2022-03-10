package com.wsc.community.util;

import com.wsc.community.entity.User;
import org.springframework.stereotype.Component;

@Component
public class HostHolder {

    private ThreadLocal<User> userLocal = new ThreadLocal<>();

    public void setUser(User user){
        userLocal.set(user);
    }

    public User getUser(){
        return userLocal.get();
    }

    public void clear(){
        userLocal.remove();
    }
}
