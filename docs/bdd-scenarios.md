# BDD Scenarios - Mine Explorer MVP

> Document from BDD Workshop (Tres Amigos) - April 2026

## MVP Configuration

| Parameter | Value |
|-----------|-------|
| Grid | 5x5 |
| Coordinates | (x,y) with (0,0) at top-left, Y increases downward |
| Walls | Shared between adjacent cells |
| Borders | Indestructible |
| Grenades | Fixed stock at start (e.g., 3) |
| Visibility | Fog of war (only visited cells visible) |

## Business Rules

| Rule | Invariant |
|------|-----------|
| **R1: Shared walls** | A wall between two cells is unique; destroying it opens passage from both sides |
| **R2: Blast propulsion** | Only the explosion of a destructible wall generates a blast that propels the player |
| **R3: Grenade without effect** | Throwing a grenade without a wall to destroy consumes the grenade without movement |
| **R4: Indestructible walls** | Border walls cannot be destroyed (grenade wasted) |
| **R5: Chain reaction** | The blast propels the player to the adjacent cell, triggering mine or exit |
| **R6: Fog of war** | Only visited cells are visible; mines and exit are hidden |
| **R7: Terminal states** | A LOST or WON game no longer accepts actions |
| **R8: No deadlock detection** | The player can be in an unwinnable situation without notification |

---

## Feature 1: Game Creation

### Scenario 1.1: Standard game creation
```gherkin
Scenario: Creating a new game with initial configuration
  Given a game configuration with:
    | width | 5 |
    | height | 5 |
    | grenades | 3 |
    | player position | (0, 0) |
    | exit position | (4, 4) |
    | mine positions | (2, 2), (3, 1) |
  When I create the game
  Then the game is in progress (PLAYING)
  And the player is at position (0, 0)
  And the player has 3 grenades
  And only cell (0, 0) is visible
```

---

## Feature 2: Player Movement (ZQSD)

### Scenario 2.1: Move south without obstacle (Happy Path)
```gherkin
Scenario: The player moves south into an accessible cell
  Given a game in progress
  And the player is at position (0, 0)
  And there is no wall between (0, 0) and (0, 1)
  When the player moves south (S)
  Then the player is at position (0, 1)
  And cell (0, 1) becomes visible
  And the game is in progress (PLAYING)
```

### Scenario 2.2: Move east without obstacle
```gherkin
Scenario: The player moves east into an accessible cell
  Given a game in progress
  And the player is at position (0, 0)
  And there is no wall between (0, 0) and (1, 0)
  When the player moves east (D)
  Then the player is at position (1, 0)
  And cell (1, 0) becomes visible
```

### Scenario 2.3: Move north without obstacle
```gherkin
Scenario: The player moves north into an accessible cell
  Given a game in progress
  And the player is at position (2, 2)
  And there is no wall between (2, 2) and (2, 1)
  When the player moves north (Z)
  Then the player is at position (2, 1)
  And cell (2, 1) becomes visible
```

### Scenario 2.4: Move west without obstacle
```gherkin
Scenario: The player moves west into an accessible cell
  Given a game in progress
  And the player is at position (2, 2)
  And there is no wall between (2, 2) and (1, 2)
  When the player moves west (Q)
  Then the player is at position (1, 2)
  And cell (1, 2) becomes visible
```

### Scenario 2.5: Movement blocked by interior wall
```gherkin
Scenario: The player cannot cross an intact interior wall
  Given a game in progress
  And the player is at position (1, 1)
  And there is a wall between (1, 1) and (2, 1)
  When the player moves east (D)
  Then the player stays at position (1, 1)
  And the game is in progress (PLAYING)
```

### Scenario 2.6: Movement blocked by north border
```gherkin
Scenario: The player cannot exit the grid from the north
  Given a game in progress
  And the player is at position (2, 0)
  When the player moves north (Z)
  Then the player stays at position (2, 0)
  And the game is in progress (PLAYING)
```

### Scenario 2.7: Movement blocked by south border
```gherkin
Scenario: The player cannot exit the grid from the south
  Given a game in progress
  And the player is at position (2, 4)
  When the player moves south (S)
  Then the player stays at position (2, 4)
```

