package org.questgame.webquestgame.Servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.questgame.webquestgame.Logic.Story;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * The {@code DownloadServlet} class facilitates downloading a serialized game story as a file.
 * <p>
 * This servlet handles HTTP GET requests to allow users to download their current story state.
 * It retrieves the {@link Story} object from the session, serializes it, and streams it as a file
 * attachment to the client.
 * </p>
 * <p>
 * @see HttpServlet
 * @see Story
 */
public class DownloadServlet extends HttpServlet {

	/**
	 * Logger for recording information about the execution of operations
	 */
	private static final Logger log = LogManager.getLogger();

	/**
	 * Handles HTTP GET requests for downloading the serialized story.
	 * <p>
	 * This method:
	 * <ul>
	 *     <li>Retrieves the {@link Story} object from the HTTP session.</li>
	 *     <li>Generates a filename in the format "story-[current timestamp].ser".</li>
	 *     <li>Serializes the {@code Story} object and writes it to the response's output stream.</li>
	 * </ul>
	 * </p>
	 *
	 * @param req  the {@link HttpServletRequest} object containing client request information
	 * @param resp the {@link HttpServletResponse} object for sending the response to the client
	 * @throws IOException if an error occurs during serialization or writing to the output stream
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		log.info("Preparing to download story");
		LocalDateTime currentDateTime = LocalDateTime.now();
		String fileName = "story-" + currentDateTime + ".ser";
		Story story =  (Story) req.getSession().getAttribute("story");
		resp.setContentType("application/octet-stream");
		resp.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		log.info("File {} is prepared to download", fileName);
		story.serialize(resp.getOutputStream());
	}
}
