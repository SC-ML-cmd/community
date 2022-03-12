package com.wsc.community.controller;

import com.wsc.community.entity.Comment;
import com.wsc.community.entity.Page;
import com.wsc.community.service.CommentService;
import com.wsc.community.service.DiscussPostService;
import com.wsc.community.service.UserService;
import com.wsc.community.entity.DiscussPost;
import com.wsc.community.entity.User;
import com.wsc.community.util.CommunityConstant;
import com.wsc.community.util.CommunityUtil;
import com.wsc.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping(path = "/discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @RequestMapping(path = "add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "你还没有进行登录");
        }

        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPostService.addDiscussPost(discussPost);

        return CommunityUtil.getJSONString(0, "发布成功！");
        //对于报错的异常进行统一处理
    }

    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page) {
        // 查询帖子
        DiscussPost post = discussPostService.findDiscussPost(discussPostId);
        model.addAttribute("post", post);
        // 可以关联查讯，通过用户的id获取到用户的信息
        // 可以通过userId再次查询用户的信息 效率较低 之后对有效率影响的位置，使用redis进行优化
        // 作者信息
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);

        //评论分页信息  当前页面号由界面传入记录起始 = （当前页面号 - 1）* 每页评论数  结束为 当前页面数 * 每页评论数
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(post.getCommentCount()); //帖子表上有一个评论个数字段，这个字段本来应该通过多表查询实现，但现在增加这个字段用于提高效率
        // 评论: 给帖子的评论
        // 回复: 对评论的评论
        List<Comment> commentList = commentService.findCommentByEntity(ENTITY_TYPE_POST, post.getId(), page.getOffset(),
                page.getLimit());
        // 评论VO列表
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if(commentList != null){
            // 直接返回评论并不合适，每条评论中还需要有用户信息
            for (Comment comment : commentList){
                // 评论VO
                Map<String, Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment", comment);
                commentVo.put("user", userService.findUserById(comment.getUserId()));

                // 每条评论中还要有回复的信息
                List<Comment> replyList = commentService.findCommentByEntity(
                        ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                //回复的VO列表
                List<Map> replyVoList = new ArrayList<>();
                if(replyList != null){
                    for(Comment reply : replyList){
                        Map<String, Object> replyVo = new HashMap<>();
                        //回复信息
                        replyVo.put("reply", reply);
                        //作者信息
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        //回复目标
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);

                        replyVoList.add(replyVo);
                    }
                }
                // 回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);
                commentVo.put("replys", replyVoList);
                commentVoList.add(commentVo);

            }
        }
        model.addAttribute("comments", commentVoList);
        return "/site/discuss-detail";
    }


}
