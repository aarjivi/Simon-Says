import javax.swing.*; // to create jframe and jbuttons
import java.awt.*; // to create layout and colors
import java.awt.event.ActionEvent; // allows to click a button; lets us know when its cicked
import java.awt.event.ActionListener; // listens to user; helps makes buttons that can be clicked
import java.util.ArrayList; // creates array that can be altered
import java.util.Random;

public class Main extends JFrame implements ActionListener {

    private final JButton[] colorButtons = new JButton[4]; // this is hwo to write an array; creates buttons
    private final String[] colors = {"Red", "Green", "Blue", "Yellow"};
    private final ArrayList<Integer> sequence = new ArrayList<>(); // this is an array list
    private final ArrayList<Integer> playerSequence = new ArrayList<>();
    private int currentStep = 0;
    private boolean playerTurn = false;

    public Main() { // constructor 
        setTitle("Simon Says");
        setLayout(new GridLayout(2, 2));
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close the application when the window is closed

        for (int i = 0; i < 4; i++) {
            colorButtons[i] = new JButton(colors[i]); // create a new button with color name
            colorButtons[i].setBackground(getColorByName(colors[i])); // set the button's background color
            colorButtons[i].addActionListener(this); // adds action listener to button; "this" initializes it
            add(colorButtons[i]); // add the button to the window
        } 

        generateSequence(); // method we created to generate the initial sequence
        displaySequence(); // display the initial sequence to the player
    }

    private Color getColorByName(String colorName) {
        return switch (colorName) {
                // return the color object based on the color name
            case "Red" -> Color.RED;
            case "Green" -> Color.GREEN;
            case "Blue" -> Color.BLUE;
            case "Yellow" -> Color.YELLOW;
            default -> Color.BLACK;
        };
    }

    private void generateSequence() {
        Random rand = new Random(); // creates a Random object
        sequence.add(rand.nextInt(4)); // adds a random integer to the sequence
    }

    private void displaySequence() {
        playerTurn = false;
        playerSequence.clear(); // clear the player input's sequence
        new Timer(1000, new ActionListener() {
            int index = 0; // index to track the current step in the sequence

            @Override
            public void actionPerformed(ActionEvent e) {
                if (index < sequence.size()) { // if the current index is within the sequence length
                    int colorIndex = sequence.get(index); // get the color index from the sequence
                    colorButtons[colorIndex].setBackground(Color.WHITE); // sets the buttonto white tp odnicate it 
                    Timer pause = new Timer(500, e1 -> colorButtons[colorIndex].setBackground(getColorByName(colors[colorIndex]))); //creates a pause timer to reset the button color
                    pause.setRepeats(false); // set the pause timer to not eepeat
                    pause.start(); // starts the pause timer
                    index++; // move to the next step
                } else {
                    ((Timer) e.getSource()).stop(); // stop the timer if the sequenve is finished 
                    playerTurn = true; // set the flag to indicate it's the player;s turn
                }
            }
        }).start(); // start the timer to display the sequence 
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (playerTurn) { // if it's the player's turn
            JButton clickedButton = (JButton) e.getSource(); // get the button that was clicked
            int clickedIndex =-1; // variable to store the index of the clicked button; the value -1 is default,, a sentinel value used to indicate that no valud button index has been identified yet
            for (int i=0; i<4; i++){ // loop through the buttons to find the clicked one
                if (colorButtons[i] == clickedButton) { // if the button matches
                    clickedIndex = i; // set the clicked index
                    break; //exit the loop
                }
            }

            playerSequence.add(clickedIndex); // add the clicked index to the player's sequence
            if (playerSequence.get(currentStep) != sequence.get(currentStep)) { //if the player's input is incorrect
                JOptionPane.showMessageDialog(this, "Game over! You reached step " + currentStep); // show game over message 
                System.exit(0); //exit the app
            } else {
                currentStep ++; // move to the next step
                if (currentStep == sequence.size()){ //if the player has completed the sequence
                    currentStep = 0; // reset current step
                    generateSequence(); // generate a new step in the sequence
                    displaySequence(); // display the updated sequence
                }
             }
                
        }
    }
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> { // schedule the creation of the GUI on the event dispatching thread
            Main game = new Main(); // create a new instance of the game
            game.setVisible(true); // make the game window visible
        });
    }
}
    