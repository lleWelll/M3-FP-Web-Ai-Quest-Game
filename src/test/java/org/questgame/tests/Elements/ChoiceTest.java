package org.questgame.tests.Elements;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.questgame.webquestgame.Logic.Elements.Choice;
import org.questgame.webquestgame.Logic.Elements.Situation;

@ExtendWith(MockitoExtension.class)
public class ChoiceTest {

	private Choice choice;

	@Mock
	private Situation leadFromSituation, leadToSituation;

	@BeforeEach
	public void init() {
		choice = new Choice("Test Description", leadFromSituation, leadToSituation, true);
	}

	@Test
	public void getLeadFrom_shouldReturnLeadFromSituation() {
		Assertions.assertEquals(leadFromSituation, choice.getLeadFrom());
	}

	@Test
	public void getLeadTo_shouldReturnLeadToSituation() {
		Assertions.assertEquals(leadToSituation, choice.getLeadTo());
	}

	@Test
	public void isGoNext_shouldReturnTrue() {
		Assertions.assertTrue(choice.isGoNext());
	}

	@Test
	public void goNext_whenGoNextIsTrue_shouldReturnLeadToSituation() {
		Assertions.assertEquals(leadToSituation, choice.goNext());
	}

	@Test
	public void goNext_whenGoNextIsFalse_shouldReturnLeadFromSituation() {
		choice.setGoNext(false);
		Assertions.assertEquals(leadFromSituation, choice.goNext());
	}
}
