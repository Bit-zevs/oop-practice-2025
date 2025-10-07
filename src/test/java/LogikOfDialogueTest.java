import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Assertions;

public class LogikOfDialogueTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testHelpPrintsInstructions() throws Exception {
        LogikOfDialogue l = new LogikOfDialogue();

        var method = LogikOfDialogue.class.getDeclaredMethod("help");
        method.setAccessible(true);
        method.invoke(l);

        String output = outContent.toString();
        Assertions.assertTrue(output.contains("This bot guesses a random number."));
        Assertions.assertTrue(output.contains("Enter a number from 0 to 4"));
    }

    @Test
    void testQuestionForTheUserPrintsResult() throws Exception {
        LogikOfDialogue l = new LogikOfDialogue();

        var method = LogikOfDialogue.class.getDeclaredMethod("questionForTheUser", Integer.class);
        method.setAccessible(true);
        method.invoke(l, 2);

        String output = outContent.toString();
        Assertions.assertTrue(output.contains("Enter a number from 0 to 4"));
        Assertions.assertTrue(output.contains("Yes, you are right") || output.contains("No, the correct answer is"));
    }
}
