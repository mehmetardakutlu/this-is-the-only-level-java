# This Is the Only Level (Java Implementation)

An object-oriented implementation of the nostalgic Flash game "This Is the Only Level" developed using Java and the StdDraw library. In this game, the player controls a blue elephant navigating a single level design that dramatically changes its rules and physics in every stage.

## Gameplay Demo
Click to watch the gameplay and the custom gravity mechanics:
[![YouTube Demo](https://img.youtube.com/vi/wtylzdB4K0A/0.jpg)](https://youtu.be/wtylzdB4K0A)

*(Click the image above or [here](https://youtu.be/wtylzdB4K0A) to watch)*

## Project Structure
The project follows a strict Object-Oriented Programming (OOP) design pattern:
* **`code/`**: Contains the source code.
    * **`MehmetArdaKutlu.java`**: The main class initializing the game loop.
    * **`Game.java`**: Manages game logic, time tracking, and stage transitions.
    * **`Map.java`**: Handles collision detection, rendering, and physics calculations.
    * **`Player.java`**: Manages the elephant's movement, coordinates, and state.
    * **`Stage.java`**: Defines unique properties (gravity, controls, hints) for each level.
* **`report/`**: Contains the detailed project report.
* **`misc/`**: Contains assets like `ElephantRight.png` and `Spikes.png`.
* **`stdlib.jar`** The StdDraw library required to run the game. 

## Stages & Mechanics
The game features 5 distinct stages, including a custom-designed final stage:

1.  **Arrow Keys Are Required:** Standard platforming controls.
2.  **Not Always Straight Forward:** Left and Right controls are reversed.
3.  **A Bit Bouncy Here:** The Up key is disabled; the elephant jumps automatically upon touching the ground.
4.  **Never Gonna Give You Up:** The door remains locked until the button is pressed **5 times**.
5.  **Inbetween Gravitii (Custom Stage):** A unique challenge chosen from the original game where gravity reverses based on the player's X-coordinate.

## Features
* **Advanced Physics:** Custom collision detection solving quadratic equations for corner interactions.
* **Gravity Manipulation:** Dynamic gravity changes implemented in the physics engine for Stage 5.
* **OOP Design:** Modular class structure allowing easy addition of new stages.
* **Pause & Reset:** Full state management allowing players to pause or reset the game at any time.

## Detailed Report
For UML diagrams, mathematical proofs of collision logic, and implementation details, please refer to the [Project Report](report/MehmetArdaKutlu.pdf).
