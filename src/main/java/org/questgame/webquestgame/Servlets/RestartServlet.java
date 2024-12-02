package org.questgame.webquestgame.Servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * The {@code RestartServlet} class handles restarting the current game session.
 * <p>
 * This servlet processes a GET request to:
 * <ul>
 *     <li>Invalidate the current HTTP session, effectively clearing all session attributes.</li>
 *     <li>Redirect the user to the welcome page to start a new game session.</li>
 * </ul>
 * </p>
 *
 * @see HttpServlet
 */
public class RestartServlet extends HttpServlet {

	/**
	 * Logger instance for recording information about the restart process.
	 */
	private static final Logger log = LogManager.getLogger();

	/**
	 * Handles HTTP GET requests to restart the current session.
	 * <p>
	 * This method:
	 * <ul>
	 *     <li>Invalidates the current HTTP session, clearing all stored attributes.</li>
	 *     <li>Redirects the user to the "/welcome" endpoint for a fresh game start.</li>
	 * </ul>
	 * </p>
	 * @param request  the {@link HttpServletRequest} containing client request information
	 * @param response the {@link HttpServletResponse} used to send the response to the client
	 * @throws IOException if an I/O error occurs during the redirection process
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.info("Restarting current session");
		request.getSession().invalidate();
		log.info("Session invalidated, redirecting to /welcome");
		response.sendRedirect("/welcome");
	}
}