### Scenario 2.8: Movement blocked by west border
```gherkin
Scenario: The player cannot exit the grid from the west
  Given a game in progress
  And the player is at position (0, 2)
  When the player moves west (Q)
  Then the player stays at position (0, 2)
```

### Scenario 2.9: Movement blocked by east border
```gherkin
Scenario: The player cannot exit the grid from the east
  Given a game in progress
  And the player is at position (4, 2)
  When the player moves east (D)
  Then the player stays at position (4, 2)
```

---

## Feature 3: Grenade Destroys Wall and Propels Player

### Scenario 3.1: Throwing a grenade destroys wall and blast propels player (Happy Path)
```gherkin
Scenario: The player uses a grenade to destroy a wall and the blast propels them
  Given a game in progress
  And the player is at position (1, 1)
  And the player has 3 grenades
  And there is a wall between (1, 1) and (2, 1)
  And cell (2, 1) is empty
  When the player throws a grenade east (D)
  Then the wall between (1, 1) and (2, 1) is destroyed
  And the player is at position (2, 1)
  And the player has 2 grenades
  And cell (2, 1) becomes visible
  And the game is in progress (PLAYING)
```

### Scenario 3.2: Destroyed wall remains open from both sides
```gherkin
Scenario: A destroyed wall allows passage in both directions without grenade
  Given a game in progress
  And the player is at position (2, 1)
  And the wall between (1, 1) and (2, 1) was previously destroyed
  When the player moves west (Q)
  Then the player is at position (1, 1)
```

### Scenario 3.3: Grenade on border wall - grenade wasted, no blast
```gherkin
Scenario: Throwing a grenade at an indestructible wall wastes the grenade without effect
  Given a game in progress
  And the player is at position (0, 2)
  And the player has 2 grenades
  When the player throws a grenade west (Q)
  Then the player stays at position (0, 2)
  And the player has 1 grenade
  And the game is in progress (PLAYING)
```

### Scenario 3.4: Grenade toward passage without wall - grenade wasted, no blast
```gherkin
Scenario: Throwing a grenade where there is no wall wastes the grenade without movement
  Given a game in progress
  And the player is at position (1, 1)
  And the player has 2 grenades
  And there is no wall between (1, 1) and (2, 1)
  When the player throws a grenade east (D)
  Then the player stays at position (1, 1)
  And the player has 1 grenade
  And the game is in progress (PLAYING)
```

### Scenario 3.5: No grenade available - action refused
```gherkin
Scenario: The player cannot throw a grenade if they have none left
  Given a game in progress
  And the player is at position (1, 1)
  And the player has 0 grenades
  And there is a wall between (1, 1) and (2, 1)
  When the player throws a grenade east (D)
  Then the player stays at position (1, 1)
  And the player has 0 grenades
  And the wall between (1, 1) and (2, 1) is intact
```

### Scenario 3.6: Grenade toward already destroyed wall - grenade wasted
```gherkin
Scenario: Throwing a grenade toward an already destroyed wall wastes the grenade
  Given a game in progress
  And the player is at position (1, 1)
  And the player has 2 grenades
  And the wall between (1, 1) and (2, 1) was previously destroyed
  When the player throws a grenade east (D)
  Then the player stays at position (1, 1)
  And the player has 1 grenade
```

---

## Feature 4: Mine = Defeat

### Scenario 4.1: Player steps on a mine - defeat (Losing Happy Path)
```gherkin
Scenario: The player moves onto a cell containing a mine and loses
  Given a game in progress
  And the player is at position (1, 1)
  And there is no wall between (1, 1) and (2, 1)
  And a mine is at position (2, 1)
  When the player moves east (D)
  Then the player is at position (2, 1)
  And cell (2, 1) becomes visible
  And the game is lost (LOST)
```

