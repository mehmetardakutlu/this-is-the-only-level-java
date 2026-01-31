/**
 * Player represents the elephant character in the game. Stores the player's properties such as its coordinates.
 * Provides methods that allow other classes to access and modify the player's properties.
 */
public class Player {

    // X coordinate of the player
    private double x;
    // Y coordinate of the player
    private double y;
    // Movement principle of the player consists of considering its coordinates in the next frame,
    // updating these coordinates by performing collisions, jump and gravity mechanism and finally,
    // replacing the old coordinates with the new ones. So next coordinates of the player is substantial
    // X coordinate of the player in the next frame
    private double nextX;
    // Y coordinate of the player in the next frame
    private double nextY;
    // Width of the elephant character
    private double width = 20;
    // Height of the elephant character
    private double height = 20;
    // Velocity of the player in y direction
    private double velocityY = 0;
    // Indicates the facing direction of the elephant character
    private char facingDirection = 'R';

    /**
     * Constructor of the class.
     * @param x X position of the player.
     * @param y Y position of the player.
     */
    public Player(double x, double y){

        this.x = x;
        this.y = y;
        this.nextX = x;
        this.nextY = y;
    }

    /**
     * Sets x position of the player.
     * @param x New x position.
     */
    public void setX(double x){
        this.x = x;
    }

    /**
     * Sets y position of the player.
     * @param y New y position.
     */
    public void setY(double y){
        this.y = y;
    }

    /**
     * Returns x position of the player.
     * @return Current x position of the player.
     */
    public double getX(){
        return this.x;
    }

    /**
     * Returns y position of the player.
     * @return Current y position of the player.
     */
    public double getY(){
        return this.y;
    }

    /**
     * Returns the x position of the player in the next frame.
     * @return The x position of the player considering the next frame.
     */
    public double getNextX(){
        return nextX;
    }

    /**
     * Sets the x position of the player in the next frame.
     * @param nextX New x position for the next frame.
     */
    public void setNextX(double nextX){
        this.nextX = nextX;
    }

    /**
     * Returns the y position of the player in the next frame.
     * @return The y position of the player considering the next frame.
     */
    public double getNextY(){
        return nextY;
    }

    /**
     * Sets the y position of the player in the next frame.
     * @param nextY New y position for the next frame.
     */
    public void setNextY(double nextY){
        this.nextY = nextY;
    }

    /**
     * Returns the velocity of the player in y direction.
     * @return Current displacement in y direction in a frame.
     */
    public double getVelocityY(){
        return this.velocityY;
    }

    /**
     * Sets the velocity of the player in y direction.
     * @param velocityY New velocity in y direction.
     */
    public void setVelocityY(double velocityY){
        this.velocityY = velocityY;
    }

    /**
     * Returns the width of the elephant character.
     * @return The width of the elephant character.
     */
    public double getWidth(){
        return this.width;
    }

    /**
     * Returns the height of the elephant character.
     * @return The height of the elephant character.
     */
    public double getHeight(){
        return this.height;
    }

    /**
     * Returns the facing direction of the elephant character.
     * @return The facing direction of the elephant character, which is either 'R' or 'L'.
     */
    public char getFacingDirection(){
        return this.facingDirection;
    }

    /**
     * Sets the facing direction of the elephant character.
     * @param direction Facing direction of the elephant character. Right is represented by 'R' and left is
     * represented by 'L'.
     */
    public void setFacingDirection(char direction){
        this.facingDirection = direction;
    }

    /**
     * Respawns the player.
     * @param spawnPoint The point which the player will be respawned.
     */
    public void respawn(int[] spawnPoint){
        this.x = spawnPoint[0];
        this.y = spawnPoint[1];
        this.nextX = this.x;
        this.nextY = this.y;
        // Resets the velocityY variable to ensure that the player respawns at rest
        this.velocityY = 0;
    }

    /**
     * Draws the player based on its facing direction.
     * @param facingDirection Indicate whether a right-facing or left-facing elephant will be drawn.
     */
    public void draw(char facingDirection){
        if(facingDirection == 'R'){
            // Draws a right-facing elephant on the map
            StdDraw.picture(x, y , "misc/ElephantRight.png", width, height);
        }
        else if(facingDirection == 'L'){
            // Draws a left-facing elephant on the map
            StdDraw.picture(x, y , "misc/ElephantLeft.png", width, height);
        }
    }
}
