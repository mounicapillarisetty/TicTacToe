import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Tic Tac Toe Game with GUI implementations.
 * 
 * @author Mounica Pillarisetty
 * @version December 2nd 2017
 */

public class TicTacToeGame implements ActionListener
{
    private JFrame window = new JFrame("THE TIC TAC TOE GAME!"); 
    
    private static final int SCREEN_WIDTH = 500; // WINDOW WIDTH
    private static final int SCREEN_HEIGHT = 500; // WINDOW HEIGHT 
    
    public static final String PLAYER_X = "X"; // player using "X"
    public static final String PLAYER_O = "O"; // player using "O"
    public static final String EMPTY = " ";  // empty cell
    public static final String TIE = "T"; // game ended in a tie
    
    private String player; // current player (PLAYER_X or PLAYER_O)
    private String winner; // winner: PLAYER_X, PLAYER_O, TIE, EMPTY = in progress
    private int numFreeSquares; // number of squares still free
    private JButton board[][]; // 3x3 array representing the board
    
    private JMenuItem newGame; // new game setting
    private JMenuItem quitGame; // quit game setting
    
    private JLabel currentText; // current state message
    
    private int winCounterX = 0; // number of X wins 
    private int winCounterO = 0; // number of O wins
    private int tieCounter = 0; // number of ties

    /**
     * Constructs a new Tic Tac Toe GUI board
     */

    public TicTacToeGame()
    {
        setGUI(); // setter for GUI things
        winner = EMPTY;
        numFreeSquares = 9;
        player = PLAYER_X;
    }

    /**
     * Method for reseting the game.
     */
    private void newGame() 
    {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j].setText(EMPTY);
                board[i][j].setEnabled(true);
                board[i][j].setIcon(new ImageIcon("blank.jpg"));
            }
            window.setResizable(false); 
        }
        currentText.setText("Game is in Progress: X's turn");
        winner = EMPTY;
        numFreeSquares = 9;
        player = PLAYER_X;
    }
