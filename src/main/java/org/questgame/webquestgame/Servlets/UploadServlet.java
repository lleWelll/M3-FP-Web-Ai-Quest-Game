package org.questgame.webquestgame.Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.questgame.webquestgame.Additional.Settings;
import org.questgame.webquestgame.Logic.Story;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The {@code UploadServlet} class handles file uploads and processes uploaded story files.
 * <p>
 * This servlet processes POST requests to:
 * <ul>
 *     <li>Upload a story file to the server.</li>
 *     <li>Create the necessary upload directory if it does not exist.</li>
 *     <li>Parse the uploaded file to initialize a {@link Story} object.</li>
 *     <li>Forward the request to the initialization endpoint for further processing.</li>
 * </ul>
 * </p>
 *
 * @see HttpServlet
 * @see Story
 */
@MultipartConfig
public class UploadServlet extends HttpServlet {

	/**
	 * Logger instance for recording file upload and processing events.
	 */
	private static final Logger log = LogManager.getLogger();

	/**
	 * Handles HTTP POST requests to upload and process a story file.
	 * <p>
	 * This method:
	 * <ul>
	 *     <li>Determines the upload directory path.</li>
	 *     <li>Creates the directory if it does not already exist.</li>
	 *     <li>Uploads the file and stores it on the server.</li>
	 *     <li>Parses the uploaded file into a {@link Story} object.</li>
	 *     <li>Forwards the request to the "/init" endpoint for further processing.</li>
	 * </ul>
	 * </p>
	 *
	 * @param req  the {@link HttpServletRequest} containing client request information
	 * @param resp the {@link HttpServletResponse} used to send the response to the client
	 * @throws ServletException if a servlet-specific error occurs during file upload or processing
	 * @throws IOException      if an I/O error occurs during file upload or forwarding
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.info("Sending file on server");
		String uploadPath = getServletContext().getRealPath("") + File.separator + Settings.UPLOAD_DIRECTORY;

		createDirectory(uploadPath);
		String uploadedPath = uploadFile(req, uploadPath);
		req.setAttribute("story", Story.getStoryFromFile(uploadedPath));

		log.info("Redirecting to /init");
		getServletContext().getRequestDispatcher("/init").forward(req, resp);
	}

	/**
	 * Creates the upload directory if it does not already exist.
	 *
	 * @param uploadPath the path to the directory where files will be uploaded
	 */
	private void createDirectory(String uploadPath) {
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
			log.info("Directory {} created on {}", Settings.UPLOAD_DIRECTORY, uploadPath);
		}
		else {
			log.debug("Directory {} is already exists", Settings.UPLOAD_DIRECTORY);
		}
	}

	/**
	 * Handles the file upload process.
	 * <p>
	 * This method:
	 * <ul>
	 *     <li>Iterates through all parts of the HTTP request.</li>
	 *     <li>Checks for a valid submitted file name.</li>
	 *     <li>Saves the uploaded file to the specified directory.</li>
	 *     <li>Returns the full path of the uploaded file.</li>
	 * </ul>
	 * </p>
	 *
	 * @param req        the {@link HttpServletRequest} containing the uploaded file
	 * @param uploadPath the directory path where the file will be saved
	 * @return the full path of the uploaded file
	 * @throws ServletException if an error occurs while processing the request parts
	 * @throws IOException      if an I/O error occurs during file upload
	 * @throws FileNotFoundException if no file is found in the request
	 */
	private String uploadFile(HttpServletRequest req, String uploadPath) throws ServletException, IOException {
		for (Part part : req.getParts()) {
			String fileName = part.getSubmittedFileName();
			if (fileName != null) {
				part.write(uploadPath + File.separator + fileName);
				log.info("File {} uploaded to server", fileName);
				return uploadPath + "/" + fileName;
			}
		}
		log.error("There is no file in request");
		throw new FileNotFoundException("There is no file in request");
	}

}
