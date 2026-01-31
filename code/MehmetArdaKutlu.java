import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.*;
import java.util.Random;

public class MehmetArdaKutlu {

    public static void main(String[] args){

        // Enabling double buffering for smoother animations
        StdDraw.enableDoubleBuffering();

        // Creating stage objects
        Stage stage1 = new Stage(-0.45, 3.65, 10, 0, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_UP,
                "Arrow keys are required", "Arrow keys move player, press button and enter the second pipe");

        Stage stage2 = new Stage(-0.45, 3.65, 10, 1, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP,
                "Not always straight forward", "Right and left buttons reversed");

        Stage stage3 = new Stage(-2, 3.65, 24, 2, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, -1,
                "A bit bouncy here", "You jump constantly");

        Stage stage4 = new Stage(-0.45, 3.65, 10, 3, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_UP,
                "Never gonna give you up", "Press button 5 times ");

        // We were expected to choose the last stage from the original game
        // I chose "Inbetween gravitii", which is the 21st stage of the original game
        Stage stage5 = new Stage(-0.45, 3.65, 10, 4, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_UP,
                "Inbetween gravitii", "Gravity reverses in certain regions");

        // Packing stages into an ArrayList
        ArrayList<Stage> stages = new ArrayList<>();
        stages.add(stage1);
        stages.add(stage2);
        stages.add(stage3);
        stages.add(stage4);
        stages.add(stage5);

        // Creating a Random object to choose the colors randomly
        Random random = new Random();

        for(Stage stage : stages){
            // Choosing a random color for the obstacles using Random class
            stage.setColor(new Color(random.nextInt(10,246),random.nextInt(10,246),random.nextInt(10,246)));
            // Choosing the colors of the strips that indicate reverse gravity areas for the last stage
            if(stage.getStageNumber() == 4){
                // Choosing a different strip color from the obstacle color using a do-while loop
                do{
                    stage.setGravityStripColor(new Color(random.nextInt(10,246),random.nextInt(10,246),random.nextInt(10,246)));
                }while(stage.getColor().equals(stage.getGravityStripColor()));
            }
        }
        // Creating the canvas
        StdDraw.setCanvasSize(800, 600);
        StdDraw.setXscale(0, 800);
        StdDraw.setYscale(0, 600);

        while(true){
            // Creating the game object at the beginning
            Game mainGame = new Game(stages);
            // Game continues until the player finishes all the levels
            while(mainGame.getStageIndex() < stages.size()){
                // Creating Mew map object at the beginning of the game,
                // or when a stage finishes
                // or when player hits the reset button
                Map map = new Map(mainGame.getCurrentStage(), new Player(130,465));
                // Choosing a random color for the obstacles using Random class
                //map.setObstacleColor();
                // Choosing the colors of the strips that indicate reverse gravity areas for the last stage
                //if(mainGame.getStageIndex() == 4){
                //    map.setGravityStripColor();
                //}
                // Marking the beginning time of the game using currentTimeMillis
                mainGame.setStartTime(System.currentTimeMillis());
                StdDraw.setFont();
                // Running the game by calling the play method
                mainGame.play(map);
                // If the play method ends when the stage index is 0, the player must have restarted the game
                // Inner while loop breaks, and the game continues from the beginning
                if(mainGame.getStageIndex() == 0){
                   break;
                }
            }
            if(mainGame.getStageIndex() == stages.size()){
                // When the last stage is finished, the inner while loop finishes and the end game screen appears
                StdDraw.clear(StdDraw.WHITE);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledRectangle(400, 275, 400,75);
                StdDraw.setFont(new Font("sans serif", Font.PLAIN, 28));
                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.text(400,305,"CONGRATULATIONS YOU FINISHED THE LEVEL");
                StdDraw.text(400,265,"PRESS A TO PLAY AGAIN");
                StdDraw.setFont(new Font("sans serif", Font.PLAIN, 22));
                // Number of deaths and elapsed time is written in the desired form using string formatting
                StdDraw.text(400,230,String.format("You finished with %d deaths in %02d : %02d : %02d"
                        ,mainGame.getDeathNumber(),mainGame.getMinutes(),mainGame.getSeconds(),mainGame.getMilliseconds()));
                StdDraw.show();
                // After the displaying of the end game screen, code waits for the user input
                // If player chooses to press Q, the program exits
                while(true){
                    // If the player chooses to press A, while loop breaks and a new game begins
                    if(StdDraw.isKeyPressed(KeyEvent.VK_A)){
                        for(Stage stage : stages){
                            stage.setHelpDisplaying(false);
                        }
                        break;
                    }
                    else if(StdDraw.isKeyPressed(KeyEvent.VK_Q)){
                        System.exit(0);
                    }
                }
            }
        }
    }
}
