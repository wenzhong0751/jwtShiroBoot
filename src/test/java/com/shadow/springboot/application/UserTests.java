package com.shadow.springboot.application;

import com.shadow.springboot.application.domain.bo.Permission;
import com.shadow.springboot.application.domain.bo.User;
import com.shadow.springboot.application.repository.PermissionRepository;
import com.shadow.springboot.application.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Test
    public void testUser() {
        User user = new User("test","test","test","test");
        userRepository.save(user);
        Assert.assertEquals("test",userRepository.findByUsername("test").getPassword());
    }

    @Test
    public void testPermission(){
        List<Permission> list = permissionRepository.findAll();
        Assert.assertEquals(5,list.size());
    }
}
