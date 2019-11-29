package com.mongodb.util;

import com.mongodb.entity.User;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * <h3>Mongodb_Project</h3>
 *
 * @author : zoyoy
 * @date : 2019-11-28 17:57
 **/
public class InsertTest {

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void iTest() throws IllegalArgumentException, IllegalAccessException {
        for(int i=0;i<10;i++){
            User user = new User();
            user.setNickname("测试"+i);
            user.setPassword(CookieUtils.md5Encrypt("123"));
            user.setUsername("测试"+i);
            user.setMotto("该用户比较懒哦~");
            user.setAvatar("favicon.jpg");
            System.out.println(user);
            mongoTemplate.save(user);
        }
    }



}
