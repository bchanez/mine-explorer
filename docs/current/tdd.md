# TDD Analysis - Wall-Blocked Movement (Scenario 2.5)

**Test Type:** UNIT (core domain logic, no HTTP or database)

**Feature Area:** `src/main/java/com/mineexplorer/domain/`

**Bounded Context:** Mine Explorer

## Ordered Test List (TPP + FLFI)

| # | Test Name | TPP | Contradiction | Status |
|---|-----------|-----|---------------|--------|
| 1 | should keep player at current position when moving east and there is a wall between current position and target position | nil -> constant (2) | Baseline - can be satisfied by returning the same game (no movement) when moving east | GREEN |
| 2 | should move player to target position when moving east and there is no wall between current position and target position | unconditional -> conditional (4) | The constant "stay in place" from Test 1 is wrong when no wall exists -> forces a conditional check on wall presence | GREEN |
| 3 | should keep player at current position when moving south and there is a wall between current position and target position | constant -> variable (3) | The east-only wall check from Test 2 must also work for south direction -> forces extracting direction as a variable in wall lookup | GREEN |

## Files to Create

- `src/main/java/com/mineexplorer/domain/Wall.java` - Value object representing a wall between two adjacent positions
- No new test file needed if `GameTest.java` exists; otherwise create `src/test/java/com/mineexplorer/unit/GameTest.java`

## Design Notes

- **Wall representation:** A wall exists between two positions. Use a value object `Wall` that normalizes the two positions (e.g., lexicographic ordering) so `Wall((1,1), (2,1))` equals `Wall((2,1), (1,1))`. This enforces R1 (shared wall).
- **GameConfiguration extension:** Add `Set<Wall> walls` to `GameConfiguration` to define interior walls at game creation.
- **Direction.EAST:** Must be added to the `Direction` enum (currently only has `SOUTH`).
- **Movement delta:** Each direction has a delta (EAST: x+1, SOUTH: y+1, etc.). Extract this from the current hardcoded logic.
- **Wall lookup in move():** Before moving, compute target position from direction, check if a wall exists between current and target, then either stay or move.
- **Existing pattern:** `Game` is immutable, `move()` returns a new `Game` instance.
- **Test location:** Tests in `src/test/java/com/mineexplorer/unit/` per context user provided.

## Rationale for TPP Ordering

**Test 1** establishes the wall-blocked case first because it is the simplest transformation: when a wall exists, return the same state (constant behavior). This can be satisfied by simply returning `this` or a copy with unchanged position.

**Test 2** introduces the contradiction: movement without a wall must succeed. The previous constant "stay in place" fails this test, forcing a conditional on wall presence.

**Test 3** validates that wall checking works across directions (not just east). This generalizes the direction-specific logic into a variable, ensuring the implementation is not hardcoded to east-only.

## Edge Cases Deferred

The following are related but belong to separate TDD cycles:

- **Border blocking (2.6-2.9):** Player cannot exit the grid. Borders behave like indestructible walls per R4. Implement after interior walls work.
- **Direction.NORTH and WEST:** Additional directions for full ZQSD support. Add incrementally after east/south are complete.
- **Grenade destruction of walls (3.1-3.6):** Separate feature, depends on wall concept being established first.
