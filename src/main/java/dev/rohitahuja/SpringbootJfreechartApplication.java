package dev.rohitahuja;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Push
@SpringBootApplication
@Theme("doc-chat")
public class SpringbootJfreechartApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootJfreechartApplication.class, args);
	}

}
