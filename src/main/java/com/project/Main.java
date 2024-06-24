package com.project;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) throws Exception {
    Scanner scanner = new Scanner(System.in);
    Menu menu = new Menu(scanner);
    Game game = new Game(menu, new Player());
    game.loop();
    scanner.close();
  }
}
