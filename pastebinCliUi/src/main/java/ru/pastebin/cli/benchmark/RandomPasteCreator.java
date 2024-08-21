package ru.pastebin.cli.benchmark;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import ru.pastebin.cli.dto.Paste;

import java.util.Random;

@Component
public class RandomPasteCreator {
    private final Random random;
    public RandomPasteCreator() {
        this.random = new Random(42);
    }
    public Paste createPaste(long minTextSize, long maxTextSize) {
        if (minTextSize <= 0 || maxTextSize <= 0) {
            throw new IllegalArgumentException("Text size must be more than 0");
        }
        if (minTextSize > maxTextSize) {
            throw new IllegalArgumentException("Min text size must be lower or equals than max");
        }

        int length = random.nextInt((int)minTextSize, (int)maxTextSize + 1);
        String generatedString = RandomStringUtils.random(length, true, true);

        return new Paste(generatedString, System.currentTimeMillis());
    }
}
