import java.awt.*;
import java.util.ArrayList;

/**
 * Map class constitutes the game area. It provides methods that move the player on the map,
 * draw the obstacles and other elements and create the animations of the door and button.
 */
public class Map {

    // Every map has a Stage and Player object
    private Stage stage;
    private Player player;

    // Stores the obstacles that the player collides in each frame
    private ArrayList<int[]> collidingObstacles = new ArrayList<>();

    // Obstacles List (formant is int[] = [xLeftDown , yLeftDown, xRightUp, yRightUp])
    private int[][] obstacles = {
            new int[]{0, 120, 120, 270}, new int[]{0, 270, 168, 330},
            new int[]{0, 330, 30, 480}, new int[]{0, 480, 180, 600},
            new int[]{180, 570, 680, 600}, new int[]{270, 540, 300, 570},
            new int[]{590, 540, 620, 570}, new int[]{680, 510, 800, 600},
            new int[]{710, 450, 800, 510}, new int[]{740, 420, 800, 450},
            new int[]{770, 300, 800, 420}, new int[]{680, 240, 800, 300},
            new int[]{680, 300, 710, 330}, new int[]{770, 180, 800, 240},
            new int[]{0, 120, 800, 150}, new int[]{560, 150, 800, 180},
            new int[]{530, 180, 590, 210}, new int[]{530, 210, 560, 240},
            new int[]{320, 150, 440, 210}, new int[]{350, 210, 440, 270},
            new int[]{220, 270, 310, 300}, new int[]{360, 360, 480, 390},
            new int[]{530, 310, 590, 340}, new int[]{560, 400, 620, 430}};

    // Button Coordinates
    private int[] button = new int[]{400, 390, 470, 410};

    // Color of the button
    private Color buttonColor = new Color(255,0,0);

    // Button Floor Coordinates
    private int[] buttonFloor = new int[]{400, 390, 470, 400};

    // Color of the button floor
    private Color buttonFloorColor = new Color(0,0,0);

    // Start Pipe Coordinates for Drawing
    private int[][] startPipe = {new int[]{115, 450, 145, 480},
            new int[]{110, 430, 150, 450}};

    // Exit Pipe Coordinates for Drawing
    private int[][] exitPipe = {new int[]{720, 175, 740, 215},
            new int[]{740, 180, 770, 210}};

    // Color of the pipes
    private Color pipeColor = StdDraw.ORANGE;

    // Coordinates of spike areas
    private int[][] spikes = {
            new int[]{30, 333, 50, 423}, new int[]{121, 150, 207, 170},
            new int[]{441, 150, 557, 170}, new int[]{591, 180, 621, 200},
            new int[]{750, 301, 770, 419}, new int[]{680, 490, 710, 510},
            new int[]{401, 550, 521, 570}};

    // Door Coordinates
    private int[] door = new int[]{685, 180, 700, 240};

    // Color of the door
    private Color doorColor = new Color(0,128,0);

    // How many times the button is pressed
    private int buttonPressNum = 0;
    // Whether the button is being pressed
    private boolean isButtonPressing = false;
    // Whether the player pressed the button sufficient times to open the door
    private boolean isDoorOpen = false;
    // Whether the player hit a spike
    private boolean isSpikeHit = false;

    /**
     * Constructor of the Map class.
     * @param stage Stage object of the map.
     * @param player Player object of the map.
     */
    public Map(Stage stage, Player player){

        this.stage = stage;
        this.player = player;
    }

    // Next four methods convert  [xLeftDown , yLeftDown, xRightUp, yRightUp] format to
    // StdDraw's familiar {xCenter , yCenter, halfWidth, halfHeight} format
    // This methods are particularly useful when drawing rectangles or dealing with methods involving rectangles
    /**
     * Finds the x-center of the rectangular obstacle.
     * @param coordinates Coordinates of the obstacle in form [xLeftDown , yLeftDown, xRightUp, yRightUp].
     * @return The x-center of the obstacle.
     */
    private double xCenter(int[] coordinates){
        return (coordinates[0] + coordinates[2]) / 2.0;
    }

    /**
     * Finds the y-center of the rectangular obstacle.
     * @param coordinates Coordinates of the obstacle in form [xLeftDown , yLeftDown, xRightUp, yRightUp].
     * @return The y-center of the obstacle.
     */
    private double yCenter(int[] coordinates){
        return (coordinates[1] + coordinates[3]) / 2.0;
    }

