package com.example.repo5;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FinalProcessorTest {
    @Test
    public void testExecute() {
        FinalProcessor processor = new FinalProcessor();
        assertTrue(processor.execute().contains("Finalized in Repo5."));
    }
}
