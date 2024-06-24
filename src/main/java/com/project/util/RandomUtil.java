package com.project.util;

import java.util.Random;

public class RandomUtil {

  private static Random random = new Random();

  public static void setRandom(Random random) {
    RandomUtil.random = random;
  }

  public static int nextInt(int bound) {
    return random.nextInt(bound);
  }
}
