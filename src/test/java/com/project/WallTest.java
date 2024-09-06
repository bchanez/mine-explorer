package com.project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class WallTest {

  Wall wall;

  @Test
  void toStringShouldDisplayWall() {
    // given
    String intactSymbol = "|";
    String destroyedSymbol = " ";
    wall = new Wall(intactSymbol, destroyedSymbol);

    // when
    String display = wall.toString();

    // then
    Assertions.assertEquals(intactSymbol, display);
  }

  @Test
  void toStringShouldDisplayDestroyedWall() {
    // given
    String intactSymbol = "|";
    String destroyedSymbol = " ";
    wall = new Wall(intactSymbol, destroyedSymbol);

    // when
    wall.destroy();
    String display = wall.toString();

    // then
    Assertions.assertEquals(destroyedSymbol, display);
  }
}