//////////////////////////////////// RESET DOESNT WORK ////////////////////////////////////////////////////
    /**
     * Action Performed (from actionListener Interface).
     * (This method is executed when a button is selected.)
     *
     * @param the action event
     */

    public void actionPerformed(ActionEvent e) 
    { 
        if (e.getSource() instanceof JMenuItem) {
            JMenuItem select = (JMenuItem) e.getSource();
            if (select == newGame) { 
                newGame();
                //btn.setIcon(new ImageIcon("blank.jpg"));
                //btn.setDisabledIcon(new ImageIcon("blank.jpg"));
                return;
            }
            System.exit(0);  
        }
        if (e.getSource() instanceof JButton) {
            JButton btn = (JButton) e.getSource();
            btn.setText(player);
            if(player == PLAYER_X){
                btn.setIcon(new ImageIcon("x.jpg"));
                btn.setDisabledIcon(new ImageIcon("x.jpg"));
            }
            else {
                btn.setIcon(new ImageIcon("O.jpg"));
                btn.setDisabledIcon(new ImageIcon("O.jpg"));
            }
            btn.setEnabled(false);   // disable button (can't choose it now)
            numFreeSquares--;
            if (haveWinner(btn)) 
            {
                winner = player; // must be the player who just went
                //board[i][j].setIcon(new ImageIcon("blank.jpg"));
                board[0][0].setIcon(new ImageIcon("gameover.jpg"));
                btn.setDisabledIcon(new ImageIcon("gameover.jpg"));
            } else if(numFreeSquares==0) 
            {
                winner = TIE; // board is full so it's a tie 
                btn.setIcon(new ImageIcon("gameover.jpg"));
                btn.setDisabledIcon(new ImageIcon("gameover.jpg"));
            }
        }
       
        
        // if have winner stop the game
        if (winner!=EMPTY) {
            disableAll(); // disable all buttons
            // print winner
            String s = "Game over: ";
            if (winner == PLAYER_X) {
                s += "X wins!";
                winCounterX ++;
            } else if (winner == PLAYER_O) {
                s += "O wins!";
                winCounterO ++;
            } else if (winner == TIE) {
                s += "Tied game.";
                tieCounter ++;
            }   
            currentText.setText(s + ("      X has won: " + winCounterX +
                "       O has won: " + winCounterO + "      Tied Games: " 
                + tieCounter));
        } else {
            // change to other player (game continues)
            if (player == PLAYER_X) {
                player = PLAYER_O;
                currentText.setText("Game is in progress: O's turn");
            } else {
                player = PLAYER_X;
                currentText.setText("Game is in progress: X's turn");
            }
        }
    }

    /**
     * Returns true if filling the given square gives us a winner, and false
     * otherwise.
     *
     * @param Square just filled
     * 
     * @return true if we have a winner, false otherwise
     */
    private boolean haveWinner(JButton e) 
    {
        // unless at least 5 squares have been filled, we don't need to go any further
        // (the earliest we can have a winner is after player X's 3rd move).
        if (numFreeSquares > 4) {
            return false;
        }
            
        // find the square that was selected
        int row = 0 ;
        int column = 0;

        loop: // a label to allow us to break out of both loops
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (e == board[i][j]) { // object identity
                    row = i;
                    column = j;  //  row, col represent the chosen square
                    break loop; // break out of both loops
                }    
            }
        }

        // check row "row"
        if (board[row][0].getText().equals(board[row][1].getText()) && 
            board[row][0].getText().equals(board[row][2].getText())) {
            return true;
        }

        // check column "collumn"
        if (board[0][column].getText().equals(board[1][column].getText()) &&
            board[0][column].getText().equals(board[2][column].getText())) {
            return true;
        }

        // if row=col check one diagonal
        if (row == column) {
            if (board[0][0].getText().equals(board[1][1].getText()) &&
                board[0][0].getText().equals(board[2][2].getText())) {
                return true;
            }
        }

        // if row=2-col check other diagonal
        if (row == (2-column)) {
            if (board[0][2].getText().equals(board[1][1].getText()) &&
                board[0][2].getText().equals(board[2][0].getText())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Disables all buttons when the game is over
     */
    private void disableAll() {
        if (numFreeSquares == 0) {
            return; // nothing to do
        }
        int i, j;
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                board[i][j].setEnabled(false);
            }
        }
    }

    /**
     * Set up the GUI
     *
     */
    private void setGUI() {

        // for control keys
        final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(); 

        window.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set up the menu bar and menu
        JMenuBar menubar = new JMenuBar();
        window.setJMenuBar(menubar); // add menu bar to our frame

        JMenu fileMenu = new JMenu("Game"); // create a menu called "Game"
        menubar.add(fileMenu); // and add to our menu bar

        newGame = new JMenuItem("New"); // create a menu item called "Reset"
        fileMenu.add(newGame); // and add to our menu (can also use ctrl-R:)
        newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SHORTCUT_MASK));
        newGame.addActionListener(this); 

        quitGame = new JMenuItem("Quit"); // create a menu item called "Quit"
        fileMenu.add(quitGame); // and add to our menu (can also use ctrl-Q:)
        quitGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
        quitGame.addActionListener(this);

        window.getContentPane().setLayout(new BorderLayout()); // default so not required

        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(3, 3));
        window.getContentPane().add(gamePanel, BorderLayout.CENTER);

        currentText = new JLabel("Game is in Progress: X's turn");
        window.getContentPane().add(currentText, BorderLayout.SOUTH);

        // create JButtons, add to window, and action listener
        board = new JButton[3][3];
        //Font font = new Font("Dialog", Font.BOLD, 24);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = new JButton(EMPTY);
                //board[i][j].setFont(font);
                gamePanel.add(board[i][j]);
                board[i][j].addActionListener(this);
            }
        }
        window.setVisible(true);
    }
}
