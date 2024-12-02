package org.questgame.tests.ElementHandlers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.questgame.webquestgame.Logic.ElementHandlers.ElementInitializer;
import org.questgame.webquestgame.Logic.Elements.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class ElementInitializerTest {

	private final String JSON_STORY = "{ 'Ситуация': { 'Описание': 'Вы - детектив, расследующий кражу Великой Книги Рецептов из известного ресторана. У вас есть несколько зацепок: свидетель, который видел подозрительного повара, и странная записка, оставленная на месте преступления.', 'Индекс ситуации': 'S1', 'Привело из': '-', 'Выборы ситуации': [ { 'Описание': 'Допросить свидетеля.', 'Индекс выбора': 'Y1', 'Флаг': 'goNext', 'Ведет к': 'S2' }, { 'Описание': 'Изучить записку.', 'Индекс выбора': 'Y2', 'Флаг': 'goNext', 'Ведет к': 'S3' } ] }, 'Ситуация': { 'Описание': 'Свидетель рассказывает, что видел, как повар уезжал на машине с логотипом ресторана. Он также упоминает, что повар часто посещал странные места.', 'Индекс ситуации': 'S2', 'Привело из': 'Y1', 'Выборы ситуации': [ { 'Описание': 'Отправиться в одно из странных мест.', 'Индекс выбора': 'Y3', 'Флаг': 'goNext', 'Ведет к': 'S4' }, { 'Описание': 'Отследить машину по камерам наблюдения.', 'Индекс выбора': 'Y4', 'Флаг': 'fail', 'Ведет к': 'F1' } ] }, 'Ситуация': { 'Описание': 'Записка содержит загадочные символы и упоминание о тайной встрече в старом складе.', 'Индекс ситуации': 'S3', 'Привело из': 'Y2', 'Выборы ситуации': [ { 'Описание': 'Отправиться на старый склад.', 'Индекс выбора': 'Y5', 'Флаг': 'goNext', 'Ведет к': 'S4' }, { 'Описание': 'Попробовать расшифровать символы.', 'Индекс выбора': 'Y6', 'Флаг': 'fail', 'Ведет к': 'F2' } ] }, 'Ситуация': { 'Описание': 'Вы прибываете на старый склад и находите там повара, который пытается продать книгу на чёрном рынке. У вас есть шанс его задержать.', 'Индекс ситуации': 'S4', 'Привело из': 'Y3 / Y5', 'Выборы ситуации': [ { 'Описание': 'Арестовать повара.', 'Индекс выбора': 'Y7', 'Флаг': 'victory', 'Ведет к': 'V1' }, { 'Описание': 'Попробовать договориться с поваром.', 'Индекс выбора': 'Y8', 'Флаг': 'fail', 'Ведет к': 'F3' } ] }, 'Победа': { 'Описание': 'Вы успешно арестовали повара и вернули Великую Книгу Рецептов в ресторан. Ваша репутация как детектива возросла.', 'Индекс победы': 'V1', 'Привело из': 'Y7' }, 'Поражение': { 'Описание': 'Вы не смогли отследить машину, и следы повара затерялись. Книга утеряна навсегда.', 'Индекс поражения': 'F1', 'Привело из': 'Y4' }, 'Поражение': { 'Описание': 'Попытка расшифровать символы оказалась безуспешной, и вы упустили шанс поймать повара.', 'Индекс поражения': 'F2', 'Привело из': 'Y6' }, 'Поражение': { 'Описание': 'Повар не поддался на уговоры и сбежал, оставив вас ни с чем.', 'Индекс поражения': 'F3', 'Привело из': 'Y8' } }";
	private ElementInitializer el;
	private Map<String, Element> expectedElements;

	@BeforeEach
	public void init() {
		el = new ElementInitializer();
		expectedElements = new HashMap<>();
	}

	@Test
	public void getStory() {
		el.setStory(JSON_STORY);
		Assertions.assertEquals(JSON_STORY, el.getStory());
	}

	@Test
	public void extractStory_returnStoryInCorrectForm() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Method extractStory = ElementInitializer.class.getDeclaredMethod("extractStory", String.class);
		extractStory.setAccessible(true);
		Assertions.assertEquals(JSON_STORY, extractStory.invoke(el, "\"content\": \"" + JSON_STORY + "\","));
	}

	@Test
	public void initializeSituation_whenElementMapIsEmpty_addsMainSituationObjectToElementMap() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
		getPrivateMethod("initializeSituation", String.class).invoke(el, "{ 'Ситуация': { 'Описание': 'Вы - детектив, расследующий кражу Великой Книги Рецептов из известного ресторана. У вас есть несколько зацепок: свидетель, который видел подозрительного повара, и странная записка, оставленная на месте преступления.', 'Индекс ситуации': 'S1', 'Привело из': '-', 'Выборы ситуации': [ { 'Описание': 'Допросить свидетеля.', 'Индекс выбора': 'Y1', 'Флаг': 'goNext', 'Ведет к': 'S2' }, { 'Описание': 'Изучить записку.', 'Индекс выбора': 'Y2', 'Флаг': 'goNext', 'Ведет к': 'S3' } ] },");
		Assertions.assertAll(
				() -> Assertions.assertFalse(el.getElements().isEmpty()),
				() -> Assertions.assertInstanceOf(MainSituation.class, el.getElements().get("S1")),
				() -> Assertions.assertEquals("Вы - детектив, расследующий кражу Великой Книги Рецептов из известного ресторана. У вас есть несколько зацепок: свидетель, который видел подозрительного повара, и странная записка, оставленная на месте преступления.", el.getElements().get("S1").getDescription())
		);
	}

	@Test
	public void initializeSituation_whenElementMapHaveMainSituation_addsSituationObjectToElementMap() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Method initSituations = getPrivateMethod("initializeSituation", String.class);
		initSituations.invoke(el, "{ 'Ситуация': { 'Описание': 'Вы - детектив, расследующий кражу Великой Книги Рецептов из известного ресторана. У вас есть несколько зацепок: свидетель, который видел подозрительного повара, и странная записка, оставленная на месте преступления.', 'Индекс ситуации': 'S1', 'Привело из': '-', 'Выборы ситуации': [ { 'Описание': 'Допросить свидетеля.', 'Индекс выбора': 'Y1', 'Флаг': 'goNext', 'Ведет к': 'S2' }, { 'Описание': 'Изучить записку.', 'Индекс выбора': 'Y2', 'Флаг': 'goNext', 'Ведет к': 'S3' } ] },");
		initSituations.invoke(el, " 'Ситуация': { 'Описание': 'Свидетель рассказывает, что видел, как повар уезжал на машине с логотипом ресторана. Он также упоминает, что повар часто посещал странные места.', 'Индекс ситуации': 'S2', 'Привело из': 'Y1', 'Выборы ситуации': [ { 'Описание': 'Отправиться в одно из странных мест.', 'Индекс выбора': 'Y3', 'Флаг': 'goNext', 'Ведет к': 'S4' }, { 'Описание': 'Отследить машину по камерам наблюдения.', 'Индекс выбора': 'Y4', 'Флаг': 'fail', 'Ведет к': 'F1' } ] },");
		Assertions.assertAll(
				() -> Assertions.assertEquals(2, el.getElements().size()),
				() -> Assertions.assertInstanceOf(Situation.class, el.getElements().get("S2")),
				() -> Assertions.assertEquals("Свидетель рассказывает, что видел, как повар уезжал на машине с логотипом ресторана. Он также упоминает, что повар часто посещал странные места.", el.getElements().get("S2").getDescription())
		);
	}

	@Test
	public void initializeVictory_addsVictoryObjectToElementMap() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		getPrivateMethod("initializeVictory", String.class).invoke(el, " 'Победа': { 'Описание': 'Вы успешно арестовали повара и вернули Великую Книгу Рецептов в ресторан. Ваша репутация как детектива возросла.', 'Индекс победы': 'V1', 'Привело из': 'Y7' },");
		Assertions.assertAll(
				() -> Assertions.assertFalse(el.getElements().isEmpty()),
				() -> Assertions.assertInstanceOf(Victory.class, el.getElements().get("V1")),
				() -> Assertions.assertEquals("Вы успешно арестовали повара и вернули Великую Книгу Рецептов в ресторан. Ваша репутация как детектива возросла.", el.getElements().get("V1").getDescription())
		);
	}

	@Test
	public void initializeFail_addsFailObjectToElementMap() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		getPrivateMethod("initializeFail", String.class).invoke(el, "'Поражение': { 'Описание': 'Вы не смогли отследить машину, и следы повара затерялись. Книга утеряна навсегда.', 'Индекс поражения': 'F1', 'Привело из': 'Y4' }, ");
		Assertions.assertAll(
				() -> Assertions.assertFalse(el.getElements().isEmpty()),
				() -> Assertions.assertInstanceOf(Fail.class, el.getElements().get("F1")),
				() -> Assertions.assertEquals("Вы не смогли отследить машину, и следы повара затерялись. Книга утеряна навсегда.", el.getElements().get("F1").getDescription())
		);
	}

	@Test
	public void initializeChoices_addsChoicesObjectsToElementMap() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		getPrivateMethod("initializeChoices", String.class).invoke(el, "'Выборы ситуации': [ { 'Описание': 'Допросить свидетеля.', 'Индекс выбора': 'Y1', 'Флаг': 'goNext', 'Ведет к': 'S2' }, { 'Описание': 'Изучить записку.', 'Индекс выбора': 'Y2', 'Флаг': 'goNext', 'Ведет к': 'S3' } ] },");
		Choice choice1, choice2;
		if (el.getElements().size() == 2 && el.getElements().get("Y1") instanceof Choice && el.getElements().get("Y2") instanceof Choice){
			choice1 = (Choice) el.getElements().get("Y1");
			choice2 = (Choice) el.getElements().get("Y2");
		}
		else throw new RuntimeException("Error in initializing choices");
		Assertions.assertAll(
				() -> Assertions.assertEquals("Допросить свидетеля.", choice1.getDescription()),
				() -> Assertions.assertEquals("Изучить записку.", choice2.getDescription()),
				() -> Assertions.assertTrue(choice1.isGoNext()),
				() -> Assertions.assertTrue(choice2.isGoNext())
		);
	}

	@ParameterizedTest
	@CsvSource({"goNext", "victory", "fail", "goBack"})
	public void flagHandler(String flag) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		if (flag.equals("goBack")) Assertions.assertFalse((Boolean) getPrivateMethod("flagHandler", String.class).invoke(el, flag));
		else Assertions.assertTrue((Boolean) getPrivateMethod("flagHandler", String.class).invoke(el, flag));
	}

	@Test
	public void initializeAllElements_addsAllElementsFromStory() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		el.setStory(JSON_STORY);
		getPrivateMethod("initializeAllElements", null).invoke(el);
		Assertions.assertEquals(16, el.getElements().size());
	}

	@Test
	public void setNextSituationForChoice() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		el.setStory(JSON_STORY);
		getPrivateMethod("initializeAllElements", null).invoke(el);
		getPrivateMethod("setNextSituationForChoice", null).invoke(el);
		Choice choice = (Choice) el.getElements().get("Y1");
		Situation situation = (Situation) el.getElements().get("S2");
		Assertions.assertAll(
				() -> Assertions.assertEquals(situation, choice.getLeadTo()),
				() -> Assertions.assertEquals(choice, situation.getLeadFrom()[0])
		);
	}

	@Test
	public void setChoicesToSituation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		el.setStory(JSON_STORY);
		getPrivateMethod("initializeAllElements", null).invoke(el);
		getPrivateMethod("setChoicesToSituation", null).invoke(el);
		MainSituation mainSituation = (MainSituation) el.getElements().get("S1");
		Choice choice = (Choice) el.getElements().get("Y1");
		Assertions.assertAll(
				() -> Assertions.assertEquals(choice, mainSituation.getChoices()[0]),
				() -> Assertions.assertEquals(mainSituation, choice.getLeadFrom())
		);
	}

	private Method getPrivateMethod(String name, Class<?>... arg) throws NoSuchMethodException {
		Method method = ElementInitializer.class.getDeclaredMethod(name, arg);
		method.setAccessible(true);
		return method;
	}
}
