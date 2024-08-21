package ru.pastebin.cli.benchmark;

import org.junit.jupiter.api.Test;
import ru.pastebin.cli.dto.Paste;

import static org.junit.jupiter.api.Assertions.*;

public class RandomPasteCreatorTest {
    private final RandomPasteCreator randomPasteCreator = new RandomPasteCreator();
    @Test
    void createPaste_checkTextLength() {
        int minLength = 5;
        int maxLength = 20;
        int pasteCount = 10_000;
        for (int i = 0; i < pasteCount; i++) {
            Paste paste = randomPasteCreator.createPaste(minLength, maxLength);
            int pasteLength = paste.getPaste().length();
            assertTrue(pasteLength <= maxLength);
            assertTrue(pasteLength >= minLength);
        }
    }
    @Test
    void createPaste_wrongLength_shouldThrowException() {
        int minLengthWrong = 0;
        int maxLengthWrong = -1;
        int maxLengthOk = 10;
        int minLengthOk = 7;
        assertThrows(IllegalArgumentException.class, () -> randomPasteCreator.createPaste(minLengthOk, maxLengthWrong));
        assertThrows(IllegalArgumentException.class, () -> randomPasteCreator.createPaste(minLengthWrong, maxLengthWrong));
        assertThrows(IllegalArgumentException.class, () -> randomPasteCreator.createPaste(minLengthWrong, maxLengthOk));
    }

    @Test
    void createPaste_minSizeMoreThanMax_shouldThrowException() {
        int minLength = 10;
        int maxLength = 5;
        assertThrows(IllegalArgumentException.class, () -> randomPasteCreator.createPaste(minLength, maxLength));
    }

    @Test
    void createPaste_minSizeEqualsMaxSize() {
        int minLength = 10;
        int maxLength = 10;
        for (int i = 0; i < 10; i++) {
            Paste paste = randomPasteCreator.createPaste(minLength, maxLength);
            assertEquals(paste.getPaste().length(), minLength);
        }
    }
}
