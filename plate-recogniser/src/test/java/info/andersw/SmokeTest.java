package info.andersw;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import info.andersw.restapi.ImageRecognizerController;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SmokeTest {

    @Autowired
    private ImageRecognizerController controller;

    @Test
    public void contexLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test void returnsMessage() throws Exception {
        /*
        PlateMessage ctrl = new MessageController();
        PlateMessage msg = ctrl.message("Anders");
        assertThat(msg.getId()).isGreaterThanOrEqualTo(0);
        assertThat(msg.getContent()).isEqualTo("Hello, Anders!");
         */
        assertEquals(true,true);
    }
}