### Scenario 4.2: Chain reaction - grenade destroys wall, blast propels onto mine
```gherkin
Scenario: The grenade blast propels the player onto a hidden mine
  Given a game in progress
  And the player is at position (1, 1)
  And the player has 2 grenades
  And there is a wall between (1, 1) and (2, 1)
  And a mine is at position (2, 1)
  When the player throws a grenade east (D)
  Then the wall between (1, 1) and (2, 1) is destroyed
  And the player is at position (2, 1)
  And the player has 1 grenade
  And cell (2, 1) becomes visible
  And the game is lost (LOST)
```

### Scenario 4.3: Mine only explodes if player reaches it
```gherkin
Scenario: A mine in an adjacent unvisited cell does not affect the player
  Given a game in progress
  And the player is at position (1, 1)
  And a mine is at position (2, 1)
  And there is a wall between (1, 1) and (2, 1)
  When the player moves south (S)
  Then the player is at position (1, 2)
  And the game is in progress (PLAYING)
```

---

## Feature 5: Exit = Victory

### Scenario 5.1: Player reaches exit by walking - victory (Winning Happy Path)
```gherkin
Scenario: The player moves onto the exit and wins immediately
  Given a game in progress
  And the player is at position (3, 4)
  And there is no wall between (3, 4) and (4, 4)
  And the exit is at position (4, 4)
  When the player moves east (D)
  Then the player is at position (4, 4)
  And cell (4, 4) becomes visible
  And the game is won (WON)
```

### Scenario 5.2: Player reaches exit via grenade - victory
```gherkin
Scenario: The grenade blast propels the player onto the exit
  Given a game in progress
  And the player is at position (3, 4)
  And the player has 1 grenade
  And there is a wall between (3, 4) and (4, 4)
  And the exit is at position (4, 4)
  When the player throws a grenade east (D)
  Then the wall between (3, 4) and (4, 4) is destroyed
  And the player is at position (4, 4)
  And the player has 0 grenades
  And the game is won (WON)
```

---

## Feature 6: Fog of War

### Scenario 6.1: Only the initial cell is visible at start
```gherkin
Scenario: At the start of the game only the starting position is visible
  Given a new game with the player at position (0, 0)
  Then cell (0, 0) is visible
  And cell (1, 0) is not visible
  And cell (0, 1) is not visible
  And cell (4, 4) is not visible
```

### Scenario 6.2: Visited cells remain visible
```gherkin
Scenario: A visited cell remains visible after the player leaves it
  Given a game in progress
  And the player is at position (0, 0)
  And there is no wall between (0, 0) and (1, 0)
  And there is no wall between (1, 0) and (2, 0)
  When the player moves east (D)
  And the player moves east (D)
  Then the player is at position (2, 0)
  And cell (0, 0) is visible
  And cell (1, 0) is visible
  And cell (2, 0) is visible
```

### Scenario 6.3: Cell content is revealed upon visit
```gherkin
Scenario: The player discovers the content of a cell by visiting it
  Given a game in progress
  And the player is at position (1, 1)
  And there is no wall between (1, 1) and (2, 1)
  And cell (2, 1) contains the exit
  And cell (2, 1) is not visible
  When the player moves east (D)
  Then cell (2, 1) becomes visible
  And the player sees that cell (2, 1) contains the exit
  And the game is won (WON)
```

---

## Feature 7: Edge Cases and Deadlock States

### Scenario 7.1: Player blocked without grenades - can still move
```gherkin
Scenario: The player without grenades can move in accessible areas
  Given a game in progress
  And the player is at position (1, 1)
  And the player has 0 grenades
  And there is no wall between (1, 1) and (1, 2)
  And there is a wall between (1, 1) and (2, 1)
  When the player moves south (S)
  Then the player is at position (1, 2)
  And the game is in progress (PLAYING)
```

### Scenario 7.2: Player completely surrounded by walls
```gherkin
Scenario: The player surrounded by walls cannot move without grenades
  Given a game in progress
  And the player is at position (2, 2)
  And the player has 0 grenades
  And there is a wall between (2, 2) and (1, 2)
  And there is a wall between (2, 2) and (3, 2)
  And there is a wall between (2, 2) and (2, 1)
  And there is a wall between (2, 2) and (2, 3)
  When the player moves east (D)
  Then the player stays at position (2, 2)
  And the game is in progress (PLAYING)
```

