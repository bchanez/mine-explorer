# mine-explorer

A grid-based exploration game written in Java, built as a reference for clean code and TDD/BDD practices.

## The Game

You play an explorer navigating a grid of rooms. Reach the exit without stepping on a mine.

- **Move** through rooms using `Z` / `Q` / `S` / `D`.
- **Throw a grenade** to blast a wall and get propelled into the next cell — useful, but risky if the cell hides a mine.
- **Fog of war**: only visited cells are revealed.

Full rules and scenarios live in [`docs/bdd-scenarios.md`](docs/bdd-scenarios.md).

## Getting Started

Requirements:

- [Docker](https://docs.docker.com/engine/install)
- [VS Code](https://code.visualstudio.com/) with the [Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) extension

Setup:

```bash
git clone https://github.com/bchanez/mine-explorer.git
cd mine-explorer
code .
```

When VS Code prompts, **reopen the folder in the dev container**. All required Java/Docker extensions are installed automatically.

## Troubleshooting

If an extension misbehaves inside the container, reload the window: `Cmd/Ctrl+Shift+P` → `Reload Window`.

## Contributing

Open an issue for bugs, suggestions, or questions.
