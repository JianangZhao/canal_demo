package org.example.handler;

import org.example.model.User;
import org.example.test.CanalListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

@Component
@CanalTable(value = "user")
public class UserHandler implements EntryHandler<User> {

    @Autowired
    CanalListener canalListener;

    @Override
    public void insert(User user){
        System.out.println("添加：" + user);
    }

    @Override
    public void update(User before, User after){
        System.out.println("改前：" + before);
        System.out.println("改后：" + after);
    }

    @Override
    public void delete(User user){
        System.out.println("删除：" + user);
    }
}
