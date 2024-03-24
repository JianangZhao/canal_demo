package org.example.handler;

import org.example.model.User;
import org.example.test.CanalListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

@Component
@CanalTable(value = "user")
public class UserHandler implements EntryHandler<User> {
    Logger log = LoggerFactory.getLogger(UserHandler.class);
    @Autowired
    CanalListener canalListener;

    @Override
    public void insert(User user){
        log.info("添加：" + user);
    }

    @Override
    public void update(User before, User after){
        log.info("改前：" + before);
        log.info("改后：" + after);
    }

    @Override
    public void delete(User user){
        log.info("删除：" + user);
    }
}