    /**
     * Finds the halfwidth of the rectangular obstacle.
     * @param coordinates Coordinates of the obstacle in form [xLeftDown , yLeftDown, xRightUp, yRightUp].
     * @return The halfwidth of the obstacle.
     */
    private double halfWidth(int[] coordinates){
        return Math.abs((coordinates[0] - coordinates[2])) / 2.0;
    }

    /**
     * Finds the halfheight of the rectangular obstacle.
     * @param coordinates Coordinates of the obstacle in form [xLeftDown , yLeftDown, xRightUp, yRightUp].
     * @return The halfheight of the obstacle.
     */
    private double halfHeight(int[] coordinates){
        return Math.abs((coordinates[1] - coordinates[3])) / 2.0;
    }

    /**
     * Checks if the player is in contact with the top surface of an obstacle.
     * @param x The x coordinate of the player.
     * @param y The y coordinate of the player.
     * @return true if the player stands at the top surface of one of the obstacles.
     * false if it is not.
     */
    private boolean isTouchingGround(double x, double y){
        for(int[] obstacle : obstacles){
            if(Math.abs(x - xCenter(obstacle)) < halfWidth(obstacle) + player.getWidth() / 2 &&
               y == yCenter(obstacle) + halfHeight(obstacle) + player.getHeight() / 2){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the player is in contact with the bottom surface of an obstacle.
     * This method is only used in stage 5 ("Inbetween gravitii").
     * @param x The x coordinate of the player.
     * @param y The y coordinate of the player.
     * @return true if the player is touching to the bottom of one of the obstacles.
     * false if it is not.
     */
    private boolean isTouchingTop(double x, double y){
        for(int[] obstacle : obstacles){
            if(Math.abs(x - xCenter(obstacle)) < halfWidth(obstacle) + player.getWidth() / 2 &&
               y == yCenter(obstacle) - halfHeight(obstacle) - player.getHeight() / 2){
                return true;
            }
        }
        return false;
    }

    /**
     * Implements gravity mechanism by updating velocityY.
     */
    private void gravity(){
        player.setVelocityY(player.getVelocityY() + stage.getGravity());
    }

    /**
     * // Reverses gravity mechanism for stage 5("Inbetween gravitii").
     */
    private void reverseGravity(){
        player.setVelocityY(player.getVelocityY() - 2 * stage.getGravity());
    }

    /**
     * Calculates x coordinate of the successive frame.
     * @param direction Indicates whether the player is moving to the right or to the left.
     */
    public void updateXCoordinate(char direction){
        if(direction == 'L'){
            player.setNextX(player.getX() - stage.getVelocityX());
        }
        else if(direction == 'R'){
            player.setNextX(player.getX() + stage.getVelocityX());
        }
        else{
            player.setNextX(player.getX());
        }
    }

    /**
     * Finds y coordinate of the successive frame.
     * If up key is pressed and the player is touching the ground, y velocity will be set to its value
     * in the current stage, which handles the jump mechanism.
     * @param direction It is set to 'U' if the up key is pressed, 'N' otherwise.
     */
    public void updateYCoordinate(char direction){
        if((direction == 'U' && isTouchingGround(player.getX(), player.getY()) ||
          stage.getStageNumber() == 2 && isTouchingGround(player.getX(), player.getY())) &&
           player.getVelocityY() <= 0){
            player.setVelocityY(stage.getVelocityY());
        }
        // If the player is not touching to the ground, then its y velocity will be updated by the gravity value
        if(stage.getStageNumber() != 4){
            if(!isTouchingGround(player.getNextX(), player.getY() + player.getVelocityY())){
                gravity();
            }
        }
        // If the current stage is stage 5 ("Inbetween gravitii"), gravity mechanism will follow different principles
        else if(stage.getStageNumber() == 4){
            // If next x coordinate of the player is in intervals [25,50) ,[75,100), [125,150) ... , [775,790]
            // the gravity points downwards, as it should normally be
            if(!isTouchingGround(player.getNextX(), player.getY() + player.getVelocityY()) &&
              ((int)player.getNextX() / 25) % 2 == 1){
                gravity();
            }
            // However, if next x coordinate of the player is in intervals [0,25) ,[50,75), [100,125) ... , [750,775)
            // the gravity points upward with a doubled magnitude, so the player must be cautious in order not to hit
            // the spikes at the top
            // The reversed gravity areas are indicated with stripes to facilitate the gameplay
            else if(!isTouchingTop(player.getNextX(), player.getY() + player.getVelocityY()) &&
                    ((int)player.getNextX() / 25) % 2 == 0){
                reverseGravity();
            }
        }
        player.setNextY(player.getY() + player.getVelocityY());
    }

    // Next four methods determine the collision boundaries of the obstacles
    // If the player comes to one of the boundaries, it touches the obstacle and when
    // it crosses one of the boundaries, it enters inside the obstacle
    /**
     * Finds the right boundary of the rectangular obstacle.
     * @param obstacle Coordinates of the obstacle in form [xLeftDown , yLeftDown, xRightUp, yRightUp].
     * @return The right boundary of the obstacle.
     */
    private double rightBoundary(int[] obstacle){
        return xCenter(obstacle) + halfWidth(obstacle) + player.getWidth() / 2;
    }

    /**
     * Finds the left boundary of the rectangular obstacle.
     * @param obstacle Coordinates of the obstacle in form [xLeftDown , yLeftDown, xRightUp, yRightUp].
     * @return The left boundary of the obstacle.
     */
    private double leftBoundary(int[] obstacle){
        return xCenter(obstacle) - halfWidth(obstacle) - player.getWidth() / 2;
    }

    /**
     * Finds the upper boundary of the rectangular obstacle.
     * @param obstacle Coordinates of the obstacle in form [xLeftDown , yLeftDown, xRightUp, yRightUp].
     * @return The upper boundary of the obstacle.
     */
    private double topBoundary(int[] obstacle){
        return yCenter(obstacle) + halfHeight(obstacle) + player.getHeight() / 2;
    }

    /**
     * Finds the lower boundary of the rectangular obstacle.
     * @param obstacle Coordinates of the obstacle in form [xLeftDown , yLeftDown, xRightUp, yRightUp].
     * @return The lower boundary of the obstacle.
     */
    private double bottomBoundary(int[] obstacle){
        return yCenter(obstacle) - halfHeight(obstacle) - player.getHeight() / 2;
    }

    /**
     * Detects the collisions with the obstacles and temporarily saves the obstacles that
     * the player collides to collidingObstacles (collidingObstacles is cleared in every frame).
     */
    private void checkCollision(){
        collidingObstacles.clear();
        for(int[] obstacle : obstacles){
            // If the player is colliding with an obstacle, player's coordinates must be enclosed by boundaries
            // of that obstacle. In this case, the obstacle is added to collidingObstacles
            if(Math.abs(player.getNextX() - xCenter(obstacle)) < halfWidth(obstacle) + player.getWidth() / 2 &&
                    Math.abs(player.getNextY() - yCenter(obstacle)) < halfHeight(obstacle) + player.getHeight() / 2){
                collidingObstacles.add(obstacle);
            }
        }
        // Since the door is an obstacle that is not in obstacles array, it must also be considered
        // If the door is completely open (which means if its length is 0) it will be ignored
        if(halfHeight(door) > 0){
            if(Math.abs(player.getNextX() - xCenter(door)) < halfWidth(door) + player.getWidth() / 2 &&
               Math.abs(player.getNextY() - yCenter(door)) < halfHeight(door) + player.getHeight() / 2){
                collidingObstacles.add(door);
            }
        }
    }

    /**
     * Checks the collisions with spikes. The collision mechanism is similar with obstacles.
     * @return true if the player's coordinates are enclosed by one of the spikes' boundaries,
     * false if they are not.
     */
    private boolean spikeCollision(){
        for(int[] spike : spikes){
            if(Math.abs(player.getX() - xCenter(spike)) < halfWidth(spike) + player.getWidth() / 2 &&
               Math.abs(player.getY() - yCenter(spike)) < halfHeight(spike) + player.getHeight() / 2){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks the collisions when the player is moving in both axes.
     * @param obstacle Coordinates of the obstacle in form [xLeftDown , yLeftDown, xRightUp, yRightUp].
     * @return "Side" if the player collides the obstacle from sides.
     * "Top/Bottom" if the collision happens at the top or bottom surface of the obstacle.
     */
    private String twoDCollisionType(int[] obstacle){
        double updateY;
        double boundaryX;
        // If player moves right
        if(player.getNextX() > player.getX()){
            boundaryX = leftBoundary(obstacle);
        }
        else{
            boundaryX = rightBoundary(obstacle);
        }
        updateY = (player.getNextX() - boundaryX) * (player.getNextY() - player.getY()) /
                (player.getNextX() - player.getX());

        if(Math.abs(player.getNextY() - updateY - yCenter(obstacle)) >= halfHeight(obstacle) + player.getHeight() / 2){
            return "Top/Bottom";
        }
        else{
            return "Side";
        }
    }

    /**
     * Handles the collision when the player is moving in both axes. Updates the player's new coordinate
     * candidates nextX and nextY.
     * @param obstacle Coordinates of the obstacle in form [xLeftDown , yLeftDown, xRightUp, yRightUp].
     */
    private void twoDCollision(int[] obstacle){
        double updateY;
        double boundaryX;
        // If the player moves right, the left boundary will be considered
        if(player.getNextX() > player.getX()){
            boundaryX = leftBoundary(obstacle);
        }
        // If the player moves left, the left boundary will be considered
        else{
            boundaryX = rightBoundary(obstacle);
        }
        updateY = (player.getNextX() - boundaryX) * (player.getNextY() - player.getY()) /
                (player.getNextX() - player.getX());
        // Updating the player's coordinates by using some trigonometry
        if(Math.abs(player.getNextY() - updateY - yCenter(obstacle)) >= halfHeight(obstacle) + player.getHeight() / 2){
            double updateX;
            double boundaryY;
            if(player.getNextY() > player.getY()){
                boundaryY = bottomBoundary(obstacle);
            }
            else{
                boundaryY = topBoundary(obstacle);
            }
            updateX = (player.getNextY() - boundaryY) * (player.getNextX() - player.getX()) /
                    (player.getNextY() - player.getY());
            player.setNextX(player.getNextX() - updateX);
            player.setNextY(boundaryY);
            player.setVelocityY(0);
        }
        else{
            player.setNextX(boundaryX);
            player.setNextY(player.getNextY() - updateY + player.getVelocityY());
        }
    }

    // Handles the collisions that happen when the player is only moving in x direction

    /**
     * Handles the collisions that happen when the player is only moving in x direction. Updates the
     * player's new x coordinate candidate nextX.
     * @param obstacle Coordinates of the obstacle in form [xLeftDown , yLeftDown, xRightUp, yRightUp].
     */
    private void horizontalCollision(int[] obstacle){
        double boundaryX;
        if(player.getNextX() > player.getX()){
            boundaryX = leftBoundary(obstacle);
        }
        else{
            boundaryX = rightBoundary(obstacle);
        }
        player.setNextX(boundaryX);
    }

    /**
     * Handles the collisions that happen when the player is only moving in y direction. Updates the
     * player's new y coordinate candidate nextY.
     * @param obstacle Coordinates of the obstacle in form [xLeftDown , yLeftDown, xRightUp, yRightUp].
     */
    private void verticalCollision(int[] obstacle){
        double boundaryY;
        if(player.getNextY() > player.getY()){
            boundaryY = bottomBoundary(obstacle);
        }
        else{
            boundaryY = topBoundary(obstacle);
        }
        player.setNextY(boundaryY);
        player.setVelocityY(0);
    }

    /**
     * Moves player in the map. Harnesses the collision methods that have defined above.
     */
    public void movePlayer(){
        // By default, the code assumes that the player does not hit a spike
        isSpikeHit = false;

        // Checking the possible collisions in that frame
        checkCollision();

        // If there are no collisions, the only thing that should be done is replacing x and y coordinates
        // by nextX and nextY, there are no need to make further adjustments
        if(collidingObstacles.isEmpty()){
            player.setX(player.getNextX());
            player.setY(player.getNextY());
            // After updating the coordinates, the spikeCollision searches for a possible hit with a spike.
            // If there is a hit, the player respawns
            // spikeCollision is called after every possible coordinate update
            if(spikeCollision()){
                isSpikeHit = true;
                buttonPressNum = 0;
                door[3] = 240;
                player.respawn(new int[]{130,465});
                player.setFacingDirection('R');
            }
        }

        // If the player is colliding with only one obstacle, it may be only moving in x direction,
        // which corresponds to a horizontal collision, or in y direction, which corresponds to a vertical collision,
        // or in both directions, which leads to a 2-dimensional collision
        // The code checks the next and current coordinates of the player and decides which collision type it should apply
        else if(collidingObstacles.size() == 1){
            if(player.getX() != player.getNextX() && player.getY() != player.getNextY()){
                twoDCollision(collidingObstacles.getFirst());
            }
            else if(player.getX() != player.getNextX() && player.getY() == player.getNextY()){
                horizontalCollision(collidingObstacles.getFirst());
            }
            else if(player.getX() == player.getNextX() && player.getY() != player.getNextY()){
                verticalCollision(collidingObstacles.getFirst());
            }
            // Updating the coordinates
            player.setX(player.getNextX());
            player.setY(player.getNextY());
            // Checking for a spike hit
            if(spikeCollision()){
                isSpikeHit = true;
                buttonPressNum = 0;
                door[3] = 240;
                player.respawn(new int[]{130,465});
                player.setFacingDirection('R');
            }
        }

        // If the player is colliding with 2 obstacles simultaneously, by the structure of the game,
        // it must be moving in both directions
        // Player may move in 8 different directions
        else if(collidingObstacles.size() == 2){
            int[] obstacle1 = collidingObstacles.getFirst();
            int[] obstacle2 = collidingObstacles.getLast();

            String obstacle1ColType = twoDCollisionType(obstacle1);

            // The case which the player moves in up-right direction
            if(player.getNextX() > player.getX() && player.getNextY() > player.getY()){
                // If the side boundaries of the obstacles are the same, it means that
                // the player is falling. In this case, the player seems to collide one obstacle
                // from its left, and the other obstacle from the top. The collision at the top is undesired
                // since it will reset the velocityY, and cause the player to stop in the air suddenly.
                // Only the side collision will be considered
                if(leftBoundary(obstacle1) == leftBoundary(obstacle2)){
                    if(obstacle1ColType.equals("Side")){
                        twoDCollision(obstacle1);
                    }
                    else{
                        twoDCollision(obstacle2);
                    }
                    player.setX(player.getNextX());
                    player.setY(player.getNextY());
                    if(spikeCollision()){
                        isSpikeHit = true;
                        buttonPressNum = 0;
                        door[3] = 240;
                        player.respawn(new int[]{130,465});
                        player.setFacingDirection('R');
                    }
                }
                // If the bottom boundaries of the obstacles are the same, it means that
                // the player hit two obstacles from below in their intersection. In this case,
                // the collision from the bottom is desired, so will be considered
                else if(bottomBoundary(obstacle1) == bottomBoundary(obstacle2)){
                    if(obstacle1ColType.equals("Top/Bottom")){
                        twoDCollision(obstacle1);
                    }
                    else{
                        twoDCollision(obstacle2);
                    }
                    player.setX(player.getNextX());
                    player.setY(player.getNextY());
                    if(spikeCollision()){
                        isSpikeHit = true;
                        buttonPressNum = 0;
                        door[3] = 240;
                        player.respawn(new int[]{130,465});
                        player.setFacingDirection('R');
                    }
                }
                // If neither of the cases above is correct, the player must have hit a corner.
                // In this case, its x and y coordinate must be adjusted.
                // x coordinate will be the left boundary of the obstacle that is at the side
                // y coordinate will be the top boundary of the obstacle that is below the player
                else{
                    // Adjusting the coordinates of the player
                    if(obstacle1ColType.equals("Side")){
                        player.setNextX(leftBoundary(obstacle1));
                        player.setNextY(bottomBoundary(obstacle2));
                    }
                    else{
                        player.setNextX(leftBoundary(obstacle2));
                        player.setNextY(bottomBoundary(obstacle1));
                    }
                    player.setX(player.getNextX());
                    player.setY(player.getNextY());
                    player.setVelocityY(0);
                    if(spikeCollision()){
                        isSpikeHit = true;
                        buttonPressNum = 0;
                        door[3] = 240;
                        player.respawn(new int[]{130,465});
                        player.setFacingDirection('R');
                    }
                }
            }
            // The case which the player moves in down-right direction
            else if(player.getNextX() > player.getX() && player.getNextY() < player.getY()){
                if(leftBoundary(obstacle1) == leftBoundary(obstacle2)){
                    if(obstacle1ColType.equals("Side")){
                        twoDCollision(obstacle1);
                    }
                    else{
                        twoDCollision(obstacle2);
                    }
                    player.setX(player.getNextX());
                    player.setY(player.getNextY());
                    if(spikeCollision()){
                        isSpikeHit = true;
                        buttonPressNum = 0;
                        door[3] = 240;
                        player.respawn(new int[]{130,465});
                        player.setFacingDirection('R');
                    }
                }
                else if(topBoundary(obstacle1) == topBoundary(obstacle2)){
                    if(obstacle1ColType.equals("Top/Bottom")){
                        twoDCollision(obstacle1);
                    }
                    else {
                        twoDCollision(obstacle2);
                    }
                    player.setX(player.getNextX());
                    player.setY(player.getNextY());
                    if(spikeCollision()){
                        isSpikeHit = true;
                        buttonPressNum = 0;
                        door[3] = 240;
                        player.respawn(new int[]{130,465});
                        player.setFacingDirection('R');
                    }
                }
                else{
                    if(obstacle1ColType.equals("Side")){
                        player.setNextX(leftBoundary(obstacle1));
                        player.setNextY(topBoundary(obstacle2));
                    }
                    else{
                        player.setNextX(leftBoundary(obstacle2));
                        player.setNextY(topBoundary(obstacle1));
                    }
                    player.setX(player.getNextX());
                    player.setY(player.getNextY());
                    player.setVelocityY(0);
                    if(spikeCollision()){
                        isSpikeHit = true;
                        buttonPressNum = 0;
                        door[3] = 240;
                        player.respawn(new int[]{130,465});
                        player.setFacingDirection('R');
                    }
                }
            }
            // The case which the player moves in up-left direction
            else if(player.getNextX() < player.getX() && player.getNextY() > player.getY()){
                if(rightBoundary(obstacle1) == rightBoundary(obstacle2)){
                    if(obstacle1ColType.equals("Side")){
                        twoDCollision(obstacle1);
                    }
                    else{
                        twoDCollision(obstacle2);
                    }
                    player.setX(player.getNextX());
                    player.setY(player.getNextY());
                    if(spikeCollision()){
                        isSpikeHit = true;
                        buttonPressNum = 0;
                        door[3] = 240;
                        player.respawn(new int[]{130,465});
                        player.setFacingDirection('R');
                    }
                }
                else if(bottomBoundary(obstacle1) == bottomBoundary(obstacle2)){
                    if(obstacle1ColType.equals("Top/Bottom")){
                        twoDCollision(obstacle1);
                    }
                    else{
                        twoDCollision(obstacle2);
                    }
                    player.setX(player.getNextX());
                    player.setY(player.getNextY());
                    if(spikeCollision()){
                        isSpikeHit = true;
                        buttonPressNum = 0;
                        door[3] = 240;
                        player.respawn(new int[]{130,465});
                        player.setFacingDirection('R');
                    }
                }
                else{
                    if(obstacle1ColType.equals("Side")){
                        player.setNextX(rightBoundary(obstacle1));
                        player.setNextY(bottomBoundary(obstacle2));
                    }
                    else{
                        player.setNextX(rightBoundary(obstacle2));
                        player.setNextY(bottomBoundary(obstacle1));
                    }
                    player.setX(player.getNextX());
                    player.setY(player.getNextY());
                    player.setVelocityY(0);
                    if(spikeCollision()){
                        isSpikeHit = true;
                        buttonPressNum = 0;
                        door[3] = 240;
                        player.respawn(new int[]{130,465});
                        player.setFacingDirection('R');
                    }
                }
            }
            // The case which the player moves in down-left direction
            else if(player.getNextX() < player.getX() && player.getNextY() < player.getY()){
                if(rightBoundary(obstacle1) == rightBoundary(obstacle2)){
                    if(obstacle1ColType.equals("Side")){
                        twoDCollision(obstacle1);
                    }
                    else{
                        twoDCollision(obstacle2);
                    }
                    player.setX(player.getNextX());
                    player.setY(player.getNextY());
                    if(spikeCollision()){
                        isSpikeHit = true;
                        buttonPressNum = 0;
                        door[3] = 240;
                        player.respawn(new int[]{130,465});
                        player.setFacingDirection('R');
                    }
                }
                else if(topBoundary(obstacle1) == topBoundary(obstacle2)){
                    if(obstacle1ColType.equals("Top/Bottom")){
                        twoDCollision(obstacle1);
                    }
                    else{
                        twoDCollision(obstacle2);
                    }
                    player.setX(player.getNextX());
                    player.setY(player.getNextY());
                    if(spikeCollision()){
                        isSpikeHit = true;
                        buttonPressNum = 0;
                        door[3] = 240;
                        player.respawn(new int[]{130,465});
                        player.setFacingDirection('R');
                    }
                }
                else{
                    if(obstacle1ColType.equals("Side")){
                        player.setNextX(rightBoundary(obstacle1));
                        player.setNextY(topBoundary(obstacle2));
                    }
                    else{
                        player.setNextX(rightBoundary(obstacle2));
                        player.setNextY(topBoundary(obstacle1));
                    }
                    player.setX(player.getNextX());
                    player.setY(player.getNextY());
                    player.setVelocityY(0);
                    if(spikeCollision()){
                        isSpikeHit = true;
                        buttonPressNum = 0;
                        door[3] = 240;
                        player.respawn(new int[]{130,465});
                        player.setFacingDirection('R');
                    }
                }
            }
        }
    }

    /**
     * Checks if the player reached the exit
     * @return true if the player reaches the exit pipe, false otherwise.
     */
    public boolean changeStage(){
        return Math.abs(player.getX() - xCenter(exitPipe[1])) < halfWidth(exitPipe[1]) + player.getWidth() / 2 &&
               yCenter(exitPipe[1]) - halfHeight(exitPipe[1]) + player.getHeight() / 2 <= player.getY() &&
               player.getY() < yCenter(exitPipe[1]) + halfHeight(exitPipe[1]) + player.getHeight() / 2;
    }

    /**
     * Presses the button and increases buttonPressNum.
     * @return true if the player presses the button, false if it doesn't.
     */
    public boolean pressButton(){
        if(Math.abs(player.getX() - xCenter(button)) < halfWidth(button) + player.getWidth() / 2 &&
           yCenter(button) - halfHeight(button) + player.getHeight() / 2 <= player.getY() &&
           player.getY() < yCenter(button) + halfHeight(button) + player.getHeight() / 2){
            if(!isButtonPressing){
                buttonPressNum ++;
            }
            isButtonPressing = true;
            return true;
        }
        isButtonPressing = false;
        return false;
    }

    /**
     * Handles the door opening animation.
     * If the current stage is not stage 4 ("Never gonna give you up"), the door will start opening when
     * the button is pressed one time. In stage 4, it will start opening at the fifth press.
     */
    private void doorCheck(){
        if(stage.getStageNumber() != 3){
            isDoorOpen = buttonPressNum >= 1;
        }
        else if(stage.getStageNumber() == 3){
            isDoorOpen = buttonPressNum >= 5;
        }
        if(isDoorOpen){
            if(halfHeight(door) > 0){
                door[3] -= 2;
            }
        }
    }

    /**
     * // Restarts the stage.
     */
    public void restartStage(){
        buttonPressNum = 0;
        door[3] = 240;
        player.respawn(new int[]{130,465});
        player.setFacingDirection('R');
    }

    /**
     * Indicates if the player hit a spike.
     * @return true if there is a spike hit, false if is not.
     */
    public boolean getIsSpikeHit(){
        return isSpikeHit;
    }

    /**
     * Returns the stage of the map
     * @return The current stage of the map.
     */
    public Stage getStage(){
        return this.stage;
    }

    /**
     * Returns the player.
     * @return The player object of the map.
     */
    public Player getPlayer(){
        return this.player;
    }

    /**
     * Draws the components of the map.
     */
    public void draw(){
        // Drawing the button and its floor
        if(!pressButton()){
            StdDraw.setPenColor(buttonColor);
            StdDraw.filledRectangle(xCenter(button), yCenter(button),
                    halfWidth(button), halfHeight(button));
        }
        StdDraw.setPenColor(buttonFloorColor);
        StdDraw.filledRectangle(xCenter(buttonFloor), yCenter(buttonFloor),
                halfWidth(buttonFloor), halfHeight(buttonFloor));
        // Drawing the player
        player.draw(player.getFacingDirection());
        StdDraw.setPenColor(stage.getColor());
        // Drawing the obstacles
        for(int[] obstacle : obstacles){
            StdDraw.filledRectangle(xCenter(obstacle), yCenter(obstacle),
                    halfWidth(obstacle), halfHeight(obstacle));
        }
        // Drawing the pipes
        StdDraw.setPenColor(pipeColor);
        for(int[] coordinates : startPipe){
            StdDraw.filledRectangle(xCenter(coordinates), yCenter(coordinates),
                    halfWidth(coordinates), halfHeight(coordinates));
        }
        for(int[] coordinates : exitPipe){
            StdDraw.filledRectangle(xCenter(coordinates), yCenter(coordinates),
                    halfWidth(coordinates), halfHeight(coordinates));
        }
        // Drawing the door
        doorCheck();
        if(halfHeight(door) > 0){
            StdDraw.setPenColor(doorColor);
            StdDraw.filledRectangle(xCenter(door), yCenter(door),
                    halfWidth(door), halfHeight(door));
        }
        // Drawing the spikes
        StdDraw.picture(xCenter(spikes[0]), yCenter(spikes[0]), "misc/Spikes.png",
                2 * halfHeight(spikes[0]), 2 * halfWidth(spikes[0]),
                90);
        StdDraw.picture(xCenter(spikes[1]), yCenter(spikes[1]), "misc/Spikes.png",
                2 * halfWidth(spikes[1]), 2 * halfHeight(spikes[1]),
                180);
        StdDraw.picture(xCenter(spikes[2]), yCenter(spikes[2]), "misc/Spikes.png",
                2 * halfWidth(spikes[2]), 2 * halfHeight(spikes[2]),
                180);
        StdDraw.picture(xCenter(spikes[3]), yCenter(spikes[3]), "misc/Spikes.png",
                2 * halfWidth(spikes[3]), 2 * halfHeight(spikes[3]),
                180);
        StdDraw.picture(xCenter(spikes[4]), yCenter(spikes[4]), "misc/Spikes.png",
                2 * halfHeight(spikes[4]), 2 * halfWidth(spikes[4]),
                270);
        StdDraw.picture(xCenter(spikes[5]), yCenter(spikes[5]), "misc/Spikes.png",
                2 * halfWidth(spikes[5]), 2 * halfHeight(spikes[5]));
        StdDraw.picture(xCenter(spikes[6]), yCenter(spikes[6]), "misc/Spikes.png",
                2 * halfWidth(spikes[6]), 2 * halfHeight(spikes[6]));
        // Drawing the strips that indicates reverse gravity areas for the stage 5 ("Inbetween gravitii")
        if(stage.getStageNumber() == 4){
            StdDraw.setPenColor(stage.getGravityStripColor());
            StdDraw.filledRectangle(12.5,360,12.5,240);
            StdDraw.filledRectangle(62.5,540,12.5,60);
            StdDraw.filledRectangle(62.5,225,12.5,105);
            StdDraw.filledRectangle(112.5,540,12.5,60);
            StdDraw.filledRectangle(112.5,300,12.5,30);
            StdDraw.filledRectangle(110,195,10,75);
            StdDraw.filledRectangle(122.5,135,2.5,15);
            StdDraw.filledRectangle(162.5,540,12.5,60);
            StdDraw.filledRectangle(159,300,9,30);
            StdDraw.filledRectangle(162.5,135,12.5,15);
            StdDraw.filledRectangle(212.5,585,12.5,15);
            StdDraw.filledRectangle(222.5,285,2.5,15);
            StdDraw.filledRectangle(212.5,135,12.5,15);
            StdDraw.filledRectangle(262.5,585,12.5,15);
            StdDraw.filledRectangle(272.5,555,2.5,15);
            StdDraw.filledRectangle(262.5,285,12.5,15);
            StdDraw.filledRectangle(262.5,135,12.5,15);
            StdDraw.filledRectangle(312.5,585,12.5,15);
            StdDraw.filledRectangle(305,285,5,15);
            StdDraw.filledRectangle(312.5,135,12.5,15);
            StdDraw.filledRectangle(322.5,180,2.5,30);
            StdDraw.filledRectangle(362.5,585,12.5,15);
            StdDraw.filledRectangle(367.5,375,7.5,15);
            StdDraw.filledRectangle(362.5,195,12.5,75);
            StdDraw.filledRectangle(412.5,585,12.5,15);
            StdDraw.filledRectangle(412.5,375,12.5,15);
            StdDraw.filledRectangle(412.5,195,12.5,75);
            StdDraw.filledRectangle(462.5,585,12.5,15);
            StdDraw.filledRectangle(462.5,375,12.5,15);
            StdDraw.filledRectangle(462.5,135,12.5,15);
            StdDraw.filledRectangle(512.5,585,12.5,15);
            StdDraw.filledRectangle(512.5,135,12.5,15);
            StdDraw.filledRectangle(562.5,585,12.5,15);
            StdDraw.filledRectangle(567.5,415,7.5,15);
            StdDraw.filledRectangle(562.5,325,12.5,15);
            StdDraw.filledRectangle(555,210,5,30);
            StdDraw.filledRectangle(567.5,180,7.5,30);
            StdDraw.filledRectangle(562.5,135,12.5,15);
            StdDraw.filledRectangle(612.5,585,12.5,15);
            StdDraw.filledRectangle(610,555,10,15);
            StdDraw.filledRectangle(610,415,10,15);
            StdDraw.filledRectangle(612.5,150,12.5,30);
            StdDraw.filledRectangle(662.5,585,12.5,15);
            StdDraw.filledRectangle(662.5,150,12.5,30);
            StdDraw.filledRectangle(712.5,555,12.5,45);
            StdDraw.filledRectangle(717.5,480,7.5,30);
            StdDraw.filledRectangle(705,315,5,15);
            StdDraw.filledRectangle(712.5,270,12.5,30);
            StdDraw.filledRectangle(712.5,147.5,12.5,27.5);
            StdDraw.filledRectangle(710,177.5,10,2.5);
            StdDraw.filledRectangle(762.5,510,12.5,90);
            StdDraw.filledRectangle(772.5,360,2.5,60);
            StdDraw.filledRectangle(762.5,270,12.5,30);
            StdDraw.filledRectangle(772.5,210,2.5,30);
            StdDraw.filledRectangle(762.5,150,12.5,30);
        }
    }
}
