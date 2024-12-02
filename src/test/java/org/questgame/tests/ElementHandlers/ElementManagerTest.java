package org.questgame.tests.ElementHandlers;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.questgame.webquestgame.Logic.ElementHandlers.ElementManager;
import org.questgame.webquestgame.Logic.Elements.*;
import org.questgame.webquestgame.Logic.Story;

import java.util.EmptyStackException;
import java.util.Map;


@ExtendWith(MockitoExtension.class)
public class ElementManagerTest {

	private ElementManager em;

	private Choice[] choices;

	@Mock
	private Story story;

	@Mock
	private MainSituation mainSituation;

	@BeforeEach
	public void init() {
		Mockito.lenient().when(story.getSTORY_ELEMENTS()).thenReturn(Map.of("S1", mainSituation));
		choices = new Choice[2];
		choices[0] = Mockito.mock(Choice.class);
		choices[1] = Mockito.mock(Choice.class);
		Mockito.lenient().when(mainSituation.getChoices()).thenReturn(choices);
		em = new ElementManager(story);
	}

	@Test
	public void getMainSituation_returnMainSituation() {
		Assertions.assertAll(
				() -> Assertions.assertEquals(mainSituation, em.getMainSituation()),
				() -> Assertions.assertEquals(mainSituation, em.getCurrentSituation())
		);
	}

	@Test
	public void getCurrentSituation_whenElementStackIsNotEmpty_returnsLastSituationInStack() {
		em.getMainSituation(); //Adding mainSituation to the stack
		Assertions.assertEquals(mainSituation, em.getCurrentSituation());
	}

	@Test
	public void getCurrentSituation_whenElementStackIsEmpty_throwsEmptyStackException() {
		Assertions.assertThrows(EmptyStackException.class,
				() -> em.getCurrentSituation()
		);
	}

	@Test
	public void getCurrentChoices_whenStackIsNotEmpty_returnsCurrentChoices() {
		em.getMainSituation();
		Assertions.assertEquals(choices, em.getCurrentChoices());
	}

	@Test
	public void getCurrentChoices_whenStackIsEmpty_throwsEmptyStackException() {
		Assertions.assertThrows(EmptyStackException.class,
				() -> em.getCurrentChoices()
		);
	}

	@Test
	public void getNextSituation_withValidIndex_returnsNextSituation() {
		Situation expectedNextSituation = Mockito.mock(Situation.class);
		Mockito.when(choices[0].goNext()).thenReturn(expectedNextSituation);
		em.getMainSituation();
		Assertions.assertAll(
				() -> Assertions.assertEquals(expectedNextSituation, em.getNextSituation(0)),
				() -> Assertions.assertEquals(expectedNextSituation, em.getCurrentSituation())
		);

	}

	@Test
	public void getNextSituation_withInvalidIndex_throwsIllegalArgumentException() {
		em.getMainSituation();
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> em.getNextSituation(5)
		);
	}

	@Test
	public void getNextSituation_whenThereAreNoNextSituation_throwsNullPointerException() {
		em.getMainSituation();
		Assertions.assertThrows(NullPointerException.class,
				() -> em.getNextSituation(0)
		);
	}

	@Test
	public void getNextSituation_whenStackIsEmpty_throwsEmptyStackException() {
		Assertions.assertThrows(EmptyStackException.class,
				() -> em.getNextSituation(0)
		);
	}

	@Test
	public void getPreviousSituation_whenElementStackSizeIsMoreThanOne_returnsPreviousSituationInStack() {
		Situation expectedNextSituation = Mockito.mock(Situation.class);
		Mockito.when(choices[0].goNext()).thenReturn(expectedNextSituation);
		em.getMainSituation();
		em.getNextSituation(0);
		Assertions.assertAll(
				() -> Assertions.assertEquals(mainSituation, em.getPreviousSituation()),
				() -> Assertions.assertEquals(mainSituation, em.getCurrentSituation())
		);

	}

	@Test
	public void getPreviousSituation_whenElementStackSizeIsOne_returnsMainSituation() {
		em.getMainSituation();
		Assertions.assertAll(
				() -> Assertions.assertEquals(mainSituation, em.getPreviousSituation()),
				() -> Assertions.assertEquals(mainSituation, em.getCurrentSituation())
		);
	}

	@Test
	public void getPreviousSituation_whenElementStackIsEmpty_returnsMainSituation() {
		Assertions.assertAll(
				() -> Assertions.assertEquals(mainSituation, em.getPreviousSituation()),
				() -> Assertions.assertEquals(mainSituation, em.getCurrentSituation())
		);
	}
}