### Scenario 7.3: Actions impossible after victory
```gherkin
Scenario: The player can no longer act after winning
  Given a won game (WON)
  And the player is at position (4, 4)
  When the player moves west (Q)
  Then the player stays at position (4, 4)
  And the game is won (WON)
```

### Scenario 7.4: Actions impossible after defeat
```gherkin
Scenario: The player can no longer act after losing
  Given a lost game (LOST)
  And the player is at position (2, 2)
  When the player moves north (Z)
  Then the player stays at position (2, 2)
  And the game is lost (LOST)
```

### Scenario 7.5: Grenade impossible after victory
```gherkin
Scenario: The player can no longer throw a grenade after winning
  Given a won game (WON)
  And the player has 2 grenades
  When the player throws a grenade north (Z)
  Then the player has 2 grenades
  And the game is won (WON)
```

---

## Feature 8: Initial Grid Configuration

### Scenario 8.1: Starting position with surrounding walls
```gherkin
Scenario: The player starts surrounded by destructible interior walls
  Given a game configuration with:
    | player position | (0, 0) |
    | grenades | 2 |
  And there is a wall between (0, 0) and (1, 0)
  And there is a wall between (0, 0) and (0, 1)
  When I create the game
  Then the player can throw a grenade east (D)
  And the player can throw a grenade south (S)
```

### Scenario 8.2: Exit and mine cannot be on the same cell
```gherkin
Scenario: Configuration refuses a mine on the exit cell
  Given a game configuration with:
    | exit position | (4, 4) |
    | mine positions | (4, 4) |
  When I create the game
  Then creation fails with error "Mine and exit cannot be on the same cell"
```

### Scenario 8.3: Player cannot start on a mine
```gherkin
Scenario: Configuration refuses a player on a mine
  Given a game configuration with:
    | player position | (0, 0) |
    | mine positions | (0, 0) |
  When I create the game
  Then creation fails with error "Player cannot start on a mine"
```

### Scenario 8.4: Player cannot start on the exit
```gherkin
Scenario: Configuration refuses a player on the exit
  Given a game configuration with:
    | player position | (4, 4) |
    | exit position | (4, 4) |
  When I create the game
  Then creation fails with error "Player cannot start on the exit"
```

---

## TDD Implementation Priority Order

### Priority 1: Foundations
1. **1.1** - Game creation (initial state)
2. **2.1** - Simple move south
3. **2.5** - Movement blocked by wall

### Priority 2: Grenade Mechanics
4. **3.1** - Grenade destroys wall + propulsion
5. **3.5** - No grenade = action refused
6. **3.4** - Grenade without wall = wasted

### Priority 3: End Conditions
7. **5.1** - Victory by walking onto exit
8. **4.1** - Defeat by walking onto mine
9. **4.2** - Chain reaction grenade-mine

### Priority 4: Edge Cases
10. **3.3** - Grenade on border
11. **7.3, 7.4** - Actions post-victory/defeat
12. **2.6-2.9** - All borders

### Priority 5: Fog of War
13. **6.1** - Initial visibility
14. **6.2** - Visited cells remain visible
15. **6.3** - Content revelation

### Priority 6: Configuration Validation
16. **8.2, 8.3, 8.4** - Invalid configurations

---

## Coverage Matrix

| Business Rule | Happy Path | Edge Cases | Error Cases |
|---------------|------------|------------|-------------|
| Movement (ZQSD) | 2.1, 2.2, 2.3, 2.4 | 2.5, 2.6, 2.7, 2.8, 2.9 | 7.3, 7.4 |
| Grenade + blast | 3.1, 3.2 | 3.3, 3.4, 3.5, 3.6 | 7.5 |
| Mine = defeat | 4.1 | 4.2 (chain), 4.3 | - |
| Exit = victory | 5.1 | 5.2 (via grenade) | - |
| Fog of war | 6.1, 6.2 | 6.3 | - |
| Game configuration | 1.1 | - | 8.2, 8.3, 8.4 |
| Deadlock states | - | 7.1, 7.2 | - |
