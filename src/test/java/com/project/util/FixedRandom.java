package com.project.util;

import java.util.Random;

public class FixedRandom extends Random {
  private final int fixedValue;

  public FixedRandom(int fixedValue) {
    this.fixedValue = fixedValue;
  }

  @Override
  public int nextInt(int bound) {
    return fixedValue;
  }
}
