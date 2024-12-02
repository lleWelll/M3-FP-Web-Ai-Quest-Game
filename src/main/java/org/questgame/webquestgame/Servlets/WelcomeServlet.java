package org.questgame.webquestgame.Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * The {@code WelcomeServlet} class handles requests to display the welcome page of the game.
 * <p>
 * This servlet processes GET requests to forward users to the welcome page.
 * </p>
 *
 * @see HttpServlet
 */
public class WelcomeServlet extends HttpServlet {

	/**
	 * Logger instance for recording navigation to the welcome page.
	 */
	private static final Logger log = LogManager.getLogger();

	/**
	 * Handles HTTP GET requests to display the welcome page.
	 * <p>
	 * This method:
	 * <ul>
	 *     <li>Forwards the request to "/Pages/WelcomePage.jsp" to render the welcome page.</li>
	 * </ul>
	 * </p>
	 *
	 * @param req  the {@link HttpServletRequest} containing client request information
	 * @param resp the {@link HttpServletResponse} used to send the response to the client
	 * @throws ServletException if a servlet-specific error occurs during forwarding
	 * @throws IOException      if an I/O error occurs during the forwarding process
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.info("Redirecting to /Pages/Welcome.jsp");
		getServletContext().getRequestDispatcher("/Pages/WelcomePage.jsp").forward(req, resp);
	}
}
