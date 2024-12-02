package org.questgame.webquestgame.Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.questgame.webquestgame.Logic.Elements.Fail;
import org.questgame.webquestgame.Logic.Elements.Situation;
import org.questgame.webquestgame.Logic.Elements.Victory;

import java.io.IOException;

/**
 * The {@code FinishServlet} class is responsible for handling game completion logic.
 * <p>
 * This servlet determines whether the current situation results in a victory or failure
 * and redirects the user to the appropriate finish page.
 * </p>
 *
 * @see HttpServlet
 * @see Victory
 * @see Fail
 */
public class FinishServlet extends HttpServlet {

	/**
	 * Logger for recording information about the execution of operations
	 */
	private static final Logger log = LogManager.getLogger();

	/**
	 * Handles HTTP GET requests to determine the game's outcome (victory or fail).
	 * <p>
	 * This method:
	 * <ul>
	 *     <li>Retrieves the current situation from the request attributes.</li>
	 *     <li>Checks if the situation is a {@link Victory} or {@link Fail} and sets a corresponding attribute ("finishType").</li>
	 *     <li>Redirects to the finish page based on the outcome.</li>
	 * </ul>
	 * </p>
	 * @param req  the {@link HttpServletRequest} containing client request information
	 * @param resp the {@link HttpServletResponse} used to send the response to the client
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during forwarding
	 * @throws IllegalArgumentException if the current situation is neither a {@link Victory} nor a {@link Fail}
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.info("Checking for victory or fail");
		Situation currentSituation = (Situation) req.getAttribute("currentSituation");
		if (currentSituation instanceof Victory) req.setAttribute("finishType", "victory");
		else if (currentSituation instanceof Fail) req.setAttribute("finishType", "fail");
		else {
			log.error("Current situation is not victory or fail ({})", currentSituation.getClass().getSimpleName());
			throw new IllegalArgumentException("Current situation is not victory or fail");
		}
		log.info("Redirecting to /Pages/Finish.jsp");
		getServletContext().getRequestDispatcher("/Pages/FinishPage.jsp").forward(req, resp);
	}
}
