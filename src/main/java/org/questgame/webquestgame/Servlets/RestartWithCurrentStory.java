package org.questgame.webquestgame.Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.questgame.webquestgame.Logic.ElementHandlers.ElementManager;

import java.io.IOException;

/**
 * The {@code RestartWithCurrentStory} class handles restarting the game session while retaining the current story.
 * <p>
 * This servlet processes a GET request to:
 * <ul>
 *     <li>Retrieve the {@link ElementManager} from the current session.</li>
 *     <li>Reset the game state to the main situation of the current story.</li>
 *     <li>Update the session with the modified {@link ElementManager}.</li>
 *     <li>Forward the user to the "index.jsp" page to restart the game.</li>
 * </ul>
 * </p>
 *
 * @see HttpServlet
 * @see ElementManager
 */
public class RestartWithCurrentStory extends HttpServlet {

	/**
	 * Logger instance for recording the process of restarting the session with the current story.
	 */
	private static final Logger log = LogManager.getLogger();

	/**
	 * Handles HTTP GET requests to restart the game with the current story.
	 * <p>
	 * This method:
	 * <ul>
	 *     <li>Retrieves the {@link ElementManager} from the current session.</li>
	 *     <li>Resets the current situation to the main situation using {@code getMainSituation()}.</li>
	 *     <li>Updates the session with the reset {@link ElementManager}.</li>
	 *     <li>Forwards the user to "index.jsp" to restart the game.</li>
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
		log.info("Restarting with current Story");
		HttpSession session = req.getSession();
		ElementManager em = (ElementManager) session.getAttribute("elementManager");
		em.getMainSituation();
		session.setAttribute("elementManager", em);
		log.info("Current Situation = MainSituation, Redirecting to /index.jsp");
		getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
	}
}
