package com.mineexplorer.console;

import com.mineexplorer.write.application.domain.models.Game;
import com.mineexplorer.write.application.domain.models.GameState;
import com.mineexplorer.write.application.domain.models.Position;

public class GameRenderer {

    public String render(Game game) {
        var output = new StringBuilder();
        output.append("\n");
        output.append("Grenades: ").append("💣".repeat(game.grenadeCount())).append("\n");
        output.append("\n");

        var bounds = game.visibleBounds();

        for (int y = bounds.minY(); y <= bounds.maxY(); y++) {
            output.append(renderCellRow(game, bounds, y));
            output.append("\n");

            var wallRow = renderWallRow(game, bounds, y);
            if (wallRow.contains("─")) {
                output.append(wallRow).append("\n");
            }
        }

        return output.toString();
    }

    private String renderCellRow(Game game, com.mineexplorer.write.application.domain.models.VisibleBounds bounds, int y) {
        var row = new StringBuilder();
        for (int x = bounds.minX(); x <= bounds.maxX(); x++) {
            var pos = new Position(x, y);
            row.append(cellToString(game, pos));
            row.append(game.hasVisibleWallToRight(pos) ? "│" : " ");
        }
        return row.toString();
    }

    private String renderWallRow(Game game, com.mineexplorer.write.application.domain.models.VisibleBounds bounds, int y) {
        var wallRow = new StringBuilder();
        for (int x = bounds.minX(); x <= bounds.maxX(); x++) {
            var pos = new Position(x, y);
            wallRow.append(game.hasVisibleWallBelow(pos) ? "──" : "  ");
            wallRow.append(" ");
        }
        return wallRow.toString();
    }

    private String cellToString(Game game, Position pos) {
        if (pos.equals(game.playerPosition())) {
            return "🧑";
        }
        if (!game.isVisibleCell(pos)) {
            return "  ";
        }
        if (game.state() == GameState.WON && pos.equals(game.playerPosition())) {
            return "🚪";
        }
        return "··";
    }
}
