package com.wsc.community.service;

import com.wsc.community.dao.AlphaDao;
import com.wsc.community.dao.DiscussPostMapper;
import com.wsc.community.dao.UserMapper;
import com.wsc.community.entity.DiscussPost;
import com.wsc.community.entity.User;
import com.wsc.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

//@Scope("prototype")
@Service
public class AlphaService {

    @Autowired
    private AlphaDao alphaDao;

    @Autowired
    UserMapper userMapper;

    @Autowired
    DiscussPostMapper discussPostMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;


    public AlphaService(){
        System.out.println("实例化AlphaService");
    }

    @PostConstruct //初始化方法
    public void init(){
        System.out.println("初始化AlphaService");
    }

    @PreDestroy //销毁方法
    public void destroy(){
        System.out.println("销毁AlphaService");
    }

    public String find() {
        return alphaDao.select();
    }

    // 注册用户，并增加一个贴子
    //通过注解将该方法中的DAO操作形成一个事务
    // 传播机制
    // REQUIRED  支持当前事务 A->B  A 为外部事务， 若存在外部事务使用外部事务，否则不存在创建新事务。
    // REQUIRED_NEW 暂停外部事务，创建新事务
    // NESTED: 如果存在外部事务，则嵌套在该事务中执行，否则和REQUIRED相同。
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Object save1(){
        //新增用户
        User user = new User();
        user.setUsername("alpha");
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5("123" + user.getSalt()));
        user.setEmail("alpha@qq.com");
        user.setHeaderUrl("http://image.nowcoder.com/head/99t.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        // 新增帖子
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle("HELLO");
        post.setContent("新人报道");
        post.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(post);

        Integer.valueOf("abc");

        return "OK";
    }

    public Object save2(){
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                // 回调方法
                //新增用户
                User user = new User();
                user.setUsername("beta");
                user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
                user.setPassword(CommunityUtil.md5("123" + user.getSalt()));
                user.setEmail("alpha@qq.com");
                user.setHeaderUrl("http://image.nowcoder.com/head/999t.png");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);
                // 新增帖子
                DiscussPost post = new DiscussPost();
                post.setUserId(user.getId());
                post.setTitle("你好！");
                post.setContent("新人报道");
                post.setCreateTime(new Date());
                discussPostMapper.insertDiscussPost(post);

                Integer.valueOf("abc");

                return "OK";
            }
        });
        return "OK";
    }

}
