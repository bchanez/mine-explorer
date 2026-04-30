# TDD Analysis - Scenario 2.1: Player Moves South into Accessible Cell

**Test Type:** UNIT (core domain logic, no HTTP or database)

**Feature Area:** `src/main/java/com/mineexplorer/domain/`

**Bounded Context:** mineexplorer

## Ordered Test List (TPP + FLFI)

| # | Test Name | TPP | Contradiction | Status |
|---|-----------|-----|---------------|--------|
| 1 | should place the player at position (0, 1) when the player moves south from position (0, 0) | nil -> constant (2) | Baseline - forces introduction of `move(Direction)` method on Game and `Direction` enum; can be satisfied by always returning new position with y+1 | ✅ GREEN |
| 2 | should make the destination cell visible when the player moves south into an unvisited cell | scalar -> collection (5) | The position-only change from Test 1 is insufficient - forces updating the visibleCells set to include the new position | ✅ GREEN |

## Files to Create

- `src/main/java/com/mineexplorer/domain/Direction.java` - Enum for movement directions (SOUTH initially, others added when tested)
- No new test file needed - extend existing test class or create `src/test/java/com/mineexplorer/unit/PlayerMovementTest.java`

## Design Notes

- **Immutability:** Game is immutable; `move(Direction)` should return a new Game instance
- **Direction enum:** Start with only `SOUTH` value; add `NORTH`, `EAST`, `WEST` when driven by tests
- **Position arithmetic:** Moving SOUTH means y increases by 1 (y+1)
- **Visible cells:** The new position must be added to the existing set of visible cells
- **Wall concept:** Deferred to Scenario 2.5 - this scenario explicitly states "no wall between (0, 0) and (0, 1)"
- **Game state:** Remains PLAYING (already handled by existing code)

## Implementation Notes for TDD Agent

### Test 1: Player position changes when moving south
- Add `Direction` enum with `SOUTH` value
- Add `move(Direction direction)` method on Game
- Return new Game with updated playerPosition (y + 1 for SOUTH)
- Keep other fields unchanged (grenadeCount, visibleCells)

### Test 2: Destination cell becomes visible
- Update `move()` to add new position to visibleCells set
- Use `Set.of()` or immutable set operations to combine old visibleCells with new position
- The starting cell (0, 0) should remain visible, and (0, 1) should be added

## Edge Cases (Deferred)

- **Walls:** Scenario 2.5 will introduce wall blocking - not needed for 2.1
- **Grid boundaries:** Not mentioned in this scenario - defer until tested
- **Other directions:** NORTH, EAST, WEST to be added when their scenarios are analyzed
