package com.d9.seckill;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")  // 指定加载 application-test.yml
class SeckillApplicationTests {

	@Test
	void contextLoads() {
	}

}
