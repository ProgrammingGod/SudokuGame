import java.util.Random;
import java.util.Scanner;

/**
 * Created by HarryJohnson on 11/08/2016.
 */
public class GameBoard {
    private SudokuCell[][] gameMatrix;

    public GameBoard() {
        //constructor for the gameboard
        this.gameMatrix = new SudokuCell[9][9];
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                this.gameMatrix[x][y] = new SudokuCell();
            }
        }
    }

    public void PopulateGameBoard() {
        //populates the gameboard with numbers 1-9 randomly
        //iterates through the array until it reaches the end of the array
        //if it reaches deadlock it clears the board and tries again
        //method never returns an invalid board
        int deadLockCounter;
        Random numGen = new Random();
        int tmp;
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                this.gameMatrix[x][y].setNumber('#');
            }
        }
        deadLockCounter = 100;
        //iterates through the tiles
        for (int a = 0; a < 9; a += 3) {
            for (int b = 0; b < 9; b += 3) {
                //iterates through a single tile
                for (int x = 0 + a; x < 3 + a; x++) {
                    for (int y = 0 + b; y < 3 + b; y++) {
                        if (this.gameMatrix[x][y].getNumber() == '#') {
                            tmp = (numGen.nextInt(58 - 49) + 49);
                            while ((!tileValid(this.gameMatrix[x][y].getTile(), tmp)) || (!lineValid(x, y, tmp))) {
                                tmp = (numGen.nextInt(58 - 49) + 49);
                                deadLockCounter--;
                                if (deadLockCounter == 0) {
                                    //declares deadlock and backtracks to 0,0
                                    DeadLockBackTrack();
                                    a = 0;
                                    b = 0;
                                    x = 0;
                                    y = 0;
                                }
                            }
                            deadLockCounter = 100;
                            this.gameMatrix[x][y].setNumber((char) tmp);
                        }
                    }
                }
            }
        }
    }

    public void PrintGameBoard() {
        //prints the gameboard to the console in a readable format, separated by tiles
        System.out.println("\\ X  1 2 3  4 5 6  7 8 9\nY   ---------------------");
        for (int x = 0; x < 9; x++) {
            System.out.print(x+1 + "|   ");
            for (int y = 0; y < 9; y++) {
                System.out.print(this.gameMatrix[x][y].getNumber() + " ");
                if (y == 2 || y == 5) {
                    System.out.print("|");
                }
            }
            if (x == 2 || x == 5) {
                System.out.println("\n     ------+------+-----");
            } else {
                System.out.println();
            }
        }
    }

    public void AssignTiles() {
        //assigns a tile number to each SudokuCell in the array
        //these tile numbers are used to make gameboard validation and printing easier.
        int tileNum = 1;
        int xLowerBound = 0;
        int xUpperBound = 3;
        int yLowerBound = 0;
        int yUpperBound = 3;

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int a = xLowerBound; a < xUpperBound; a++) {
                    for (int b = yLowerBound; b < yUpperBound; b++) {
                        this.gameMatrix[b][a].setTile(tileNum);
                    }
                }
                xLowerBound = xUpperBound;
                xUpperBound += 3;
                tileNum++;
            }
            xLowerBound = 0;
            xUpperBound = 3;
            yLowerBound = yUpperBound;
            yUpperBound += 3;
        }
    }

    public void PrintTiles() {
        //prints the array to the console
        //prints the tile attribute, for debugging and development purposes
        //likely not used in finally product
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                System.out.print(this.gameMatrix[x][y].getTile() + " ");
                if (y == 2 || y == 5) {
                    System.out.print("|");
                }
            }
            if (x == 2 || x == 5) {
                System.out.println("\n------+------+-----");
            } else {
                System.out.println();
            }
        }
    }

    public boolean tileValid(int currentTile, int currentNumber) {
        //checks if the currentNumber parameter is the same as another number in the current tile
        //if there is a duplicate, tileValid returns false.
        boolean bool = true;
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (gameMatrix[x][y].getTile() == currentTile) {
                    if (gameMatrix[x][y].getNumber() == (char) currentNumber) {
                        bool = false;
                    }
                }
            }
        }
        return bool;
    }
    public boolean tileValidforBoardValid(int i, int j,int currentTile, int currentNumber) {
        //checks if the currentNumber parameter is the same as another number in the current tile
        //if there is a duplicate, tileValid returns false.
        boolean bool = true;
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if(x != i || y != j){
                    if((gameMatrix[x][y].getNumber() != ' ')){
                        if (gameMatrix[x][y].getTile() == currentTile) {
                            if (gameMatrix[x][y].getNumber() == (char) currentNumber) {
                                bool = false;
                            }
                        }
                    }
                }
            }
        }
        return bool;
    }

    public boolean lineValid(int i, int j, int currentNumber) {
        //checks if the currentNumber parameter is the same as another number in the same x and y axis as the currentNumber
        //if there is a duplicate, lineValid returns false.
        boolean bool = true;
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if((i != x) && (j != y)){
                    if((gameMatrix[x][y].getNumber() != ' ')&&(gameMatrix[i][j].getNumber() != ' ')){
                        if ((gameMatrix[x][j].getNumber() == (char) currentNumber) || (gameMatrix[i][y].getNumber() == (char) currentNumber)) {
                            bool = false;
                        }
                    }
                }
            }
        }
        return bool;
    }

    public void DeadLockBackTrack() {
        //backtracks if the deadlock condition is met
        //sets the array to only contain '#', which is my placeholder for nothing while still being visually printable
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                this.gameMatrix[x][y].setNumber('#');
            }
        }
    }

    public void HideCells(int difficulty) {
        //hides cells dependant on the difficulty
        Random numGen = new Random();
        int tmpX;
        int tmpY;
        for (int i = 0; i < ((10 * difficulty) + 20); i++) {
            do {
                tmpX = numGen.nextInt(9);
                tmpY = numGen.nextInt(9);
            } while (this.gameMatrix[tmpX][tmpY].getNumber() == ' ');
            this.gameMatrix[tmpX][tmpY].setNumber(' ');
            this.gameMatrix[tmpX][tmpY].setEditable(true);
        }

    }
    public void CopyMatrix(GameBoard gb) {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                this.gameMatrix[x][y].setNumber(gb.gameMatrix[x][y].getNumber());
            }
        }
    }
    public void SolveTile(GameBoard gb){
        System.out.println("Which tile would you like to solve?");
        System.out.println("Enter the X value of the Tile:");
        Scanner kb = new Scanner(System.in);
        int xInt = kb.nextInt();
        System.out.println("Enter the Y value of the Tile:");
        int yInt = kb.nextInt();
        System.out.println("What number would you like to apply to this Tile?");
        int input = kb.nextInt();
        if(gb.gameMatrix[yInt-1][xInt-1].isEditable()){
            gb.gameMatrix[yInt-1][xInt-1].setNumber((char)(input+48));
        }
    }
    public boolean ValidBoardInPlay() {
        boolean valid = true;
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++) {
                if ((!tileValidforBoardValid(i, j,this.gameMatrix[i][j].getTile(), this.gameMatrix[i][j].getNumber()))
                || (!lineValid(i, j, this.gameMatrix[i][j].getNumber()))){
                    valid = false;
                }
            }
        }
    return valid;
    }

    public boolean NoBlanks() {
        boolean bool = true;
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++) {
                if (this.gameMatrix[i][j].getNumber() == ' '){
                    bool = false;
                }
            }
        }
        return bool;
    }

    public boolean WinCheck() {
        boolean bool = false;
        if (ValidBoardInPlay()) {
            if(NoBlanks()){
                bool = true;
        }
    }
        return bool;
    }
}