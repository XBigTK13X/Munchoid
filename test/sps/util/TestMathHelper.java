package sps.util;

import junit.framework.TestCase;
import org.junit.Test;

public class TestMathHelper extends TestCase {
    @Test
    public void testPercentToValue() {
        assertEquals(MathHelper.percentToValue(50, 150, 50), 100.0f);
        assertEquals(MathHelper.percentToValue(-50, 50, 0), -50.0f);
        assertEquals(MathHelper.percentToValue(-150, -50, 50), -100.0f);
        assertEquals(MathHelper.percentToValue(-150, -50, 100), -50.0f);
    }

    @Test
    public void testValueToPercent() {
        assertEquals(MathHelper.valueToPercent(50, 150, 150), 100.0f);
        assertEquals(MathHelper.valueToPercent(50, 150, 100), 50.0f);
        assertEquals(MathHelper.valueToPercent(-50, 50, 0), 50.0f);
        assertEquals(MathHelper.valueToPercent(-150, -50, -100), 50.0f);
        assertEquals(MathHelper.valueToPercent(-50, 50, -50), 0.0f);
    }

    @Test
    public void testRotationLerp() {
        assertEquals(MathHelper.lerpDegrees(0, 360, 0), 360.0f);
        assertEquals(MathHelper.lerpDegrees(0, 360, 100), 0.0f);
    }
}


