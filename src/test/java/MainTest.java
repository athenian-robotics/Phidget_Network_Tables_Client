import com.arc852.Main;
import org.junit.jupiter.api.Test;

public class MainTest {
    @Test
    public void encoderToRadTest() {
        assert Main.ENC_RESOLUTION * Main.ENC_TO_RADS == 2*Math.PI;
    }


}
