package org.questgame.webquestgame.Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.questgame.webquestgame.Logic.ElementHandlers.ElementManager;
import org.questgame.webquestgame.Logic.Elements.Fail;
import org.questgame.webquestgame.Logic.Elements.Victory;

import java.io.IOException;

/**
 * The {@code StoryServlet} class handles updating the current situation in the game story based on user choices.
 * <p>
 * This servlet processes GET requests to:
 * <ul>
 *     <li>Retrieve the {@link ElementManager} from the session.</li>
 *     <li>Navigate to the next or previous situation based on the user's choice.</li>
 *     <li>Forward the request to either the finish endpoint (if the current situation is a {@link Victory} or {@link Fail}),
 *         or back to the main index page.</li>
 * </ul>
 * </p>
 *
 * @see HttpServlet
 * @see ElementManager
 * @see Victory
 * @see Fail
 */
public class StoryServlet extends HttpServlet {

	/**
	 * Logger instance for recording the navigation process and handling errors.
	 */
	private static final Logger log = LogManager.getLogger();

	/**
	 * Handles HTTP GET requests to update the current situation in the story.
	 * <p>
	 * This method:
	 * <ul>
	 *     <li>Retrieves the {@link ElementManager} from the session.</li>
	 *     <li>Determines the user's choice index using the {@code getChoiceIndex()} method.</li>
	 *     <li>Updates the current situation to either the next or previous situation.</li>
	 *     <li>Redirects to the finish endpoint if the current situation is a {@link Victory} or {@link Fail}.</li>
	 *     <li>Otherwise, forwards the request to "index.jsp" for continued gameplay.</li>
	 * </ul>
	 * </p>
	 *
	 * @param req  the {@link HttpServletRequest} containing client request information
	 * @param resp the {@link HttpServletResponse} used to send the response to the client
	 * @throws ServletException if a servlet-specific error occurs during forwarding
	 * @throws IOException      if an I/O error occurs during the forwarding process
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		log.info("Updating current situation");
		ElementManager em = (ElementManager) req.getSession().getAttribute("elementManager");
		int choiceIndex = getChoiceIndex(req);
		if (choiceIndex == -1)  req.setAttribute("currentSituation", em.getPreviousSituation());
		else req.setAttribute("currentSituation", em.getNextSituation(choiceIndex));
		log.info("Current situation = {} (choice with index {})", em.getCurrentSituation().getDescription(), choiceIndex);

		if (em.getCurrentSituation() instanceof Victory || em.getCurrentSituation() instanceof Fail) {
			log.info("redirecting to /finish");
			getServletContext().getRequestDispatcher("/finish").forward(req, resp);
		}
		else {
			getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	}

	/**
	 * Retrieves the choice index from the HTTP request parameter.
	 * <p>
	 * This method:
	 * <ul>
	 *     <li>Validates if the parameter is numeric using {@code StringUtils.isNumeric()}.</li>
	 *     <li>Logs an error and returns {@code -1} if the parameter is not numeric.</li>
	 *     <li>Parses and returns the numeric value of the parameter.</li>
	 * </ul>
	 * </p>
	 *
	 * @param req the {@link HttpServletRequest} containing client request information
	 * @return the choice index as an {@code int}, or {@code -1} if invalid
	 */
	private int getChoiceIndex(HttpServletRequest req) {
		String param = req.getParameter("index");
		if (! StringUtils.isNumeric(param)) {
			log.error("Request Parameter 'index' is not numeric (index = {})", param);
		}
		return Integer.parseInt(req.getParameter("index"));
	}

}
