package com.project.object;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.Board;

@ExtendWith(MockitoExtension.class)
class ExitTest {

  Exit exit;

  Random random = new Random(1);

  @Mock
  Board board;

  @Test
  void toStringShouldDisplayExit() {
    // given
    exit = new Exit(board, random);

    // when
    String display = exit.toString();

    // then
    String expected = "()";
    Assertions.assertEquals(expected, display);
  }
}
