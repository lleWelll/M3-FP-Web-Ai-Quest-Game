package org.questgame.webquestgame.Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.questgame.webquestgame.Additional.ChatGPTClient;
import org.questgame.webquestgame.Logic.ElementHandlers.ElementInitializer;
import org.questgame.webquestgame.Logic.Story;

import java.io.IOException;

/**
 * The {@code GeneratingServlet} class handles the generation of a game story based on user input.
 * <p>
 * This servlet processes a POST request containing a user prompt, interacts with the {@link ChatGPTClient}
 * to generate a main quest storyline, initializes the story elements, and forwards the request to
 * the initialization endpoint.
 * </p>
 *
 * @see HttpServlet
 * @see ChatGPTClient
 * @see ElementInitializer
 * @see Story
 */
public class GeneratingServlet extends HttpServlet {

	/**
	 * Logger instance for recording information about the generation and initialization process.
	 */
	private static final Logger log = LogManager.getLogger();

	/**
	 * Handles HTTP POST requests to generate a game story from a user prompt.
	 * <p>
	 * This method:
	 * <ul>
	 *     <li>Uses {@link ChatGPTClient} to generate a storyline based on the prompt.</li>
	 *     <li>Initializes the story elements using {@link ElementInitializer}.</li>
	 *     <li>Sets the generated {@link Story} object as a request attribute.</li>
	 *     <li>Forwards the request to the "/init" endpoint for further processing.</li>
	 * </ul>
	 * </p>
	 *
	 * @param req  the {@link HttpServletRequest} containing client request information
	 * @param resp the {@link HttpServletResponse} used to send the response to the client
	 * @throws ServletException if a servlet-specific error occurs during processing
	 * @throws IOException      if an I/O error occurs during request forwarding
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userPrompt = req.getParameter("userPrompt");
		String response = ChatGPTClient.generateMainQuestLine(userPrompt);
		Story story = ElementInitializer.createStoryFromAiResponse(response);
		req.setAttribute("story", story);

		log.info("Redirecting to /init");
		getServletContext().getRequestDispatcher("/init").forward(req, resp);
	}
}
