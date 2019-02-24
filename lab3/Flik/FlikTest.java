import org.junit.Test;
import static org.junit.Assert.*;


public class FlikTest {

    @Test
    public void testIsSameNumber() {
        int a = 128;
        int b = 128;
        int c = 500;
        boolean actual = Flik.isSameNumber(a, b);
        boolean actual2 = Flik.isSameNumber(a, c);

        assertTrue(actual);
        assertFalse(actual2);

    }

}
