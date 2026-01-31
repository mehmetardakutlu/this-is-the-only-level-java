import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Handles the interactions with the user.
 * Provides methods that takes and processes user inputs.
 * Involves the method play() that is responsible for running the game.
 */

public class Game {

    // stageIndex indicates the current stage of the game
    private int stageIndex = 0;
    // An ArrayList that stores all stages
    private ArrayList<Stage> stages;
    // deathNumber tracks the total number of spike hits and restarts
    private int deathNumber = 0;
    // Elapsed time in the game
    private double gameTime;
    // Determines the duration between two frames, values lower than 40 caused screen flickering on my PC
    private int pauseDuration = 42;
    // Time dependent variables to show the time as m : s : ms
    private int milliseconds;
    private int seconds;
    private int minutes;
    // Stores the beginning time of the game
    private double startTime;
    // Stores the last reset time
    private double resetTime = 0;
    // Indicates game reset status
    private boolean resetGame = false;
    // A flag to ignore constant presses to the restart button
    private boolean isClickingRestart = false;
    // Another flag to ignore constant presses, this time for the reset button
    private boolean isClickingReset = false;
    // Another flag to prevent the user from triggering the buttons by hovering the cursor
    // while the mouse is being pressed
    private boolean isEmptyPressing = true;

    /**
     * Constructor of the game class.
     * @param stages An ArrayList that stores the Stage objects.
     */
    public Game(ArrayList<Stage> stages){

        this.stages = stages;
    }

