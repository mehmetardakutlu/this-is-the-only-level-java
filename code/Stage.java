import java.awt.*;

/**
 * Stores the unique characteristics of every step of the game.
 * Provides methods that enable access and modification of the Stage characteristics.
 */
public class Stage {
    private int stageNumber;
    private double gravity;
    private double velocityX;
    private double velocityY;
    private int rightCode;
    private int leftCode;
    private int upCode;
    private String clue;
    private String help;
    // Color of the obstacles
    private Color color;
    // Color of the strips (only used in stage 5("Inbetween gravitii")
    private Color gravityStripColor;
    // Indicates whether the help message is displaying
    private boolean isHelpDisplaying;

    /**
     * Constructor of the class.
     * @param gravity The gravity value at the stage.
     * @param velocityX The distance that the player travels in one frame on the x-axis.
     * @param velocityY The distance that the player travels in one frame on the y-axis.
     *                  This variable usually interacts with jump and gravity mechanism.
     * @param stageNumber The index of the stage.
     * @param rightCode Indicates which key moves player to right.
     * @param leftCode Indicates which key moves player to left.
     * @param upCode Indicates which key makes the player jump.
     *               In stage 3("a bit bouncy here"), the up code is -1, which corresponds to none of the keys,
     *               which means that the jump key is disabled at this stage.
     * @param clue Clue string for the stage.
     * @param help Help string for the stage.
     */
    public Stage(double gravity, double velocityX, double velocityY, int stageNumber, int rightCode,
          int leftCode, int upCode, String clue, String help){

        this.gravity = gravity;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.stageNumber = stageNumber;
        this.rightCode = rightCode;
        this.leftCode = leftCode;
        this.upCode = upCode;
        this.clue = clue;
        this.help = help;
        this.isHelpDisplaying = false;
    }

    /**
     * Returns the stage number.
     * @return The unique stage number.
     */
    public int getStageNumber(){
        return this.stageNumber;
    }

    /**
     * Returns the gravity value.
     * @return Gravity value of the stage.
     */
    public double getGravity(){
        return this.gravity;
    }

    /**
     * Returns the velocity in x direction.
     * @return How fast can the player move in x direction.
     */
    public double getVelocityX(){
        return this.velocityX;
    }

    /**
     * Returns the velocity in y direction.
     * @return How high the player can jump.
     */
    public double getVelocityY(){
        // Returns the velocity in y direction
        return this.velocityY;
    }

    /**
     * Returns the key codes of the stage in the form [rightCode, leftCode, upCode].
     * @return An array storing the unique key codes for the stage.
     */
    public int[] getKeyCodes(){
        return new int[]{rightCode, leftCode, upCode};
    }

    /**
     * Returns the clue for the stage.
     * @return The clue message of the stage.
     */
    public String getClue(){
        return this.clue;
    }

    /**
     * Returns the displaying status of the help message in current stage.
     * @return true if the help message is displaying, false if it is not.
     */
    public boolean isHelpDisplaying() {
        return isHelpDisplaying;
    }

    /**
     * Allows the modification of isHelpDisplaying variable from other classes.
     * @param isHelpDisplaying Indicates whether the help message is displaying for the current stage.
     */
    public void setHelpDisplaying(boolean isHelpDisplaying){
        this.isHelpDisplaying = isHelpDisplaying;
    }

    /**
     * Returns the help for the stage.
     * @return The help message of the stage.
     */
    public String getHelp(){
        return this.help;
    }

    /**
     * Returns the color of the obstacles.
     * @return The color of the obstacles.
     */
    public Color getColor(){
        return this.color;
    }

    /**
     * Sets a Color object for the current stage.
     * @param color Color of the obstacles.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Returns the color of the gravity strips. Only used in stage 5 ("Inbetween gravitii").
     * @return The color of the gravity strips.
     */
    public Color getGravityStripColor() {
        return gravityStripColor;
    }

    /**
     * Sets the color of the gravity strips. Only used in stage 5 ("Inbetween gravitii").
     * @param gravityStripColor Color of the gravity strips.
     */
    public void setGravityStripColor(Color gravityStripColor){
        this.gravityStripColor = gravityStripColor;
    }
}
