package com.project;

import java.util.Random;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    Menu menu = new Menu(scanner);
    Game game = new Game(menu, new Player(), new Random());
    game.loop();
    scanner.close();
  }
}
