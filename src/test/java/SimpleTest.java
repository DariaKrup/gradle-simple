import org.junit.Test;
import static org.junit.Assert.*;


public class SimpleTest {
    @Test
    public void testConcatenate() {
        mathService = new MathService();
        int result = mathService.multiply(2, 4);

        assertThat(result, equalTo(8));
    }
}