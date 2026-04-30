# TDD Analysis - Grenade Destroys Wall and Propels Player (Scenario 3.1)

**Test Type:** UNIT (core domain logic, no HTTP or database keywords)

**Feature Area:** `src/main/java/com/mineexplorer/domain/`

**Bounded Context:** mineexplorer

## Ordered Test List (TPP + FLFI)

| # | Test Name | TPP | Contradiction | Status |
|---|-----------|-----|---------------|--------|
| 1 | should destroy the wall between player position and adjacent cell when player throws a grenade toward that wall | nil -> constant (2) | Baseline - the simplest grenade effect: wall removal. Can be satisfied by removing a hardcoded wall from the set. | ✅ GREEN |
| 2 | should propel the player to the adjacent cell when grenade destroys a wall | constant -> variable (3) | Test 1's implementation only removes wall but leaves player in place. Now player must also move to the target cell. | ✅ GREEN |
| 3 | should decrement grenade count when player throws a grenade toward a wall | unconditional -> conditional (4) | Tests 1-2 do not track grenade consumption. The grenade count must decrease by one after throwing. | ✅ GREEN |
| 4 | should reveal the destination cell when player is propelled by grenade blast | unconditional -> conditional (4) | Tests 1-3 do not update visibility. The propelled-to cell must be added to visibleCells. | ✅ GREEN |
| 5 | should keep game in progress when player is propelled to an empty cell after wall destruction | unconditional -> conditional (4) | Need to verify game state remains PLAYING (no mine, no exit at destination). This validates the happy path state. | ✅ GREEN |

## Files to Create

- `src/test/java/com/mineexplorer/unit/ThrowGrenadeTest.java` - Test class for grenade throwing behavior

## Design Notes

- **Immutability:** `Game.throwGrenade(Direction)` must return a new `Game` instance (same pattern as `move()`)
- **Wall tracking:** Game already stores `Set<Wall> walls` - destruction means returning new Game with that wall removed from the set
- **Wall identity:** Use `Wall.between(playerPosition, targetPosition)` to identify the wall to destroy (Wall has canonical ordering)
- **Direction reuse:** Existing `Direction` enum has `deltaX()` and `deltaY()` for computing target position
- **Visibility pattern:** Same as `move()` - add destination to `visibleCells` set
- **Test convention:** JUnit 5 `@Test` with AssertJ `assertThat()`
- **Test location:** `/Users/bastien/Dev/mine-explorer/src/test/java/com/mineexplorer/unit/ThrowGrenadeTest.java`

## Business Context

From BDD Scenario 3.1:
- Player at (1,1) with 3 grenades
- Wall exists between (1,1) and (2,1)
- Cell (2,1) is empty
- After throwing grenade east: wall destroyed, player at (2,1), 2 grenades remaining, cell visible, game PLAYING

Business Rule R2: "Blast propulsion - Only the explosion of a destructible wall generates a blast that propels the player"
