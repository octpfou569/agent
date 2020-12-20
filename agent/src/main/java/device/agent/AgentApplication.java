package device.agent;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AgentApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(AgentApplication.class)
        .web(WebApplicationType.NONE)
        .run(args);
	}
}
