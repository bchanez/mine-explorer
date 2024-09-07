package com.project;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WallTest {

  Wall wall;

  @Test
  void toStringShouldDisplayWall() {
    // given
    String intactSymbol = "|";
    String destroyedSymbol = " ";
    wall = new Wall(false, intactSymbol, destroyedSymbol);

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
    wall = new Wall(false, intactSymbol, destroyedSymbol);

    // when
    wall.destroy();
    String display = wall.toString();

    // then
    Assertions.assertEquals(destroyedSymbol, display);
  }

  @Test
  void isDestroyedShouldReturnTrueIfWallIsDestroyed() {
    // given
    wall = new Wall(false, "", "");
    wall.destroy();

    // when
    boolean isDestroyed = wall.isDestroyed();

    // then
    assertTrue(isDestroyed);
  }

  @Test
  void isDestroyedShouldReturnFalseWhenWallIsIndestructible() {
    // given
    wall = new Wall(true, "", "");
    wall.destroy();

    // when
    boolean isDestroyed = wall.isDestroyed();

    // then
    assertFalse(isDestroyed);
  }
}
