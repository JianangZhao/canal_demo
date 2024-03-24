package org.example;


import org.example.test.CanalListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class App {

    @Autowired
    CanalListener canalListener;

    @Test
    public void testCanalLister(){
        canalListener.printCanalLog();
    }

}
