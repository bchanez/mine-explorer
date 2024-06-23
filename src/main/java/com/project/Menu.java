package com.project;

import java.util.Scanner;

public class Menu {

  private Scanner scanner;

  public Menu(Scanner scanner) {
    this.scanner = scanner;
  }

  public int getNbRow() {
    System.out.print("Nombre de ligne : ");
    return scanner.nextInt();
  }

  public int getNbColumn() {
    System.out.print("Nombre de colonne : ");
    return scanner.nextInt();
  }

  public int doAction(Player player) {
    if (player.getState().equals(PlayerState.WON)) {
      System.out.println("Vous avez gagné!");
      return 0;
    } else if (player.getState().equals(PlayerState.LOST)) {
      System.out.println("Vous avez perdu..");
      return 0;
    }

    String menu = "----------\n" +
        "Actions disponibles :\n" +
        "Quitter[0] Se déplacer[1]\n" +
        "----------";

    System.out.println(menu);
    int action = scanner.nextInt();

    return action;
  }

  public String chooseDirectionToMovePlayer() {
    String menu = "----------\n" +
        "Actions disponibles :\n" +
        "Z Q S D\n" +
        "----------";

    System.out.println(menu);
    String action = scanner.next();

    return action;
  }
}
