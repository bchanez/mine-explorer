package com.project.object;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MineTest {

  Mine mine;

  @Test
  void toStringShouldDisplayMine() {
    // given
    mine = new Mine();

    // when
    String display = mine.toString();

    // then
    String expected = "**";
    Assertions.assertEquals(expected, display);
  }
}