    /**
     * Starts and runs the game.
     * @param map Current map object of the game.
     */
    public void play(Map map){
        // A main while loop ensures the continuity of the game
        // while loop breaks only when the stage is completed or the player hits reset button
        while(true){
            // currentTime marks the exact time that the frame is processing
            double currentTime = System.currentTimeMillis();
            // Time counter is calculated by subtracting the beginning time from the current time
            // resetTime is used to go back to the time counter value at the beginning of the current stage
            // This happens when the player restarts the current stage
            gameTime = resetTime + currentTime - startTime;
            // Converting raw data in milliseconds to more useful m : s : ms format
            milliseconds = ((int) gameTime % 1000) / 10;
            seconds = (int)(gameTime % 60000) / 1000;
            minutes = (int) gameTime / 60000;

            // Taking the user input
            handleInput(map);
            // Updating the coordinates of the player, performing collisions if they exist
            map.movePlayer();
            // Incrementing the death counter if movePlayer detects that the player hit a spike
            if(map.getIsSpikeHit()){
                deathNumber ++;
            }
            // Clearing the canvas
            StdDraw.clear(StdDraw.WHITE);
            StdDraw.setPenColor(new Color(56, 93, 172)); // Color of the area
            StdDraw.filledRectangle(400, 60, 400, 60); // Drawing timer area
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(250,85,"Help");
            StdDraw.rectangle(250,85,40,15); // Help button
            StdDraw.text(550,85,"Restart");
            StdDraw.rectangle(550,85,40,15); // Restart button
            StdDraw.text(400,20,"RESET THE GAME");
            StdDraw.rectangle(400,20,80,15); // Reset button
            StdDraw.text(700, 75, "Deaths: " + deathNumber);//deathCounter
            StdDraw.text(700, 50, "Stage: " + (getCurrentStage().getStageNumber() + 1));//stageNumber
            // Using string format to show the timer in desired format
            StdDraw.text(100, 50, String.format("%02d : %02d : %02d",minutes,seconds,milliseconds));//timerText
            StdDraw.text(100,75, "Level: 1");
            // If the help button is not pressed for the current stage, the clue is shown
            if(!getCurrentStage().isHelpDisplaying()){
                StdDraw.text(400, 85, "Clue:");
                StdDraw.text(400, 55, getCurrentStage().getClue());//clueText
            }
            // If the help button is pressed for the current stage, the help message will be displayed
            else{
                StdDraw.text(400, 85, "Help:");
                StdDraw.text(400, 55, getCurrentStage().getHelp());//helpText
            }

            // Detecting if the player is hovering the cursor while constantly pressing the mouse
            isEmptyClicked();

            // If the player presses the restart button, and flags verify that it is a valid press,
            // the game restarts. When restarting, death number is incremented and the player is respawned to
            // its initial position
            if(isRestartClicked() && !isClickingRestart && !isEmptyPressing){
                isClickingRestart = true;
                deathNumber ++;
                getCurrentStage().setHelpDisplaying(false);
                startTime = System.currentTimeMillis();
                map.restartStage();
            }

            // If the player presses the reset button, and flags verify that it is a valid press,
            // The game resets. When resetting, a new Game object is created
            isResetClicked();
            if(resetGame && !isClickingReset && !isEmptyPressing){
                stageIndex = 0;
                for(Stage stage : stages){
                    stage.setHelpDisplaying(false);
                }
                map.draw();
                // Before resetting, "RESETTING THE GAME..." banner is shown for 2 seconds
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledRectangle(400, 340, 400,75);
                StdDraw.setFont(new Font("sans serif", Font.PLAIN, 50));
                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.text(400,340,"RESETTING THE GAME...");
                StdDraw.show();
                StdDraw.pause(2000);
                break;
            }

            // If the player presses the help button, and flags verify that it is a valid press,
            // isHelpClicked variable in stage class becomes true, and help message is displayed instead of clue
            if(!getCurrentStage().isHelpDisplaying()){
                if(isHelpClicked() && !isEmptyPressing){
                    getCurrentStage().setHelpDisplaying(true);
                }
            }

            // Calling the draw method from Map class, drawing the components of the map
            map.draw();

            // Checking if the player reached exit by calling changeState method from map
            if(map.changeStage()){
                // If the player reaches exit,
                // resetTime will be incremented by the time spent for the completed stage
                resetTime += System.currentTimeMillis() - startTime;
                // stageIndex will be incremented
                stageIndex ++;
                // Before moving into the next stage, "You passed the stage But is the level over?!" banner
                // is displayed for two seconds
                isEmptyPressing = true;
                if(stageIndex < stages.size()){
                    StdDraw.setPenColor(StdDraw.GREEN);
                    StdDraw.filledRectangle(400, 275, 400,75);
                    StdDraw.setFont(new Font("sans serif", Font.PLAIN, 30));
                    StdDraw.setPenColor(StdDraw.WHITE);
                    StdDraw.text(400,300,"You passed the stage");
                    StdDraw.text(400,250,"But is the level over?!");
                    StdDraw.show();
                    StdDraw.pause(2000);
                }
                // While loop breaks, and the code continues from the next stage with a new map object
                break;
            }
            // Making the animation by showing the contents of the canvas and then pausing for a certain time
            StdDraw.show();
            StdDraw.pause(pauseDuration);
        }
    }

