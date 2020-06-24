package info.andersw;
import static org.assertj.core.api.Assertions.assertThat;

import info.andersw.model.Message;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SmokeTest {

    @Autowired
    private MessageController controller;

    @Test
    public void contexLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test void returnsMessage() throws Exception {
        MessageController ctrl = new MessageController();
        Message msg = ctrl.message("Anders");
        assertThat(msg.getId()).isGreaterThanOrEqualTo(0);
        assertThat(msg.getContent()).isEqualTo("Hello, Anders!");
    }
}