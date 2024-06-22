package com.project.object;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExitTest {

  Exit exit;

  @Test
  void toStringShouldDisplayExit() {
    // given
    exit = new Exit();

    // when
    String display = exit.toString();

    // then
    String expected = "()";
    Assertions.assertEquals(expected, display);
  }
}
