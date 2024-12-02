package org.questgame.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.questgame.webquestgame.Additional.ChatGPTClient;
import org.questgame.webquestgame.Exceptions.ChatGptGenerationException;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ChatGPTClientTest {
	private final String AI_RESPONSE_EXAMPLE = "{\n" +
			"  \"id\": \"chatcmpl-ATCxHVhBjBep3fLnYdQhBfOd5gF1S\",\n" +
			"  \"object\": \"chat.completion\",\n" +
			"  \"created\": 1731524675,\n" +
			"  \"model\": \"gpt-3.5-turbo-0125\",\n" +
			"  \"choices\": [\n" +
			"    {\n" +
			"      \"index\": 0,\n" +
			"      \"message\": {\n" +
			"        \"role\": \"assistant\",\n" +
			"        \"content\": \"вот тебе пример, на который ты сможешь опираться: { 'Ситуация': { 'Описание': 'Вы - детектив, расследующий кражу Великой Книги Рецептов из известного ресторана. У вас есть несколько зацепок: свидетель, который видел подозрительного повара, и странная записка, оставленная на месте преступления.', 'Индекс ситуации': 'S1', 'Привело из': '-', 'Выборы ситуации': [ { 'Описание': 'Допросить свидетеля.', 'Индекс выбора': 'Y1', 'Флаг': 'goNext', 'Ведет к': 'S2' }, { 'Описание': 'Изучить записку.', 'Индекс выбора': 'Y2', 'Флаг': 'goNext', 'Ведет к': 'S3' } ] }, 'Ситуация': { 'Описание': 'Свидетель рассказывает, что видел, как повар уезжал на машине с логотипом ресторана. Он также упоминает, что повар часто посещал странные места.', 'Индекс ситуации': 'S2', 'Привело из': 'Y1', 'Выборы ситуации': [ { 'Описание': 'Отправиться в одно из странных мест.', 'Индекс выбора': 'Y3', 'Флаг': 'goNext', 'Ведет к': 'S4' }, { 'Описание': 'Отследить машину по камерам наблюдения.', 'Индекс выбора': 'Y4', 'Флаг': 'fail', 'Ведет к': 'F1' } ] }, 'Ситуация': { 'Описание': 'Записка содержит загадочные символы и упоминание о тайной встрече в старом складе.', 'Индекс ситуации': 'S3', 'Привело из': 'Y2', 'Выборы ситуации': [ { 'Описание': 'Отправиться на старый склад.', 'Индекс выбора': 'Y5', 'Флаг': 'goNext', 'Ведет к': 'S4' }, { 'Описание': 'Попробовать расшифровать символы.', 'Индекс выбора': 'Y6', 'Флаг': 'fail', 'Ведет к': 'F2' } ] }, 'Ситуация': { 'Описание': 'Вы прибываете на старый склад и находите там повара, который пытается продать книгу на чёрном рынке. У вас есть шанс его задержать.', 'Индекс ситуации': 'S4', 'Привело из': 'Y3 / Y5', 'Выборы ситуации': [ { 'Описание': 'Арестовать повара.', 'Индекс выбора': 'Y7', 'Флаг': 'victory', 'Ведет к': 'V1' }, { 'Описание': 'Попробовать договориться с поваром.', 'Индекс выбора': 'Y8', 'Флаг': 'fail', 'Ведет к': 'F3' } ] }, 'Победа': { 'Описание': 'Вы успешно арестовали повара и вернули Великую Книгу Рецептов в ресторан. Ваша репутация как детектива возросла.', 'Индекс победы': 'V1', 'Привело из': 'Y7' }, 'Поражение': { 'Описание': 'Вы не смогли отследить машину, и следы повара затерялись. Книга утеряна навсегда.', 'Индекс поражения': 'F1', 'Привело из': 'Y4' }, 'Поражение': { 'Описание': 'Попытка расшифровать символы оказалась безуспешной, и вы упустили шанс поймать повара.', 'Индекс поражения': 'F2', 'Привело из': 'Y6' }, 'Поражение': { 'Описание': 'Повар не поддался на уговоры и сбежал, оставив вас ни с чем.', 'Индекс поражения': 'F3', 'Привело из': 'Y8' } }\",\n" +
			"        \"refusal\": null\n" +
			"      },\n" +
			"      \"logprobs\": null,\n" +
			"      \"finish_reason\": \"stop\"\n" +
			"    }\n" +
			"  ],\n" +
			"  \"usage\": {\n" +
			"    \"prompt_tokens\": 15,\n" +
			"    \"completion_tokens\": 25,\n" +
			"    \"total_tokens\": 40,\n" +
			"    \"prompt_tokens_details\": {\n" +
			"      \"cached_tokens\": 0,\n" +
			"      \"audio_tokens\": 0\n" +
			"    },\n" +
			"    \"completion_tokens_details\": {\n" +
			"      \"reasoning_tokens\": 0,\n" +
			"      \"audio_tokens\": 0,\n" +
			"      \"accepted_prediction_tokens\": 0,\n" +
			"      \"rejected_prediction_tokens\": 0\n" +
			"    }\n" +
			"  },\n" +
			"  \"system_fingerprint\": null\n" +
			"}";

	@Mock
	HttpClient client;

	@Mock
	HttpResponse<String> response;

	@BeforeEach
	public void init() throws IOException, InterruptedException {
		Mockito.lenient().when(client.send(Mockito.any(), any(HttpResponse.BodyHandler.class))).thenReturn(response);
		Mockito.lenient().when(response.body()).thenReturn(AI_RESPONSE_EXAMPLE);
		Mockito.lenient().when(response.statusCode()).thenReturn(200);
	}

	@Test
	public void generateMainQuestLine_withValidUserPrompt_returnStoryInCorrectFormat() {
		try (MockedStatic<HttpClient> mockedClient = Mockito.mockStatic(HttpClient.class)) {
			mockedClient.when(HttpClient::newHttpClient).thenReturn(client);
			Assertions.assertEquals(AI_RESPONSE_EXAMPLE, ChatGPTClient.generateMainQuestLine("smt"));
		}
	}

	@ParameterizedTest
	@EmptySource
	@NullSource
	public void generateMainQuestLine_withEmptyUserPrompt_throwsChatGptGenerationException(String userPrompt) {
		try (MockedStatic<HttpClient> mockedClient = Mockito.mockStatic(HttpClient.class)) {
			mockedClient.when(HttpClient::newHttpClient).thenReturn(client);
			Assertions.assertThrows(
					ChatGptGenerationException.class,
					() -> ChatGPTClient.generateMainQuestLine(userPrompt)
			);
		}
	}

	@Test
	public void generateMainQuestLine_whenResponseStatusNotSuccessful_throwsChatGptGenerationException() {
		Mockito.when(response.statusCode()).thenReturn(500);
		try (MockedStatic<HttpClient> mockedClient = Mockito.mockStatic(HttpClient.class)) {
			mockedClient.when(HttpClient::newHttpClient).thenReturn(client);
			Assertions.assertThrows(
					ChatGptGenerationException.class,
					() -> ChatGPTClient.generateMainQuestLine("smt")
			);
		}
	}
}
