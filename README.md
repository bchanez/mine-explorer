# mine-explorer

Welcome to mine-explorer project! This document explains how to set up your development environment, run the project, and code effectively using Visual Studio Code with Docker.

## Project Overview

This project aims to demonstrate clean coding practices and adherence to best practices to serve as a reference example. The core objective is to create a well-structured and maintainable codebase that showcases effective Java programming techniques.

### Project Description

The project is a game of exploration set on a grid-based board composed of rooms. In this game, you play as a character who moves from room to room, aiming to find the exit while avoiding mines that could lead to the character’s demise. 

#### Key Features:

- **Exploration Mechanics**: Navigate through a grid of rooms, with the goal of finding the exit.
- **Mines**: Avoid mines placed randomly across the grid, which can end the game if encountered.
- **Inventory**: The player can collect and use various items to assist in the exploration.

Feel free to suggest additional items or mechanics that could enhance the gameplay experience.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Environment Setup](#environment-setup)
3. [VSCode Extension Troubleshooting](#vscode-extension-troubleshooting)
4. [Contact](#contact)

## Prerequisites

Before you begin, make sure you have the following tools installed on your machine:

- [Docker](https://docs.docker.com/engine/install): To run the application in containers.
- [Visual Studio Code](https://code.visualstudio.com/): Your code editor.
- [Dev Containers Extension for VSCode](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers): Open any folder or repository inside a Docker container.

## Environment Setup

1. **Clone the Project Repository**

    Clone the project repository from Git:

    ```bash
    git clone https://github.com/bchanez/mine-explorer.git
    cd mine-explorer
    ```

2. **Open the Project in VSCode**

    Open VSCode in the project directory:

    ```bash
    code .
    ```

3. **Container Mode**

    Activate Container Mode: When you open the project in VSCode, it should automatically detect the .devcontainer file and prompt you to open the project in a Docker container. Accept this option to configure your development environment within a container.
    Automatic Extensions: The Docker container will automatically install the necessary extensions for Java and Docker development.

## VSCode Extension Troubleshooting

If you encounter issues with VSCode extensions in container mode, follow these steps:

1. **Reload the Window**

    If an extension is not loading properly, try reloading the VSCode window:
    * Press Ctrl+P (or Cmd+P on macOS) to open the Command Palette.
    * Type >Reload Window and press Enter.

## Contact

If you encounter any issues or need assistance, please open an issue. I will do my best to assist you as quickly as possible.
