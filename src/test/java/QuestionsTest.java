import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QuestionsTest {

    @Test
    void testExpressionGeneratorWithinRange() {
        Questions q = new Questions();
        for (int i = 0; i < 100; i++) {
            int value = q.expressionGenerator();
            Assertions.assertTrue(value >= 0 && value < 5,
                    "Значение должно быть в диапазоне [0, 4], но было " + value);
        }
    }
}
