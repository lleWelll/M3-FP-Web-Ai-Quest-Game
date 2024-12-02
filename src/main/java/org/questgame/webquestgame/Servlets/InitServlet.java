package org.questgame.webquestgame.Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.questgame.webquestgame.Logic.ElementHandlers.ElementManager;
import org.questgame.webquestgame.Logic.Story;

import java.io.IOException;

/**
 * The {@code InitServlet} class initializes a new game session.
 * <p>
 * This servlet processes a POST request to set up the game state by:
 * <ul>
 *     <li>Retrieving the {@link Story} object from the request attributes.</li>
 *     <li>Creating an {@link ElementManager} for managing game elements.</li>
 *     <li>Storing the story, the element manager, and the main situation in the HTTP session.</li>
 *     <li>Redirecting the user to the game start page ("index.jsp").</li>
 * </ul>
 * </p>
 *
 * @see HttpServlet
 * @see Story
 * @see ElementManager
 */
public class InitServlet extends HttpServlet {

	/**
	 * Logger instance for recording the initialization process and session setup.
	 */
	private static final Logger log = LogManager.getLogger();

	/**
	 * Handles HTTP POST requests to initialize a new game session.
	 * <p>
	 * This method:
	 * <ul>
	 *     <li>Starts a new session or retrieves an existing one.</li>
	 *     <li>Initializes the {@link Story} and {@link ElementManager} from the request attributes.</li>
	 *     <li>Stores the story, the element manager, and the main situation in the session.</li>
	 *     <li>Redirects the client to the "index.jsp" page to begin the game.</li>
	 * </ul>
	 * </p>
	 *
	 * @param req  the {@link HttpServletRequest} containing client request information
	 * @param resp the {@link HttpServletResponse} used to send the response to the client
	 * @throws ServletException if a servlet-specific error occurs during initialization
	 * @throws IOException      if an I/O error occurs during the redirection process
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.info("Init servlet Started");
		HttpSession session = req.getSession(true);
		Story story = (Story) req.getAttribute("story");
		ElementManager em = new ElementManager(story);
		log.info("New session started");
		session.setAttribute("story", story);
		session.setAttribute("currentSituation", em.getMainSituation());
		session.setAttribute("elementManager", em);
		log.info("Attribute story, element-manager and first situation added to session");

		log.info("Redirecting to index.jsp");
		resp.sendRedirect("/index.jsp");
	}
}
