package com.project;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CoordinateTest {

  Coordinate coordinate;

  @Test
  void initCoordinate() {
    // given
    // when
    coordinate = new Coordinate(0, 0);

    // then
    Assertions.assertEquals(0, coordinate.getX());
    Assertions.assertEquals(0, coordinate.getY());
  }

  @Test
  void setX() {
    // given
    coordinate = new Coordinate(0, 0);

    // when
    coordinate.setX(1);

    // then
    Assertions.assertEquals(coordinate, new Coordinate(1, 0));
  }

  @Test
  void setY() {
    // given
    coordinate = new Coordinate(0, 0);

    // when
    coordinate.setY(1);

    // then
    Assertions.assertEquals(coordinate, new Coordinate(0, 1));
  }

  @ParameterizedTest
  @MethodSource
  void testEquals(Coordinate coordinate1, Coordinate coordinate2, boolean expected) {
    // given
    // when
    boolean result = coordinate1.equals(coordinate2);

    // then
    Assertions.assertEquals(expected, result);
  }

  private static Stream<Arguments> testEquals() {
    Coordinate coordinate = new Coordinate(0, 0);

    return Stream.of(
        Arguments.of(coordinate, coordinate, true),
        Arguments.of(coordinate, new Coordinate(0, 0), true),
        Arguments.of(coordinate, null, false),
        Arguments.of(coordinate, new Coordinate(1, 0), false),
        Arguments.of(coordinate, new Coordinate(0, 1), false),
        Arguments.of(coordinate, new Coordinate(1, 1), false));
  }

  @ParameterizedTest
  @MethodSource
  void testHashCode(Coordinate coordinate1, Coordinate coordinate2, boolean expected) {
    // given

    // when
    boolean result = coordinate1.hashCode() == coordinate2.hashCode();

    // then
    Assertions.assertEquals(expected, result);
  }

  private static Stream<Arguments> testHashCode() {
    return Stream.of(
        Arguments.of(new Coordinate(0, 0), new Coordinate(0, 0), true),
        Arguments.of(new Coordinate(1, 2), new Coordinate(2, 3), false));
  }
}