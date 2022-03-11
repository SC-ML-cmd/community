package com.wsc.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component //使用注解后可能无法再定义构造函数
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    private static final String REPLACEMENT = "***";

    //根节点
    private TrieNode rootNode = new TrieNode();

    //当容器调用构造器，实例化Bean对象后，将自动调用该方法
    @PostConstruct
    public void init(){

        // 使用tryWithResources自动释放资源
        try(//获取到的字节流
                InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                //将字节流转换为字符流 直接使用Reader并不合适，使用缓冲流效率更高
                BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream))
            ){
            String keyword;
            while((keyword = reader.readLine()) != null){
                //添加到前缀树
                this.addKeyword(keyword);
            }

        }catch (IOException e){
            logger.error("加载");
        }
    }

    /**
     * 过滤敏感词
     * @param text 待过滤的文本
     * @return 返回过滤后的文本
     */
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }

        // 指针1
        TrieNode tempNode = rootNode;
        // 指针2
        int begin = 0;
        // 指针3
        int position = 0;
        // 结果
        StringBuilder sb = new StringBuilder();

        while(begin < text.length()){
            char c = text.charAt(position);
            //跳过符号
            if(isSymbol(c)){
                // 指针1处于根节点，表示还没有开始判断敏感字符，指针2指针3后移略过即可。
                if(tempNode == rootNode){
                    sb.append(c);
                    begin++;
                }
                // 无论符号在开头或结尾，指针3都向下走一步
                position ++;
            }else{
                TrieNode subNode = tempNode.getSubNode(c);
                if(subNode == null){
                    sb.append(c);
                    begin ++;
                    position = begin;
                    tempNode = rootNode;
                }
                else {
                    if(subNode.isKeyWordEnd){
                        sb.append(REPLACEMENT);
                        position ++;
                        begin = position;
                        tempNode = rootNode;

                    }else{
                        position ++;
                        tempNode = subNode;
                    }
                }
            }

        }
        return sb.toString();
    }

    // 将一个敏感添加到前缀树中
    private void addKeyword(String keyword){
        if(keyword.length() == 0){
            return;
        }
        TrieNode tempNode = rootNode;
        for(int i = 0; i < keyword.length(); i++){
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            // 尝试直接获取子节点，若获取不到，则创建子节点，并添加
            if(subNode == null){
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }

            tempNode = subNode;
            // 设置结束标志
            if(i == keyword.length() - 1){
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    // 判断是否为符号
    private boolean isSymbol(Character c){
        // 0x2E80 - 0x9FFF 表示东亚符号
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    //前缀树节点
    private class TrieNode{
        //关键词结束标识
        private boolean isKeyWordEnd;

        //子节点， key为下级字符，value为下级节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }

        //添加子节点
        public void addSubNode(Character c, TrieNode node){
            subNodes.put(c, node);
        }

        //获取子节点
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }

}
