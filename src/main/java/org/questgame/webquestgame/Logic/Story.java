package org.questgame.webquestgame.Logic;

import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.questgame.webquestgame.Exceptions.SerializationException;
import org.questgame.webquestgame.Logic.Elements.Element;

import java.io.*;
import java.util.Map;

import static java.util.stream.Collectors.toMap;


/**
 A class for managing the game story.
 * The class is designed to store story elements, serialize and deserialize objects.
 * Supports operations with files and streams for saving and loading data.
 *
 * <p>The class implements the {@link Serializable} interface, which allows you to save the state of an object
 * and restore it from a file or another stream.</p>
 *
 * @see java.io.Serializable
 * @see org.questgame.webquestgame.Logic.Elements.Element
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Story implements Serializable {
	/**
	 * Logger for recording information about the execution of operations
	 */
	private static Logger log = LogManager.getLogger();

	/**
	 * Serialization version identifier of the class.
	 * Used to ensure compatibility between serialized versions of an object.
	 */
	@Serial
	private static final long serialVersionUID = 1;

	/**
	 * A map of story elements, where the key is the element index and the value is the element object.
	 * <p>Indexes are created in this format: S1, S4, Y6, V1, F2</p>
	 * <p>S1 - index for First (Main) Situation</p>
	 * <p>S - index for other Situations</p>
	 * <p>Y - index for Choices</p>
	 * <p>V - index for Victories</p>
	 * <p>F - index for Fails</p>
	 */
	private Map<String, Element> STORY_ELEMENTS;

	/**
	 * Serialization method for saving instance of object in specified output stream
	 * @param outputStream output stream for serializing object
	 * @throws SerializationException if Input / Output error occurs
	 */
	public void serialize(OutputStream outputStream) {
		log.info("Serializing Story on outputStream");
		try (ObjectOutputStream objectOut = new ObjectOutputStream(outputStream)) {
			objectOut.writeObject(this);
		} catch (IOException e) {
			log.error("Error in serializing, throwing Exception");
			throw new SerializationException("Error in serializing", e);
		}
	}

	/**
	 * Static Deserialization method with null check
	 * @param path specify file for deserialization
	 * @return Deserialized instance of {@code Story} class
	 * @throws NullPointerException if path = null
	 * @throws SerializationException if Input / Output error occurs
	 */
	public static Story getStoryFromFile(String path) {
		if (path == null) {
			log.error("File is null");
			throw new NullPointerException("File is null");
		}
		return deserialize(path);
	}

	/**
	 * Static private deserialization method for creating new instance of {@code Story} from specific file
	 * @param path specify file for deserialization
	 * @return Deserialized instance of {@code Story} class
	 * @throws SerializationException if Input / Output error occurs
	 */
	private static Story deserialize(String path) {
		log.info("Deserializing Story object from file: ");
		try (FileInputStream fileIn = new FileInputStream(path);
			 ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
			Story story = (Story)objectIn.readObject();
			log.info("Story object successfully deserialized");
			return story;
		} catch (IOException | ClassNotFoundException e) {
			log.error("Error in Deserializing Story", e);
			throw new SerializationException("Error in Deserializing Story", e);
		}
	}
}
