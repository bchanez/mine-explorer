# TDD Analysis - Player Cannot Throw Grenade With None Left (Scenario 3.5)

**Test Type:** UNIT (core domain logic, no HTTP or database keywords)

**Feature Area:** `src/main/java/com/mineexplorer/domain/`

**Bounded Context:** mineexplorer

## Ordered Test List (TPP + FLFI)

| # | Test Name | TPP | Contradiction | Status |
|---|-----------|-----|---------------|--------|
| 1 | should keep player at current position when throwing grenade with no grenades remaining | unconditional -> conditional (4) | Current `throwGrenade()` always performs the action. With 0 grenades, the method must return the same game state unchanged. Forces a guard clause checking `grenadeCount > 0`. | GREEN |

## Files to Create

- None - extends existing `ThrowGrenadeTest.java` test class and `Game.throwGrenade()` method

## Design Notes

- **Existing test class:** `/Users/bastien/Dev/mine-explorer/src/test/java/com/mineexplorer/unit/ThrowGrenadeTest.java`
- **Existing method:** `Game.throwGrenade(Direction)` at line 58-69 currently unconditionally destroys wall, moves player, decrements grenades
- **Guard clause needed:** When `grenadeCount == 0`, return `this` (no state change) - same pattern as wall-blocked movement in `move()` method (line 50-52)
- **Single test sufficient:** The scenario tests a single guard condition. One test validates all assertions (position unchanged, grenade count unchanged, wall intact) because they all stem from the same cause: the grenade throw is refused.
- **Immutability preserved:** Returning `this` when action refused is idiomatic for immutable objects

## Business Context

From BDD Scenario 3.5:
- Player at (1,1) with 0 grenades
- Wall exists between (1,1) and (2,1)
- Player attempts to throw grenade east
- After action: player still at (1,1), still 0 grenades, wall still intact

Business Rule: A grenade throw requires at least one grenade in inventory. Without grenades, the action is silently refused (no state change).

## TPP Rationale

This is a **single-test scenario** because:

1. **One cause, multiple effects:** The guard clause `if (grenadeCount == 0) return this;` produces all observed outcomes simultaneously:
   - Player position unchanged
   - Grenade count unchanged (still 0)
   - Wall unchanged (intact)

2. **No TPP progression needed:** There is no simpler transformation that could be contradicted. The first (and only) test immediately demands a conditional.

3. **Atomic business rule:** "Cannot throw without grenades" is indivisible - you cannot test "position unchanged" without implicitly also testing "wall unchanged."

The test should assert all three conditions (position, grenade count, wall presence) in one test to document the complete business rule.
