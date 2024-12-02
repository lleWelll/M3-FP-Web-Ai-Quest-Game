package org.questgame.webquestgame.Additional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.questgame.webquestgame.Exceptions.ChatGptGenerationException;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * The {@code ChatGPTClient} class provides an interface for interacting with OpenAI's ChatGPT API.
 * It is used to generate main quest lines for a game based on a user-provided prompt.
 * <p>
 * This class handles:
 * <ul>
 *     <li>Validating user input</li>
 *     <li>Constructing JSON requests for the ChatGPT API</li>
 *     <li>Sending requests and handling responses</li>
 *     <li>Error handling for API failures</li>
 * </ul>
 *
 * @see Settings
 * @see ChatGptGenerationException
 */
public class ChatGPTClient {
	/**
	 * Logger instance for logging information and errors during API interactions.
	 */
	private static final Logger log = LogManager.getLogger();

	/**
	 * Generates a main quest storyline based on the user-provided prompt by interacting with the ChatGPT API.
	 * @param userPrompt the input prompt from the user to generate the main storyline
	 * @return the generated storyline as a {@link String}
	 * @throws ChatGptGenerationException if the prompt is invalid or an error occurs during API interaction
	 */
	public static String generateMainQuestLine(String userPrompt)  {
		if (StringUtils.isEmpty(userPrompt) || StringUtils.isBlank(userPrompt)) {
			log.warn("User prompt is not valid, request sending denied");
			throw new ChatGptGenerationException("User prompt is not valid, request sending denied");
		}
		log.info("Generating main story line");

		String jsonMessage = createJsonWithSystemContextAndExample(Settings.PROMPT_CONTEXT_FOR_GENERATING_MAIN_STORY, Settings.PERFECT_STORY_EXAMPLE, userPrompt);
		HttpResponse<String> response = sendRequest(jsonMessage);

		log.debug("ChatGpt Answer: {}", response.body());
		log.info("Generated story line");
		return response.body();
	}

	/**
	 * Sends an HTTP POST request to the ChatGPT API with the specified JSON message.
	 * @param jsonMessage the JSON message to send to the API
	 * @return the {@link HttpResponse} containing the API response
	 * @throws ChatGptGenerationException if an error occurs during the API request or response processing
	 */
	private static HttpResponse<String> sendRequest(String jsonMessage) {
		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(Settings.ChatGptURI)
					.header("Authorization", "Bearer " + Settings.API_KEY)
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(jsonMessage))
					.build();

			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() != 200) {
				log.error("Request failed with status: {}. Details: {}", response.statusCode(), response.body());
				throw new ChatGptGenerationException("Error Details: " + response.body());
			}
			return response;

		} catch (IOException | InterruptedException e) {
			log.error("Error while sending request to ChatGPT", e);
			throw new ChatGptGenerationException("Request error", e);
		}
	}

	/**
	 * Constructs a JSON message to send to the ChatGPT API.
	 * The JSON includes a system context, an example of a perfect story, and the user's prompt.
	 *
	 * @param systemContext the system context to guide the AI's response
	 * @param example       an example of a perfect story to provide additional context
	 * @param userPrompt    the user's input prompt
	 * @return a {@link String} representing the JSON message
	 */
	private static String createJsonWithSystemContextAndExample(String systemContext, String example, String userPrompt) {
		return  "{\n" +
				"\"model\": \"" + Settings.model + "\",\n" +
				"\"messages\": [\n" +
				"  {\"role\": \"system\", \"content\": \"" + systemContext + " " + example + "\"},\n" +
				"  {\"role\": \"user\", \"content\": \"" + userPrompt + "\"}\n" +
				"],\n" +
				"\"temperature\": 0.2" +
				"}";
	}
}