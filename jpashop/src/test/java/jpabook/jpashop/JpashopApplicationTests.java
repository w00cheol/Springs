package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JpashopApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void defaultTest() {
		Hello hello = new Hello();
		hello.setData("name");
		String data = hello.getData();
		Assertions.assertThat(data).isEqualTo("name");
	}
}
