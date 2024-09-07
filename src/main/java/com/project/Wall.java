package com.project;

public class Wall extends Draw {

  private boolean indestructible;
  private String destroyedSymbol;
  private boolean destroyed;

  public Wall(boolean indestructible, String intactSymbol, String destroyedSymbol) {
    super(intactSymbol);
    this.indestructible = indestructible;
    this.destroyedSymbol = destroyedSymbol;
  }

  public void destroy() {
    if (indestructible)
      return;

    this.symbol = destroyedSymbol;
    destroyed = true;
  }

  public boolean isDestroyed() {
    return destroyed;
  }
}
