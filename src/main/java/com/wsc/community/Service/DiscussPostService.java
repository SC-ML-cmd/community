package com.wsc.community.Service;

import com.wsc.community.dao.DiscussPostMapper;
import com.wsc.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit){
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    public int findDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }
    //显示用户时，不能显示用户userId,应该显示用户名，两种方式可以实现：
    //使用Sql拼接JOIN 直接查询到帖子内容和用户名
    //先查询到贴子内容，再单独对应查询用户名
}
