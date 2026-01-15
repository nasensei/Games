package TwentyFortyEight;

import org.checkerframework.checker.units.qual.A;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Queue;

public class App extends PApplet {
    //private Queue<Move> moveQueue = new LinkedList<>();
    private boolean isAnimating = false;
    private int startTime;
    private int finalTime = -1;
    private boolean gameOver = false;
    private static int GRID_SIZE = 4; // 4x4 grid default value
    public static final int CELLSIZE = 100; // Cell size in pixels
    public static final int CELL_BUFFER = 8; // Space between cells
    public static final int FPS = 30;

    private Cell[][] board;

    public static Random random = new Random();

    private PFont font;
    public PImage eight;

    // Feel free to add any additional methods or attributes you want. Please put
    // classes in different files.

    public App() {  
    }

    /**
     * Initialise the setting of the window size.
     */
    @Override
    public void settings() {
        if (args != null && args.length > 0) {
            try {
                int num = Integer.parseInt(args[0]);
                if (num > 2) {
                    GRID_SIZE = num;
                    //System.out.println(GRID_SIZE);
                }
            } catch (NumberFormatException ignored) {
            } 
        }
        size(GRID_SIZE * CELLSIZE, GRID_SIZE * CELLSIZE);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player
     * and map elements.
     */
    @Override
    public void setup() {
        frameRate(FPS);
        // See PApplet javadoc:
        // loadJSONObject(configPath)
        this.board = new Cell[GRID_SIZE][GRID_SIZE];
        this.eight = loadImage(this.getClass().getResource("8.png").getPath().toLowerCase(Locale.ROOT).replace("%20", ""));

        // create attributes for data storage, eg board
        for (int i = 0; i < board.length; i++) {
            for (int i2 = 0; i2 < board[i].length; i2++) {
                board[i][i2] = new Cell(i2, i);
            }
        }

        //placeRandomTile();

        // Timer setup
        startTime = millis();
        gameOver = false;
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
    @Override
    public void keyPressed(KeyEvent event) {
        if (isAnimating) return;  // Prevent input during animation
        boolean moved = false;
        int key = event.getKeyCode();

        if (key == PApplet.UP) {
            moved = goUp();
            placeRandomTile();
            gameOver = !gameContinue();
        } else if (key == PApplet.DOWN) {
            moved = goDown();
            placeRandomTile();
            gameOver = !gameContinue();
        } else if (key == PApplet.LEFT) {
            moved = goLeft();
            placeRandomTile();
            gameOver = !gameContinue();
        } else if (key == PApplet.RIGHT) {
            moved = goRight();
            placeRandomTile();
            gameOver = !gameContinue();
        } else if (event.getKey() == 'r' || event.getKey() == 'R') {
            gameRestart();
            gameOver = !gameContinue();
        }
    }

    // board movement
    private boolean goLeft() {
        boolean moved = false;

        for (int row = 0; row < GRID_SIZE; row++) {
            List<Integer> values = new ArrayList<>();
            
            // non-zero values
            for (int col = 0; col < GRID_SIZE; col++) {
                int num = board[row][col].getValue();
                if (num != 0) {
                    values.add(num);
                }
            }

            // merge equal values
            List<Integer> merged = new ArrayList<>();
            for (int i = 0; i < values.size(); i++) {
                if (i + 1 < values.size() && values.get(i).equals(values.get(i + 1))) {
                    merged.add(values.get(i) * 2);
                    i++;
                } else {
                    merged.add(values.get(i));
                }
            }

            // fill remaining with 0s
            while (merged.size() < GRID_SIZE) {
                merged.add(0);
            }
            
            //update board
            for (int col = 0; col < GRID_SIZE; col++) {
                if (board[row][col].getValue() != merged.get(col)) {
                    board[row][col].setValue(merged.get(col));
                    moved = true; //check movement
                }
            }
        }
        

        return moved;
    }

    private boolean goUp() {
        counterClockWise();
        boolean moved = goLeft();
        clockWise();
        return moved;
    }

    private boolean goDown() {
        clockWise();
        boolean moved = goLeft();
        counterClockWise();
        return moved;
    }

    private boolean goRight() {
        clockWise();
        clockWise();
        boolean moved = goLeft();
        counterClockWise();
        counterClockWise();
        return moved;
    }
    
    // rotate board 90deg:
    private void clockWise() {
        Cell[][] tempBoard = new Cell[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                tempBoard[col][GRID_SIZE - 1 - row] = board[row][col];
            }
        }
        board = tempBoard;
    }
    
    private void counterClockWise() {
        Cell[][] tempBoard = new Cell[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                tempBoard[GRID_SIZE - 1 - col][row] = board[row][col];
            }
        }
        board = tempBoard;
    }


    //spawning tile
    private void placeRandomTile() {
        List<Cell> voidCells = new ArrayList<>();

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (board[row][col].isEmpty()) {
                    voidCells.add(board[row][col]);
                }
            }
        }

        if (voidCells.isEmpty()) {
            return;
        } else {
            Cell spwanCell = voidCells.get(random.nextInt(voidCells.size()));
            spwanCell.place();
        }
    }


    //restart
    private void gameRestart() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                board[row][col].setValue(0);
            }
        }

        //Timer setup
        startTime = millis();
        gameOver = false;
    }

    private boolean gameContinue() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                int currentCell = board[row][col].getValue();
                
                // Check if the current cell is empty
                if (currentCell == 0) {
                    return true;
                }
                // right neighbouring check
                if (col < GRID_SIZE - 1 && currentCell == board[row][col + 1].getValue()) {
                    return true;
                }
                // down neighbouring check
                if (row < GRID_SIZE - 1 && currentCell == board[row + 1][col].getValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == PConstants.LEFT) {
            Cell current = board[e.getY()/App.CELLSIZE][e.getX()/App.CELLSIZE];
            current.place();
        }
    }

    /**
     * Draw all elements in the game by current frame.
     */
    @Override
    public void draw() {
        // draw game board
        this.textSize(40);
        this.strokeWeight(15);
        
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col].updatePos();
                board[row][col].draw(this);
            }
        }

        // Timer
        fill(40);
        textAlign(RIGHT, TOP);
        textSize(15);
    
        int displayTime;
        if (!gameOver) {
            displayTime = (millis() - startTime) / 1000;
        } else {
            if (finalTime == -1) {
                finalTime = millis(); // capture game over time
            }
            displayTime = (finalTime - startTime) / 1000;
            fill (200,50,50);
            textAlign(CENTER, CENTER);
            textSize(35);
            text("GAME OVER", width / 2f, height / 2f);
            text(displayTime + "s", width - 30, 20);
            return;
        }
        text(displayTime + "s", width - 30, 20);
        
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                int num = Integer.parseInt(args[0]);
                if (num >= 3) {
                    GRID_SIZE = num;
                }
            } catch (NumberFormatException ignored) {
            }
        }
        PApplet.main("TwentyFortyEight.App");
    }

}
