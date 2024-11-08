package com.project;

import java.util.Scanner;

public class Menu {

  private Scanner scanner;

  public Menu(Scanner scanner) {
    this.scanner = scanner;
  }

  public int getNbRow() {
    System.out.print("Nombre de ligne : ");
    return getInt();
  }

  public int getNbColumn() {
    System.out.print("Nombre de colonne : ");
    return getInt();
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
        "Quitter[0]" +
        " Se déplacer[1]" +
        " Lancer grenade(" + player.getGrenades().size() + ")[2]" +
        "\n->   ";

    System.out.print(menu);
    int action = getInt();

    return action;
  }

  public String chooseDirection() {
    String menu = "----------\n" +
        "Z Q S D" +
        "\n->   ";

    System.out.print(menu);
    String action = scanner.next();

    return action;
  }

  private int getInt() {
    int action;
    String input = scanner.next();
    try {
      action = Integer.parseInt(input);
    } catch (NumberFormatException e) {
      System.out.println("Veuillez entrer un nombre valide.");
      action = -1;
    }
    return action;
  }
}
