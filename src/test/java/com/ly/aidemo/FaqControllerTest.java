package com.ly.aidemo;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.evaluation.RelevancyEvaluator;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WebAppConfiguration
class FaqControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	@Qualifier("openAiChatModel")
	private ChatModel aiClient;

	@Autowired
	private ChatModel chatModel;

	@Autowired
	private VectorStore vectorStore;

	@Test
	void givenGetCatHaiku_whenCallingAiClient_thenCorrect() throws Exception {
		mockMvc.perform(post("/api/v1/generate")
				.contentType(MediaType.APPLICATION_JSON) // Especifique o formato do corpo (JSON, XML, etc.)
				.content("{\"nome\":\"gato\"}")) // Defina o conteúdo do corpo da requisição
				.andExpect(status().isOk())
				.andExpect(content().string(containsStringIgnoringCase("cat")));

	}

	@Test
	void givenGetPoetryWithGenreAndTheme_whenCallingAiClient_thenCorrect() throws Exception {
		String genre = "lyric";
		String theme = "coffee";
		mockMvc.perform(get("/ai/poetry?genre={genre}&theme={theme}", genre, theme))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.genre").value(containsStringIgnoringCase(genre)))
				.andExpect(jsonPath("$.theme").value(containsStringIgnoringCase(theme)))
				.andExpect(jsonPath("$.poetry").isNotEmpty())
				.andExpect(jsonPath("$.title").exists());
	}

	@Test
	void testEvaluation() {

		String userText = "Tem wi-fi?";

		ChatResponse response = ChatClient.builder(chatModel)
				.build().prompt()
				.advisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()))
				.user(userText)
				.call()
				.chatResponse();

		var relevancyEvaluator = new RelevancyEvaluator(ChatClient.builder(chatModel));
		EvaluationRequest evaluationRequest = new EvaluationRequest(
				userText,
				response.getMetadata().get(QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS),
				response.getResult().getOutput().getContent());
		EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);
		assertTrue(evaluationResponse.isPass(), "Response is not relevant to the question");

	}

}
