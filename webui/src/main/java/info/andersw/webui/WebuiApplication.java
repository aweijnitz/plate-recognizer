package info.andersw.webui;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@CommonsLog
public class WebuiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebuiApplication.class, args);
	}

}
