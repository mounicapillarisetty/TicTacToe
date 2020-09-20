import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.FlowLayout;
import java.net.URL;


/**
 * Tic Tac Toe Game with GUI implementation.
 * 
 * Some code is adapted from Prof. Lynn Marshall's examples
 * 
 * @author Mounica Pillarisetty
 * @version Dec 02, 2017
 */

public class TicTacToeGame implements ActionListener
{
    private JFrame window = new JFrame("TIC TAC TOE GAME!"); 

    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 600;
    private static final int TEXT_WIDTH = 60;

    private static final String PLAYER_X = "X"; // player using "X"
    private static final String PLAYER_O = "O"; // player using "O"
    private static final String EMPTY = " ";  // empty cell
    private static final String TIE = "T"; // game ended in a tie

    private String player;   // current player (PLAYER_X or PLAYER_O)
    private String winner;   // winner: PLAYER_X, PLAYER_O, TIE, EMPTY = in progress
    private int numFreeSquares; // number of squares still free
    private JButton board[][]; // 3x3 array of JButtons

    private JMenuItem newGame; // reset board
    private JMenuItem quitGame; // quit

    private JLabel currentText; // current message
    
    //BONUS
    private int winCounterX = 0; // number of X wins 
    private int winCounterO = 0; // number of O wins
    private int tieCounter = 0; // number of ties
    private AudioClip click1, click2;

    /**
     * Constructor for a new Tic Tac Toe board
     */

    public TicTacToeGame() {
        setGUI(); // setter for GUI things
        winner = EMPTY;
        numFreeSquares = 9;
        player = PLAYER_X;
        window.setResizable(false); 
    }

    /**
     * Setting up the GUI
     */
    private void setGUI() {
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Font font = new Font("AR HERMANN", Font.BOLD, 95 );

        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(3, 3));
        window.getContentPane().add(gamePanel, BorderLayout.CENTER);
        JMenuBar menubar = new JMenuBar();
        window.setJMenuBar(menubar); 
        
        JMenu fileMenu = new JMenu("Game"); 
        newGame = new JMenuItem("New"); 
        quitGame = new JMenuItem("Quit"); 
      
        // this allows us to use shortcuts (e.g. Ctrl-R and Ctrl-Q)
        final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(); // to save typing
        newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SHORTCUT_MASK));
        quitGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));

        currentText = new JLabel("Game is in Progress: X's turn");
        window.getContentPane().add(currentText, BorderLayout.SOUTH);
        
        quitGame.addActionListener(this);
        newGame.addActionListener(this); 
        menubar.add(fileMenu); 
        fileMenu.add(newGame); 
        fileMenu.add(quitGame);
        
        board = new JButton[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = new JButton(EMPTY);
                board[i][j].setFont(font);
                board[i][j].addActionListener(this);
                gamePanel.add(board[i][j]);
            }
        }
        window.setVisible(true);
    }

    /**
     * Creating a new game
     */
    private void newGame() 
    {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j].setText(EMPTY);
                board[i][j].setEnabled(true);
            }
        }
        currentText.setText("Game is in Progress: X's turn");
        winner = EMPTY;
        numFreeSquares = 9;
        player = PLAYER_X;
    }

    /**
     * Action Performed (from actionListener Interface).
     * This method is executed when a button is selected on the board
     *
     * @param e      an action event
     */

    public void actionPerformed(ActionEvent e)
    {
        
        if (e.getSource() instanceof JMenuItem) {
            JMenuItem selectItem = (JMenuItem) e.getSource();
            if (selectItem == newGame) {
                newGame();
                return;
            }
            System.exit(0);  
        }
        
        JButton choseButton = (JButton) e.getSource();  
        choseButton.setText(player);     
        choseButton.setEnabled(false);   
        numFreeSquares--;
        
         if (winnerPlayer(choseButton)) {
            winner = player; 
        }
        else if (numFreeSquares == 0){
            winner = TIE; 
        }
        
        if (winner != EMPTY) {
            // print winner
            String output = " ";
            if (winner == PLAYER_O) {
                output = output + "Game over: O won!";
                winCounterO ++;
                click1.play();
            } else if (winner == PLAYER_X) {
                output =output + "Game over: X won!";
                winCounterX ++;
                click2.play();
            } else if (winner == TIE) {
                output = output + "Game over: Tied game!";
                tieCounter ++;
                if (player == PLAYER_O){
                    click1.play();
                }
                else {
                    click2.play();
                }
            } 
            currentText.setText(output + ("      X has won: " + winCounterX +
                "       O has won: " + winCounterO + "      Tied Games: " 
                + tieCounter));
            //disabling the buttons
            if (numFreeSquares == 0){
                return; 
            }
            int i;
            int j;
            for (i = 0; i < 3; i++) {
                for (j = 0; j < 3; j++) {
                    board[i][j].setEnabled(false);
                }
            }
        } else {
            if (player == PLAYER_O) {
                player = PLAYER_X;
                currentText.setText("Game is in progress: X's turn");
                URL urlClick1 = TicTacToeGame.class.getResource("o.wav");
                click1 = Applet.newAudioClip(urlClick1);
                click1.play();
            } else {
                player = PLAYER_O;
                currentText.setText("Game is in progress: O's turn");
                URL urlClick2 = TicTacToeGame.class.getResource("x.wav");
                click2 = Applet.newAudioClip(urlClick2);
                click2.play();
            }
        }
    }

    /**
     * This is a method to declare a winner.
     * Returns true if the last played square makes a winner
     * and false otherwise.
     *
     * @param e         a square has been taken
     * @return true     if we have a winner, false otherwise
     */
    private boolean winnerPlayer(JButton e) 
    {
        int row = 0;
        int col = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (e == board[i][j])  { 
                    row = i;
                    col = j;
                    break;
                } 
            }
        }

        // checks column
        if (board[0][col].getText().equals(board[1][col].getText()) &&
            board[0][col].getText().equals(board[2][col].getText())){
            return true;
        }
        // checks row
        else if (board[row][0].getText().equals(board[row][1].getText()) && 
            board[row][0].getText().equals(board[row][2].getText())) {
            return true;
        }
        // checks diagonal from 0 0 and down
        else if (row == col){
            if (board[0][0].getText().equals(board[1][1].getText()) &&
                board[0][0].getText().equals(board[2][2].getText())) {
                return true;
            }
        }
        // checks diagonal from 0 2 and down
        else if (row == (2-col)){
            if (board[0][2].getText().equals(board[1][1].getText()) &&
                board[0][2].getText().equals(board[2][0].getText())){
                return true;
            }
        }
        return false;
    }

}
