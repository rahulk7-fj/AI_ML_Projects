package com.spring_rag.ai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class AiApplicationTests {

	@MockBean
	private VectorStore vectorStore;
	@Test
	void contextLoads() {
	}

}
