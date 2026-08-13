import org.junit.Test;
import static org.junit.Assert.*;


public class SimpleTest {

    @Test
    public void testConcatenate() {
        String result = "one";
        result = result.concat("two");

        assertEquals("onetwo", result);
    }

    @Test
    public void testConcatenateLonger() {
        String result = "extremelyamazing";
        result = result.concat("longlongword");

        assertEquals("extremelyamazinglonglongword", result);
    }
}
