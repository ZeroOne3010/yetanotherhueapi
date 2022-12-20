package io.github.zeroone3010.yahueapi;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ColorTest {

  @Nested
  class HexToColor {
    @ParameterizedTest
    @CsvSource({
        "ffffff,1.0,1.0,1.0",
        "FFFFFF,1,1,1",
        "FFFFFF,1,1,1",
        "ff0000,1,0,0",
        "00ff00 ,0,1,0",
        "0000ff,0,0,1",
        "000000,0,0,0",
        "336699,0.2,0.4,0.6",
        "BA0BAB,0.7294117647,0.0431372549,0.6705882353",
        "ba0bab,0.7294117647,0.0431372549,0.6705882353",
        "#ffffff,1.0,1.0,1.0",
        "#FFFFFF,1,1,1",
        "#FFFFFF,1,1,1",
        "#ff0000,1,0,0",
        "#00ff00 ,0,1,0",
        "#0000ff,0,0,1",
        "#000000,0,0,0",
        "#336699,0.2,0.4,0.6",
        "#BA0BAB,0.7294117647,0.0431372549,0.6705882353",
        "#ba0bab,0.7294117647,0.0431372549,0.6705882353"
    })
    void validInput(final String input, final float expectedRed, final float expectedGreen, final float expectedBlue) {
      final Color color = Color.of(input);
      assertEquals(expectedRed, color.getRed());
      assertEquals(expectedGreen, color.getGreen());
      assertEquals(expectedBlue, color.getBlue());
    }

    @ParameterizedTest
    @CsvSource({
        "a",
        "aa",
        "aaa",
        "aaaa",
        "aaaaa",
        "aaaaaz",
        "aaaaaaa"
    })
    @NullAndEmptySource
    void invalidInput(final String input) {
      assertThrows(IllegalArgumentException.class, () -> Color.of(input));
    }
  }

  @Nested
  class IntAndIntegerToColor {
    @ParameterizedTest
    @CsvSource({
        "0xffffff,1.0,1.0,1.0",
        "0xffffff,1.0,1.0,1.0",
        "0xFFFFFF,1,1,1",
        "0xFFFFFF,1,1,1",
        "0xff0000,1,0,0",
        "0x00ff00,0,1,0",
        "0x0000ff,0,0,1",
        "0x000000,0,0,0",
        "0x336699,0.2,0.4,0.6",
        "0xBA0BAB,0.7294117647,0.0431372549,0.6705882353",
        "0xba0bab,0.7294117647,0.0431372549,0.6705882353"})
    void validInput(final int input, final float expectedRed, final float expectedGreen, final float expectedBlue) {
      final Color color = Color.of(input);
      assertEquals(expectedRed, color.getRed());
      assertEquals(expectedGreen, color.getGreen());
      assertEquals(expectedBlue, color.getBlue());

      final Color integerColor = Color.of(Integer.valueOf(input));
      assertEquals(color, integerColor);
    }

    @Test
    void invalidInput() {
      final Integer input = null;
      assertThrows(IllegalArgumentException.class, () -> Color.of(input));
    }
  }

  @Nested
  class IntsToColor {
    @ParameterizedTest
    @CsvSource({
        "255,255,255,1.0,1.0,1.0",
        "255,0,0,1,0,0",
        "0,255,0,0,1,0",
        "0,0,255,0,0,1",
        "0,0,0,0,0,0",
        "51,102,153,0.2,0.4,0.6",
        "186,11,171,0.7294117647,0.0431372549,0.6705882353"})
    void validInput(final int red, final int green, final int blue, final float expectedRed, final float expectedGreen, final float expectedBlue) {
      final Color color = Color.of(red, green, blue);
      assertEquals(expectedRed, color.getRed());
      assertEquals(expectedGreen, color.getGreen());
      assertEquals(expectedBlue, color.getBlue());
    }

    @ParameterizedTest
    @CsvSource({
        "256,255,255",
        "255,256,255",
        "255,255,256",
        "-1,0,0",
        "0,-1,0",
        "0,0,-1",
        "321,123,123",
        "123,321,123",
        "123,123,321",
    })
    void invalidInput(final int red, final int green, final int blue) {
      assertThrows(IllegalArgumentException.class, () -> Color.of(red, green, blue));
    }
  }

  @Nested
  class FloatsToColor {
    @ParameterizedTest
    @CsvSource({
        "1.0,1.0,1.0",
        "1,1,1",
        "1,1,1",
        "1,0,0",
        "0,1,0",
        "0,0,1",
        "0,0,0",
        "0.2,0.4,0.6",
        "0.7294117647,0.0431372549,0.6705882353",
        "0.7294117647,0.0431372549,0.6705882353"})
    void validInput(final float red, final float green, final float blue) {
      final Color color = Color.of(red, green, blue);
      assertEquals(red, color.getRed());
      assertEquals(green, color.getGreen());
      assertEquals(blue, color.getBlue());
    }

    @ParameterizedTest
    @CsvSource({
        "1.001,1.0,1.0",
        "1.0,1.001,1.0",
        "1.0,1.0,1.001",
        "1,2,1",
        "1,1,-1",
        "10,0,0",
        "0,100,0",
        "0,0,-1000",
        "-0.01,0.0,0.0",
        "0.0,-0.01,0.0",
        "0.0,0.0,-0.01"})
    void invalidInput(final float red, final float green, final float blue) {
      assertThrows(IllegalArgumentException.class, () -> Color.of(red, green, blue));
    }
  }

  @Nested
  class ObjectToColor {
    class JavaAwtColorMockup {
      public int getRed() {
        return 123; // 0.4823529412f
      }

      public int getGreen() {
        return 45; // 0.1764705882f
      }

      public int getBlue() {
        return 67; // 0.262745098f
      }
    }

    class JavaFxColorMockup {
      public double getRed() {
        return 0.4823529412d;
      }

      public double getGreen() {
        return 0.1764705882d;
      }

      public double getBlue() {
        return 0.262745098d;
      }
    }

    class AndroidGraphicsColorMockup {
      public float red() {
        return 0.4823529412f;
      }

      public float green() {
        return 0.1764705882f;
      }

      public float blue() {
        return 0.262745098f;
      }
    }

    class IntegerBasedColorClass {
      public Integer red() {
        return 123; // 0.4823529412f
      }

      public Integer green() {
        return 45; // 0.1764705882f
      }

      public Integer blue() {
        return 67; // 0.262745098f
      }
    }

    class DoubleBasedColorClass {
      public Double red() {
        return 0.4823529412d;
      }

      public Double green() {
        return 0.1764705882d;
      }

      public Double blue() {
        return 0.262745098d;
      }
    }

    @Test
    void javaAwtColor() {
      final Color color = Color.of(new JavaAwtColorMockup());
      assertEquals(0.4823529412f, color.getRed());
      assertEquals(0.1764705882f, color.getGreen());
      assertEquals(0.262745098f, color.getBlue());
    }

    @Test
    void javaFxColor() {
      final Color color = Color.of(new JavaFxColorMockup());
      assertEquals(0.4823529412f, color.getRed());
      assertEquals(0.1764705882f, color.getGreen());
      assertEquals(0.262745098f, color.getBlue());
    }

    @Test
    void androidGraphicsColor() {
      final Color color = Color.of(new AndroidGraphicsColorMockup());
      assertEquals(0.4823529412f, color.getRed());
      assertEquals(0.1764705882f, color.getGreen());
      assertEquals(0.262745098f, color.getBlue());
    }

    @Test
    void integerBasedColorClass() {
      final Color color = Color.of(new IntegerBasedColorClass());
      assertEquals(0.4823529412f, color.getRed());
      assertEquals(0.1764705882f, color.getGreen());
      assertEquals(0.262745098f, color.getBlue());
    }

    @Test
    void doubleBasedColorClass() {
      final Color color = Color.of(new DoubleBasedColorClass());
      assertEquals(0.4823529412f, color.getRed());
      assertEquals(0.1764705882f, color.getGreen());
      assertEquals(0.262745098f, color.getBlue());
    }

    @Test
    void nonColorClass() {
      assertThrows(IllegalArgumentException.class, () -> Color.of(new Object()));
      assertThrows(IllegalArgumentException.class, () -> Color.of(new UnsupportedOperationException()));
      assertThrows(IllegalArgumentException.class, () -> Color.of(3d));
      assertThrows(IllegalArgumentException.class, () -> Color.of((Random) null));
    }
  }
}
