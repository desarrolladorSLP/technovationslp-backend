package org.desarrolladorslp.technovation;

import static org.assertj.core.api.Assertions.assertThat;

import org.desarrolladorslp.technovation.config.auth.FirebaseService;
import org.desarrolladorslp.technovation.config.auth.firebase.FirebaseServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TechnovationBackendApplicationTests {

    @Autowired
    private FirebaseService firebaseService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void firebaseServiceIsNotFake() {
        assertThat(firebaseService).isInstanceOf(FirebaseServiceImpl.class);
    }

}

