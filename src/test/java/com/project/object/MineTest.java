package com.project.object;

import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.Board;
import com.project.Room;

@ExtendWith(MockitoExtension.class)
class MineTest {

  Mine mine;

  Random random = new Random(1);

  @Mock
  Board board;

  @Test
  void toStringShouldDisplayMine() {
    // given
    Mockito.when(board.getRoomsWithoutGameObjectAndPlayer()).thenReturn(Arrays.asList(new Room()));
    mine = new Mine(board, random);

    // when
    String display = mine.toString();

    // then
    String expected = "**";
    Assertions.assertEquals(expected, display);
  }

  @Test
  @Disabled("TODO")
  void setPosition() {
    Assertions.fail();
  }
}
