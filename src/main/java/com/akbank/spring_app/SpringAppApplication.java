package com.akbank.spring_app;

import com.akbank.spring_app.domain.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
public class SpringAppApplication {

	public static void main(String[] args) {

//	 ApplicationContext context =	SpringApplication.run(SpringAppApplication.class, args);
//	 test(context);

		SpringApplication.run(SpringAppApplication.class, args);
	}


	public static void test(ApplicationContext context) {

		String message =  context.getBean("exampleBean", String.class);
		System.out.println(message);

		// User class Bean çalıştırma
		// 2. Senaryo Aynı Classtan birden fazla Bean çağırırken Bean name kullanmalıyız.
		User user = context.getBean("user",User.class);
		System.out.println("User Name: " + user.getName());

		// 3. Senaryo User Service içindeki methodu çağırma, Bean üzerinden başka Bean çağırma
		UserService userService = context.getBean(UserService.class);
		userService.handleUser();
	}


	@Bean
	public  String exampleBean() {
		return "This is an example bean";
	}

	@Bean(name = "exampleBean2")
	public  String exampleBean2() {
		return "This is an example bean";
	}

	// Auto Registration of Beans
	@Bean
	public User user() {
		User usr =  new User();
		usr.setName("John");
		return usr;
	}

	// Senaryo User Service denilen bir sınıfın içerisinde User bean'e ihtiyaç var nasıl yapmamız lazım.


	@Bean
	@Scope("prototype")
	public User user2() {
		User usr =  new User();
		usr.setName("Alice");
		return usr;
	}

	// Bean tanımları biz bu tanımları default olarak kulanırsak singelton olur
	// Bean scope larını değiştirmek için @Scope anotasyonu kullanılır
	// @Scope("prototype") ile bean her çağrıldığında yeni bir instance oluşturulur
	public class  User {

		@PostConstruct
		public void init() {
			System.out.println("User bean is initialized: " + name);
		}

		@PreDestroy
		public void destroy() {
			System.out.println("User bean is being destroyed: " + name);
		}

		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

}
