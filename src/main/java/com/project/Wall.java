package com.project;

public class Wall extends Draw {

  private String destroyedSymbol;

  public Wall(String intactSymbol, String destroyedSymbol) {
    super(intactSymbol);
    this.destroyedSymbol = destroyedSymbol;
  }

  public void destroy() {
    this.symbol = destroyedSymbol;
  }
}