    /**
     * Handles player input and interactions (keyboard and mouse).
     * @param map The Map object of the game.
     */
    private void handleInput(Map map){
        // If left key is pressed, a "left-facing elephant" will be displayed
        if(StdDraw.isKeyPressed(KeyEvent.VK_LEFT)){
            map.getPlayer().setFacingDirection('L');
            // If the player is currently at stage 2 ("not always straight forward"), it will move right
            // instead of moving left
            if(KeyEvent.VK_LEFT == map.getStage().getKeyCodes()[0]){
                map.updateXCoordinate('R');
            }
            else if(KeyEvent.VK_LEFT == map.getStage().getKeyCodes()[1]){
                map.updateXCoordinate('L');
            }
        }
        // In the original game, I observed that left arrow key has a priority over right arrow key
        // which means if the player presses right and left keys simultaneously, the game ignores the right key press.
        // So I decided to implement this mechanism to my code
        // Right key presses are handled only if the left key is not pressed
        // When the right key is pressed, a "right-facing elephant" will be displayed
        else if(StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)){
            map.getPlayer().setFacingDirection('R');
            if(KeyEvent.VK_RIGHT == map.getStage().getKeyCodes()[0]){
                map.updateXCoordinate('R');
            }
            // If the player is currently at stage 2 ("not always straight forward"), it will move left
            // instead of moving right
            else if(KeyEvent.VK_RIGHT == map.getStage().getKeyCodes()[1]){
                map.updateXCoordinate('L');
            }
        }
        // If the up arrow key is pressed, the player will jump if it is touching the ground
        // In stage 3 ("a bit bouncy here"), the up key is disabled since the player jumps consistently
        if(StdDraw.isKeyPressed(map.getStage().getKeyCodes()[2])){
            map.updateYCoordinate('U');
        }
        else{
            map.updateYCoordinate('N');
        }
    }

    /**
     * This method checks if the player pressed the mouse when the cursor is on the reset button.
     */
    private void isResetClicked(){
        if(StdDraw.isMousePressed()){
            resetGame = 320 <= StdDraw.mouseX() && StdDraw.mouseX() <= 480 &&
                    5 <= StdDraw.mouseY() && StdDraw.mouseY() <= 35;
        }
        else{
            isClickingReset = false;
            resetGame = false;
        }
    }

    /**
     * This method checks if the player pressed the mouse when the cursor is on the restart button.
     * @return true if the restart button is pressed, false if it is not.
     */
    private boolean isRestartClicked(){
        if(StdDraw.isMousePressed()){
            return 510 <= StdDraw.mouseX() && StdDraw.mouseX() <= 590 &&
                   70 <= StdDraw.mouseY() && StdDraw.mouseY() <= 100;
        }
        isClickingRestart = false;
        return false;
    }

    /**
     * This method checks if the player pressed the mouse when the cursor is on the help button
     * @return true if the help button is pressed, false if it is not.
     */
    private boolean isHelpClicked(){
        if(StdDraw.isMousePressed()){
            return 210 <= StdDraw.mouseX() && StdDraw.mouseX() <= 290 &&
               70 <= StdDraw.mouseY() && StdDraw.mouseY() <= 100;
            }
        return false;
    }

    /**
     * This method checks if the player is hovering the cursor around screen while pressing the mouse.
     * If the player is doing so, none of the three buttons will be activated.
     */
    private void isEmptyClicked(){
        if(StdDraw.isMousePressed()){
            // Checks if the cursor is on one of the three buttons. If not, isEmptyPressing flag will become true
            if((!(320 <= StdDraw.mouseX() && StdDraw.mouseX() <= 480 &&
                    5 <= StdDraw.mouseY() && StdDraw.mouseY() <= 35) &&
                    !(510 <= StdDraw.mouseX() && StdDraw.mouseX() <= 590 &&
                            70 <= StdDraw.mouseY() && StdDraw.mouseY() <= 100) &&
                    !(210 <= StdDraw.mouseX() && StdDraw.mouseX() <= 290 &&
                            70 <= StdDraw.mouseY() && StdDraw.mouseY() <= 100)) && !isEmptyPressing){
                isEmptyPressing = true;
            }
        }
        else{
            isEmptyPressing = false;
        }
    }

    /**
     * Sets the starting time of the game.
     * @param startTime The starting time of the game.
     */
    public void setStartTime(double startTime){
        this.startTime = startTime;
    }

    /**
     * Resturns the index of the current stage.
     * @return The index of the current stage.
     */
    public int getStageIndex(){
        return stageIndex;
    }

    /**
     * Returns the current stage object of the game.
     * @return The stage object of the game.
     */
    public Stage getCurrentStage(){
        return stages.get(stageIndex);
    }

    /**
     * Returns how many times did the player die.
     * @return The death number of the player.
     */
    public int getDeathNumber() {
        return deathNumber;
    }

    /**
     * The minute part of the game time.
     * @return Minute part of the game time.
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * The second part of the game time.
     * @return Second part of the game time.
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * The millisecond part of the game time.
     * @return Millisecond part of the game time.
     */
    public int getMilliseconds(){
        return milliseconds;
    }
}
