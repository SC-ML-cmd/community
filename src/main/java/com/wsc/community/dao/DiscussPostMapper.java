package com.wsc.community.dao;

import com.wsc.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
    //方法需要支持分页
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    //记录数/每页显示记录数 = 行号
    //需要动态ping
    //@Param注解用于给参数取别名
    //若只有一个参数，并且在 <if> 动态选取，必须添加别名
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);

    int updateCommentCount(int id, int commentCount);
}
