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
    wall = new Wall("|");

    // when
    String display = wall.toString();

    // then
    String expected = "|";
    Assertions.assertEquals(expected, display);
  }
}
