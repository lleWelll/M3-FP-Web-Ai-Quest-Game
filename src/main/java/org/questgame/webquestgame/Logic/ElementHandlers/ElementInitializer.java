package org.questgame.webquestgame.Logic.ElementHandlers;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.questgame.webquestgame.Exceptions.ChoiceInitializeException;
import org.questgame.webquestgame.Logic.Elements.*;
import org.questgame.webquestgame.Logic.Story;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * The {@code ElementInitializer} class is responsible for parsing and initializing game elements
 * (e.g., {@link MainSituation}, {@link Situation}, {@link Choice}, {@link Victory}, {@link Fail}) from
 * an AI-generated JSON response. The initialized elements are linked together to form a coherent game story.
 * <p>
 * The primary method is {@link #createStoryFromAiResponse(String)}, which initializes all elements and returns
 * a {@link Story} object representing the game.
 * </p>
 * @see Story
 * @see MainSituation
 * @see Situation
 * @see Choice
 * @see Victory
 * @see Fail
 */
@Setter
@Getter
public class ElementInitializer {
	/**
	 * Logger for recording information about the execution of operations
	 */
	private static Logger log = LogManager.getLogger();

	/**
	 * A map of initialized elements, where the key is the element index and the value is the element object.
	 * This map is replenishing with every method call.
	 * Elements are stored in a map where each key is an index, such as "S1", "Y2", "V1", "F2":
	 * <ul>
	 *     <li>"S1" - Main Situation</li>
	 *     <li>"S" - Other Situations</li>
	 *     <li>"Y" - Choices</li>
	 *     <li>"V" - Victories</li>
	 *     <li>"F" - Failures</li>
	 * </ul>
	 */
	private HashMap<String, Element> elements = new HashMap<>();

	/**
	 * String field for containing original JSON story (double quotes (") are replaced with single ones ('))
	 */
	private String story;

	/**
	 * Public static method, that creates an instance of {@code ElementInitializer} and initialize all elements from AI Response
	 * @param json AI Response, that contains Story Elements and data about AI Response
	 * @return new {@link Story} with initialized and linked elements
	 */
	public static Story createStoryFromAiResponse(String json) {
		ElementInitializer el = new ElementInitializer(json);
		el.initializeAllElements();
		log.info("Started linking Situations and Choices");
		el.setChoicesToSituation();
		el.setNextSituationForChoice();
		log.info("Situations and Choices Linked");
		return new Story(el.getElements());
	}

	/**
	 * Extracts the main story content from the JSON response.
	 * @param json the original JSON response
	 * @return the main story content as a string
	 */
	private String extractStory(String json) {
		return StringUtils.substringBetween(json, "\"content\": \"", "\",");
	}

	/**
	 * Initializes all elements
	 */
	private void initializeAllElements() {
		log.info("Started initializing elements from story");
		initializeElements("'Ситуация': {", "]", this::initializeSituation);
		initializeElements("'Победа': {", "},", this::initializeVictory);
		initializeElements("'Поражение': {", "}", this::initializeFail);
		initializeElements("'Выборы ситуации': [", "]", this::initializeChoices);
		log.info("Elements initialized");
	}

	/**
	 * Generic method for initializing elements based on specified tokens and an element creation method.
	 * <p>All initialized elements will pe putted to {@code elements} map</p>
	 * @param startToken     the starting token to identify the element block
	 * @param endToken       the ending token to identify the element block
	 * @param elementCreator a method reference to handle the specific element initialization
	 */
	private void initializeElements(String startToken, String endToken, Consumer<String> elementCreator) {
		String localStory = story;
		while (localStory.contains(startToken)) {
			String elementBlock = StringUtils.substringBetween(localStory, startToken, endToken);
			elementCreator.accept(elementBlock);
			localStory = StringUtils.substringAfter(localStory, startToken);
		}
	}

	/**
	 * Initializes a situation element (either {@link MainSituation} or {@link Situation}) from the provided block of text.
	 * @param situationBlock the block of text describing the situation
	 */
	private void initializeSituation(String situationBlock) {
		String description;
		String situationIndex;
		String leadFromIndex;

		log.debug("Current Situation Block: \n{}", situationBlock);
		description = StringUtils.substringBetween(situationBlock, "'Описание': '", "',");
		situationIndex = StringUtils.substringBetween(situationBlock, "'Индекс ситуации': '", "',");
		leadFromIndex = StringUtils.substringBetween(situationBlock, "'Привело из': '", "',");

		if (leadFromIndex.equals("-")) {
			elements.put(situationIndex, new MainSituation(description));
			log.debug("MainSituation added to element Map, with index: {}, description: {}", situationIndex, description);
		} else {
			elements.put(situationIndex, new Situation(description));
			log.debug("Situation added to element Map, with index: {}, description: {}", situationIndex, description);
		}
		log.info("Situation initialized");
	}

	/**
	 * Initializes a {@link Victory} element from the provided block of text.
	 * @param victoryBlock the block of text describing the victory
	 */
	private void initializeVictory(String victoryBlock) {
		String description;
		String victoryIndex;

		log.debug("Current Victory Block: \n{}", victoryBlock);
		description = StringUtils.substringBetween(victoryBlock, "'Описание': '", "',");
		victoryIndex = StringUtils.substringBetween(victoryBlock, "'Индекс победы': '", "',");

		elements.put(victoryIndex, new Victory(description));
		log.debug("Victory added to element Map, with index: {}, description: {}", victoryIndex, description);
		log.info("Victory initialized");
	}

	/**
	 * Initializes a {@link Fail} element from the provided block of text.
	 * @param failBlock the block of text describing the failure
	 */
	private void initializeFail(String failBlock) {
		String description;
		String failIndex;

		log.debug("Current Fail Block: \n{}", failBlock);
		description = StringUtils.substringBetween(failBlock, "'Описание': '", "',");
		failIndex = StringUtils.substringBetween(failBlock, "'Индекс поражения': '", "',");
		elements.put(failIndex, new Fail(description));
		log.debug("Fail added to element Map, with index: {}, description: {}", failIndex, description);
		log.info("Fail initialized");
	}

	/**
	 * Initializes {@link Choice} elements from the provided block of text.
	 * @param choiceBlock the block of text describing the choices
	 */
	private void initializeChoices(String choiceBlock) {
		String description;
		String choiceIndex;
		String flag;

		while (choiceBlock.contains("{")) {
			String choice = StringUtils.substringBetween(choiceBlock, "{", "}");
			log.debug("Current Choice Block: \n{}", choiceBlock);

			description = StringUtils.substringBetween(choice, "'Описание': '", "',");
			choiceIndex = StringUtils.substringBetween(choice, "'Индекс выбора': '", "',");
			flag = StringUtils.substringBetween(choice, "'Флаг': '", "',");

			elements.put(choiceIndex, new Choice(description, flagHandler(flag)));
			log.debug("Choice added to element Map, with index: {}, description: {}, goNext: {}", choiceIndex, description, flag);

			choiceBlock = StringUtils.substringAfter(choiceBlock, "{");
		}
		log.info("Choices initialized");
	}

	/**
	 * Parses a flag string to determine its effect on navigation.
	 * @param flag the flag string from the JSON
	 * @return true if the flag indicates navigation, false otherwise
	 */
	private boolean flagHandler(String flag) {
		switch (flag) {
			case "goNext", "victory", "fail" -> {
				return true;
			}
			default -> {
				return false;
			}
		}
	}

	/**
	 * Links Choices and Situations by adding link to {@link Situation} to {@code leadTo} property of {@link Choice} and
	 * adds {@link Choice} to {@code leadFrom} property of {@link Situation}
	 */
	private void setNextSituationForChoice() {
		log.info("Setting next situations to choices");
		Map<String, Element> choiceMap = elements.entrySet().stream()
				.filter((entry) -> entry.getValue().getClass().equals(Choice.class))
				.collect((Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
		for (String choiceIndex : choiceMap.keySet()) {
			Choice choice = (Choice) choiceMap.get(choiceIndex);
			log.debug("Current choice: {}, goNext status: {}", choiceIndex, choice.isGoNext());

			if (!choice.isGoNext()) {
				choice.setLeadTo(choice.getLeadFrom());
				continue;
			}

			String choiceBlock = StringUtils.substringBetween(story, "'Индекс выбора': '" + choiceIndex + "'", "}");
			String leadToIndex = StringUtils.substringBetween(choiceBlock, "'Ведет к': '", "'");

			if (leadToIndex.equals("-")) {
				log.error("Choice {} leadTo \"-\"", choiceIndex);
				throw new ChoiceInitializeException("Choice " + choiceIndex + "leadTo \"-\"");
			}
			choice.setLeadTo((Situation) elements.get(leadToIndex));
			Situation leadToSituation = (Situation) elements.get(leadToIndex);
			leadToSituation.addLeadFrom(choice);
			log.debug("Choice {} lead to Situation {} ", choiceIndex, leadToIndex);
		}
		log.info("Situations set to choices");
	}

	/**
	 * Links Situations and Choices by adding {@link Choice} to {@code choices[]} of {@link Situation} and adds {@link Situation}
	 * to {@code leadFrom} property of {@link Choice}
	 */
	private void setChoicesToSituation() {
		log.info("Setting Choices to Situations");
		Map<String, Element> situationMap = elements.entrySet().stream()
				.filter(entry -> entry.getValue().getClass().equals(MainSituation.class)
						|| entry.getValue().getClass().equals(Situation.class))
				.collect((Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

		for (String situationIndex : situationMap.keySet()) {
			MainSituation situation = (MainSituation) elements.get(situationIndex);
			log.debug("Current situation: {}", situationIndex);
			String situationBlock = StringUtils.substringBetween(story, "'Индекс ситуации': '" + situationIndex + "',", "]");
			while (situationBlock.contains("'Индекс выбора'")) {
				String choiceIndex = StringUtils.substringBetween(situationBlock, "Индекс выбора': '", "'");
				if (elements.containsKey(choiceIndex)) {
					Choice c = (Choice) elements.get(choiceIndex);
					situation.addChoice(c);
					c.setLeadFrom(situation);
					log.debug("Situation {} and choice {} linked", situationIndex, choiceIndex);
				} else {
					log.error("element map doesn't contain key {}", choiceIndex);
					throw new NoSuchElementException();
				}
				situationBlock = StringUtils.substringAfter(situationBlock, "'Индекс выбора'");
			}
		}
		log.info("Choices set to Situations");
	}

	/**
	 * Empty Constructor
	 */
	public ElementInitializer() {
		log.info("ElementInitializer created with no params");
	}

	/**
	 * Constructor, that initialize story from AI response
	 * @param json AI Response, that contains Story Elements and data about AI Response
	 */
	public ElementInitializer(String json) {
		this.story = extractStory(json);
		log.info("ElementInitializer created with String param");
	}
}