package com.project.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.Board;
import com.project.Coordinate;
import com.project.Player;
import com.project.Room;
import com.project.item.Grenade;

@ExtendWith(MockitoExtension.class)
class GrenadeTest {

  Grenade grenade;

  @Mock
  Board board;

  @Mock
  Player player;

  @ParameterizedTest
  @MethodSource
  void useShouldBreakWallsOfCurrentAndAdjacentRoomInDirection(String direction, String playerRoomTop,
      String playerRoomBottom, String playerRoomLeft, String playerRoomRight, String nextRoomTop,
      String nextRoomBottom,
      String nextRoomLeft, String nextRoomRight) {
    // given
    Room playerRoom = new Room(new Coordinate(-1, -1));
    Room nextRoom = new Room(new Coordinate(-1, -1));

    when(board.getRoomByCoordinate(ArgumentMatchers.any())).thenReturn(Optional.of(playerRoom))
        .thenReturn(Optional.of(nextRoom));
    grenade = new Grenade(board);

    // when
    grenade.use(player, direction);

    // then
    assertEquals(playerRoomTop, playerRoom.getTop().toString());
    assertEquals(playerRoomBottom, playerRoom.getBottom().toString());
    assertEquals(playerRoomLeft, playerRoom.getLeft().toString());
    assertEquals(playerRoomRight, playerRoom.getRight().toString());

    assertEquals(nextRoomTop, nextRoom.getTop().toString());
    assertEquals(nextRoomBottom, nextRoom.getBottom().toString());
    assertEquals(nextRoomLeft, nextRoom.getLeft().toString());
    assertEquals(nextRoomRight, nextRoom.getRight().toString());
  }

  private static Stream<Arguments> useShouldBreakWallsOfCurrentAndAdjacentRoomInDirection() {
    String topAndBottomSymbol = "----";
    String topAndBottomDestroySymbol = "    ";
    String leftAndRightSymbol = "|";
    String leftAndRightDestroySymbol = " ";

    return Stream.of(
        Arguments.of("z", topAndBottomDestroySymbol, topAndBottomSymbol, leftAndRightSymbol,
            leftAndRightSymbol,
            topAndBottomSymbol, topAndBottomDestroySymbol, leftAndRightSymbol,
            leftAndRightSymbol),
        Arguments.of("q", topAndBottomSymbol, topAndBottomSymbol, leftAndRightDestroySymbol,
            leftAndRightSymbol,
            topAndBottomSymbol, topAndBottomSymbol, leftAndRightSymbol,
            leftAndRightDestroySymbol),
        Arguments.of("s", topAndBottomSymbol, topAndBottomDestroySymbol, leftAndRightSymbol,
            leftAndRightSymbol,
            topAndBottomDestroySymbol, topAndBottomSymbol, leftAndRightSymbol,
            leftAndRightSymbol),
        Arguments.of("d", topAndBottomSymbol, topAndBottomSymbol, leftAndRightSymbol,
            leftAndRightDestroySymbol,
            topAndBottomSymbol, topAndBottomSymbol, leftAndRightDestroySymbol,
            leftAndRightSymbol),
        Arguments.of("?", topAndBottomSymbol, topAndBottomSymbol, leftAndRightSymbol,
            leftAndRightSymbol,
            topAndBottomSymbol, topAndBottomSymbol, leftAndRightSymbol,
            leftAndRightSymbol));
  }
}
