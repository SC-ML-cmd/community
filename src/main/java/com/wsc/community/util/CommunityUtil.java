package com.wsc.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class CommunityUtil {
    //生成随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    //MD5加密
    //只能加密，不能解密  hello -> adc123 密码简单，加密结果也简单
    //hello + 随机字符串  --> 再进行加密
    public static String md5(String key){
        //key 是null 空串、空格 都认为空
        if(StringUtils.isBlank(key)){
            return null;
        }

        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

}
