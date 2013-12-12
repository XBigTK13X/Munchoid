package sps.util;

import junit.framework.TestCase;
import org.junit.Test;

public class TestMathHelper extends TestCase {
    private static final float SIGNIFICANCE = .0001f;

    @Test
    public void testPercentToValue() {
        assertEquals(100.0f, MathHelper.percentToValue(50, 150, 50));
        assertEquals(-50.0f, MathHelper.percentToValue(-50, 50, 0));
        assertEquals(-100.0f, MathHelper.percentToValue(-150, -50, 50));
        assertEquals(-50.0f, MathHelper.percentToValue(-150, -50, 100));
    }

    @Test
    public void testValueToPercent() {
        assertEquals(100.0f, MathHelper.valueToPercent(50, 150, 150));
        assertEquals(50.0f, MathHelper.valueToPercent(50, 150, 100));
        assertEquals(50.0f, MathHelper.valueToPercent(-50, 50, 0));
        assertEquals(50.0f, MathHelper.valueToPercent(-150, -50, -100));
        assertEquals(0.0f, MathHelper.valueToPercent(-50, 50, -50));
    }

    @Test
    public void testRotationLerp() {
        assertEquals(359.0f, MathHelper.lerpDegrees(0, 359, 0));
        assertTrue(SIGNIFICANCE > MathHelper.lerpDegrees(0, 359, 100));
        assertEquals(40.0f, MathHelper.lerpDegrees(30, 50, 50));
        assertTrue(SIGNIFICANCE > MathHelper.lerpDegrees(10, -10, 50));
        assertEquals(330.0f, MathHelper.lerpDegrees(0, 300, 50));
    }
}


