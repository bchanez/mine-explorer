# TDD Analysis - Scenario 1.1: Standard Game Creation

**Test Type:** UNIT (core domain logic, no HTTP or database)

**Feature Area:** `src/main/java/com/mineexplorer/domain/` (to be created)

**Bounded Context:** mineexplorer

## Ordered Test List (TPP + FLFI)

| # | Test Name | TPP | Contradiction | Status |
|---|-----------|-----|---------------|--------|
| 1 | should produce a game in PLAYING state when a valid game configuration is provided | nil -> constant (2) | Baseline - can be satisfied by always returning PLAYING state | GREEN |
| 2 | should place the player at the configured starting position when the game is created | constant -> variable (3) | The constant PLAYING state alone is insufficient - forces storing and returning player position from configuration | GREEN |
| 3 | should give the player the configured number of grenades when the game is created | constant -> variable (3) | The player position alone is insufficient - forces storing and returning grenade count from configuration | GREEN |
| 4 | should make only the starting cell visible when the game is created | scalar -> collection (5) | A single position value is insufficient - forces a collection to track visible cells, initialized with the starting position | GREEN |

## Files to Create

- `src/main/java/com/mineexplorer/domain/Game.java` - Main aggregate root
- `src/main/java/com/mineexplorer/domain/GameState.java` - Enum for game states (PLAYING, WON, LOST)
- `src/main/java/com/mineexplorer/domain/Position.java` - Value object for coordinates
- `src/main/java/com/mineexplorer/domain/GameConfiguration.java` - Configuration value object
- `src/test/java/com/mineexplorer/unit/CreateGameTest.java` - Test class

## Design Notes

- **Grid coordinates:** (x, y) with (0, 0) at top-left, Y increases downward
- **Player position:** Stored as a Position value object
- **Visibility:** Collection of visible positions, initialized with starting position only (fog of war)
- **Grenades:** Integer count, fixed at game start
- **Mine positions:** Part of configuration, not visible to player initially
- **Exit position:** Part of configuration, discovery mechanic to be added later
- **Game state:** Enum with at least PLAYING value (WON, LOST added when needed by future tests)
- **Aggregate design:** Game is the aggregate root containing player state, grid visibility, and game rules

## Implementation Notes for TDD Agent

### Test 1: Game in PLAYING state
- Create `Game.create(GameConfiguration config)` static factory
- Return a Game with state = PLAYING
- GameState enum with only PLAYING value initially

### Test 2: Player at configured position
- GameConfiguration must include playerPosition
- Game must expose `playerPosition()` method
- Position value object with x and y coordinates

### Test 3: Player has configured grenades
- GameConfiguration must include grenadeCount
- Game must expose `grenadeCount()` method or `playerGrenades()`

### Test 4: Only starting cell visible
- Game must expose `visibleCells()` or `isVisible(Position)` method
- Collection of Position, containing only the starting position initially
- Validates fog of war mechanic
