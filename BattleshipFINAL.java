package src.com.company;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Battleship {

    static final int CARRIER = 5;
    static final int BATTLESHIP = 4;
    static final int DESTROYER = 3;
    static final int SUBMARINE = 3;
    static final int PATROL = 2;

    static final int UP = 0;
    static final int DOWN = 1;
    static final int RIGHT = 2;
    static final int LEFT = 3;

    //board setup
    static char[][] board = new char[11][11];
    static char[][] newBoard = new char[11][11];
    static int[] header = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    static int[] ships = new int[]{PATROL, SUBMARINE, DESTROYER, BATTLESHIP, CARRIER};

    //game controls
    static int hitsToPatrol = 0;
    static int hitsToSubmarine = 0;
    static int hitsToDestroyer = 0;
    static int hitsToBattleship = 0;
    static int hitsToCarrier = 0;
    static int shipsPlaced = 0;
    static int totalShipsSunk = 0;
    static boolean sunk = false;
    static boolean replay = true;
    static boolean qPressed = false;

    public static void main(String[] args) throws InterruptedException {
        while (replay) {
            gameStart();
            while (totalShipsSunk < ships.length) {
                runGuess();
                //if 'Q' was pressed
                if (qPressed) {
                    continue;
                }
                printBoard(newBoard);
            }
            gameEnd();
        }
    }//end of main

    public static void gameStart() throws InterruptedException {
        //reset game controls
        shipsPlaced = 0;
        totalShipsSunk = 0;
        qPressed = false;

        //initialize the game board(which will be invisible to the player) and set the ships.
        resetBoard(board);
        placeShips(ships);

        //initialize the board that the player will see and use to keep track of his hits and misses
        resetBoard(newBoard);

        System.out.println("    Welcome to Battleship! \nPress 'Q' at any time to quit.");
        System.out.println("\n\n\n\n\n\n\n\n\n\n");
        TimeUnit.SECONDS.sleep(2);
        System.out.print("Filling the board");
        for (int i = 0; i < 6; i++) {
            TimeUnit.MILLISECONDS.sleep(250);
            System.out.print(". ");
        }
        System.out.println("\n\n\n\n");
        printBoard(newBoard);
        System.out.println("Please type your guess in the following format: (example) 'D5'");
    }//end of gameStart

    public static void resetBoard(char[][] board) {
        char letter = 'A';
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < (board.length); j++) {
                if (i == 10) {
                    board[i][j] = ' ';
                } else if (j == 0) {
                    board[i][j] = letter;
                    letter++;
                } else {
                    board[i][j] = '*';
                }
            }
        }
    }//end of resetBoard

    public static void printBoard(char[][] board) {
        //print amount of ships sunk
        System.out.println("Ships Sunk: " + totalShipsSunk);
        //print the header
        System.out.print("    ");
        for (int i = 0; i < header.length; i++) {
            System.out.print(header[i] + "  ");
        }
        System.out.println();
        //print the rest of the board
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                System.out.print(" " + board[i][j] + " ");
            }
            System.out.println();
        }
    }//end of printBoard

    public static void placeShips(int[] ships) {
        while (shipsPlaced != ships.length) {
            for (int i = 0; i < ships.length; i++) {
                //get the x,y coordinates, direction and the correct letter to be placing for each ship
                //temp non randoms for testing
//                int x = i + 1;
//                int y = 0;
//                int direction = 1;

                int x = (int) ((Math.random() * 10) + 1);
                int y = (int) ((Math.random() * 10) + 1);
                int direction = (int) (Math.random() * 4);
                char letter = (getShipLetter(i));

                shipSpot(letter, ships[i], x, y, direction);
            }
        }//end of making sure all ships were properly placed
    }///end of placeShips

    public static char getShipLetter(int shipIndex) {
        char shipLetter = ' ';
        switch (shipIndex) {
            case 0:
                shipLetter = 'P';
                break;
            case 1:
                shipLetter = 'S';
                break;
            case 2:
                shipLetter = 'D';
                break;
            case 3:
                shipLetter = 'B';
                break;
            case 4:
                shipLetter = 'C';
                break;
        }
        return shipLetter;
    }//end of getShipLetter

    public static int shipSpot(char letter, int ship, int x, int y, int direction) {
        int shipSpace = ship;
        int xAxis = x;
        int yAxis = y;

        while (shipSpace > 0) {
            //check if the spot is empty
            if (board[yAxis][xAxis] != '*') {
                errorMessage();
                return -1;
            }
            if (direction == UP) {
                //look for bad placements
                if ((y - ship) < 0) {
                    errorMessage();
                    return -1;
                }
                //if no bad placements are found, proceed
                else {
                    board[yAxis][xAxis] = letter;
                    shipSpace--;
                    yAxis--;
                }
            }//end of if UP
            else if (direction == DOWN) {
                if ((y + ship) > 10) {
                    errorMessage();
                    return -1;
                } else {
                    board[yAxis][xAxis] = letter;
                    shipSpace--;
                    yAxis++;
                }
            }//end of if DOWN
            else if (direction == RIGHT) {
                if ((x + ship) > 10) {
                    errorMessage();
                    return -1;
                } else {
                    board[yAxis][xAxis] = letter;
                    shipSpace--;
                    xAxis++;
                }
            }//end of if RIGHT
            else if (direction == LEFT) {
                if ((x - ship) < 0) {
                    errorMessage();
                    return -1;
                } else {
                    board[yAxis][xAxis] = letter;
                    shipSpace--;
                    xAxis--;
                }
            }//end of if LEFT
            //check if the boat was successfully placed
            if (shipSpace == 0) {
                shipsPlaced++;
            }
        }//end of while shipSpace > 0
        return 1;
    }//end of shipSpot

    public static void errorMessage() {
        resetBoard(board);
        shipsPlaced = 0;
    }//end of errorMessage

    public static void runGuess() throws InterruptedException {

        /*  ask the user for a string in the form of a letter between 'A' and 'J'
        inclusively, followed by a number between 1 and 10 inclusively. */
        System.out.print("Guess: ");
        Scanner scan = new Scanner(System.in);
        String guess = scan.next();

        //parse through the string to get the 'Y' and 'X' values respectively
        guess = guess.toUpperCase();

        //check for game quit
        if (guess.charAt(0) == 'Q') {
            totalShipsSunk = 101;
            qPressed = true;
            return;
        }
        //check for errors
        if (guess.charAt(0) < 'A' || guess.charAt(0) > 'J' || guess.length() < 2 || guess.length() > 3
                || guess.charAt(1) < '1' || guess.charAt(1) > '9') {
            System.out.println("Incorrect input. Please try again.");
            return;
        }
        if (guess.length() == 3) {
            if (guess.charAt(2) != '0') {
                System.out.println(guess.charAt(2));
                System.out.println("Incorrect input. Please try again.");
                return;
            }
        }

        //get 'Y' guess
        char letterGuess = guess.charAt(0);
        int yGuess = getYGuess(letterGuess);
        //get 'X' guess
        int xGuess = -1;

        if (guess.length() == 3) {
            xGuess = 10;
        } else {
            xGuess = guess.charAt(1) - '0';
        }

        //if the spot was already guessed
        if (newBoard[yGuess][xGuess] == 'O' || newBoard[yGuess][xGuess] == 'X') {
            System.out.println("Already guessed that spot. Please try again. \n");
            return;
        }

        //check if the guess was a hit or a miss

        //pause for affect
        for (int i = 0; i < 4; i++) {
            System.out.print(". ");
            TimeUnit.MILLISECONDS.sleep(500);
        }
        System.out.println("\n\n\n\n");
        //for a hit
        if (board[yGuess][xGuess] != '*') {
            char temp = board[yGuess][xGuess];
            board[yGuess][xGuess] = 'X';
            newBoard[yGuess][xGuess] = 'X';
            System.out.println("HIT!!!\n\n\n");
            TimeUnit.SECONDS.sleep(1);
            //store the type of boat hit to check if it was sunk
            if (shipSunk(temp)) {
                totalShipsSunk++;
                sunk = false;
            }
        }
        //for a miss
        else {
            newBoard[yGuess][xGuess] = 'O';
            System.out.println("MISS. Try again.\n\n\n");
            TimeUnit.SECONDS.sleep(1);

        }
    }//end of runGuess

    public static int getYGuess(char letterGuess) {
        switch (letterGuess) {
            case 'A':
                return 0;
            case 'B':
                return 1;
            case 'C':
                return 2;
            case 'D':
                return 3;
            case 'E':
                return 4;
            case 'F':
                return 5;
            case 'G':
                return 6;
            case 'H':
                return 7;
            case 'I':
                return 8;
            case 'J':
                return 9;
            default:
                return -1;
        }
    }//end of getYGuess

    public static boolean shipSunk(char shipLetter) throws InterruptedException {

        int boat;
        switch (shipLetter) {
            case 'P':
                boat = PATROL;
                hitsToPatrol++;
                if (hitsToPatrol == boat) {
                    System.out.println("Patrol boat sunk!\n\n\n");
                    TimeUnit.SECONDS.sleep(1);
                    sunk = true;
                    return sunk;
                }
                break;
            case 'S':
                boat = SUBMARINE;
                hitsToSubmarine++;
                if (hitsToSubmarine == boat) {
                    System.out.println("Submarine sunk!\n\n\n");
                    TimeUnit.SECONDS.sleep(1);
                    sunk = true;
                    return sunk;
                }
                break;
            case 'D':
                boat = DESTROYER;
                hitsToDestroyer++;
                if (hitsToDestroyer == boat) {
                    System.out.println("Destroyer sunk!\n\n\n");
                    TimeUnit.SECONDS.sleep(1);
                    sunk = true;
                    return sunk;
                }
                break;
            case 'B':
                boat = BATTLESHIP;
                hitsToBattleship++;
                if (hitsToBattleship == boat) {
                    System.out.println("Battleship sunk!\n\n\n");
                    TimeUnit.SECONDS.sleep(1);
                    sunk = true;
                    return sunk;
                }
                break;
            case 'C':
                boat = CARRIER;
                hitsToCarrier++;
                if (hitsToCarrier == boat) {
                    System.out.println("Aircraft Carrier sunk!\n\n\n");
                    TimeUnit.SECONDS.sleep(1);
                    sunk = true;
                    return sunk;
                }
                break;
            default:
                boat = 0;
        }
        //else
        return false;
    }//end of shipSunk

    public static boolean gameEnd() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        if (totalShipsSunk == 5) {
            System.out.println("YOU WON! THANK YOU FOR PLAYING!");
        } else {
            System.out.println("THANK YOU FOR PLAYING!\n");
        }
        TimeUnit.SECONDS.sleep(2);
        System.out.println("Would you like to try another game? (type 'Yes' or 'No' and press Enter)");
        Scanner scan = new Scanner(System.in);
        String replayAnswer = scan.next();
        replayAnswer = replayAnswer.toUpperCase();
        if (replayAnswer.charAt(0) == 'Y') {
            //reset game controls
            //System.out.println("\n\n\n\n\n\n\n\n");
            totalShipsSunk = 0;
            replay = true;
            return replay;
        } else if (replayAnswer.charAt(0) == 'N') {
            System.out.println("Ok! Have a great day!");
            TimeUnit.SECONDS.sleep(2);
            System.out.println("\n   BATTLESHIP IN JAVA © 2020 Dovid Rosenberg");
            TimeUnit.SECONDS.sleep(2);
            replay = false;
            return replay;
        } else {
            System.out.println("Invalid input.");
            gameEnd();
            return true;
        }
    }//end of gameEnd

}//end of class BattleshipTwo
