package com.wsc.community.service;

import com.wsc.community.dao.DiscussPostMapper;
import com.wsc.community.entity.DiscussPost;
import com.wsc.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit){
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    public int findDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }
    //显示用户时，不能显示用户userId,应该显示用户名，两种方式可以实现：
    //使用Sql拼接JOIN 直接查询到帖子内容和用户名
    //先查询到贴子内容，再单独对应查询用户名

    public int addDiscussPost(DiscussPost post){
        if(post == null){
            throw new RuntimeException("参数不能为空！");
        }

        //转义HTML标签
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        //过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        return discussPostMapper.insertDiscussPost(post);
    }

    public DiscussPost findDiscussPost(int id){
        return discussPostMapper.selectDiscussPostById(id);
    }

    public int updateCommentCount(int id, int commentCount){
        return discussPostMapper.updateCommentCount(id, commentCount);
    }
}
