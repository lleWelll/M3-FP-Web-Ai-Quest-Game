package org.questgame.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.questgame.webquestgame.Exceptions.SerializationException;
import org.questgame.webquestgame.Logic.Story;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;


@ExtendWith(MockitoExtension.class)
public class StoryTest {

	private Story story;

	@BeforeEach
	public void init() {
		story = new Story();
	}

	@Test
	public void serialize(){
		AtomicInteger writeObjectMethodInvokedCounter = new AtomicInteger(0); //Method that counts, how many times writeObject() was invoked in test
		try (MockedConstruction<ObjectOutputStream> objectOutputStream = Mockito.mockConstruction(ObjectOutputStream.class,
				(mock, context) -> Mockito.doAnswer(
						invocation -> writeObjectMethodInvokedCounter.incrementAndGet()
				).when(mock).writeObject(story)
			)) {
			ByteArrayOutputStream outputStream = Mockito.mock(ByteArrayOutputStream.class);
			story.serialize(outputStream);
			Assertions.assertAll(
					() -> Assertions.assertEquals(1, objectOutputStream.constructed().size()),
					() -> Assertions.assertTrue(writeObjectMethodInvokedCounter.get() > 0)
			);
		}
	}

	@Test
	public void getStoryFromFile_withValidPathToSerializedObject_returnsDeserializedStoryObject() {
		try (MockedConstruction<ObjectInputStream> objectInputStream = Mockito.mockConstruction(ObjectInputStream.class,
				(mock, context) -> Mockito.when(mock.readObject()).thenReturn(story)
		); MockedConstruction<FileInputStream> fileInputStream = Mockito.mockConstruction(FileInputStream.class,
					 (mock, context) -> Mockito.when(mock.read()).thenReturn(1) //Imitates successful return of method
		)
		) {
			Story storyObject = Story.getStoryFromFile("some path");
			Assertions.assertAll(
					() -> Assertions.assertEquals(1, objectInputStream.constructed().size()),
					() -> Assertions.assertEquals(1, fileInputStream.constructed().size()),
					() -> Assertions.assertEquals(story, storyObject)
			);
		}
	}

	@ParameterizedTest
	@NullSource
	public void getStoryFromFile_withNullOrEmptyArgs_throwsNullPointerExceptionAndSerializationException(String path) {
		if (path == null) {
			Assertions.assertThrows(
					NullPointerException.class,
					() -> Story.getStoryFromFile(path)
			);
		}
		else if (path.isEmpty()) {
			Assertions.assertThrows(
					SerializationException.class,
					() -> Story.getStoryFromFile(path)
			);
		}
	}

}
