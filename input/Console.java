package input;
import java.util.ArrayList;
import universe.SmartGridUniverse;
import input.ops.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**A command line console that is available to the user as an alternative to the GUI
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class Console extends JPanel implements IInput, ActionListener, KeyListener {

    private static final int 
            /**the width of the console on the screen*/
            W = 700,
            /**the height of the console on the screen*/
            H = 600,
            /**the number of lines of text to display on the consoles*/
            LINE_COUNT = 500;
    /**the dimensions that the console takes up on a display device*/
    private Dimension FRAME_SIZE = new Dimension(W,H);

    public static final int 
            /**the opcode referenced in a user input sequence*/
            OP = 0,
            /**optional argument 1 for the opcode referenced in a user input sequence*/
            ARG1 = 1,
            /**optional argument 2 for the opcode referenced in a user input sequence*/
            ARG2 = 2,
            /**optional argument 3 for the opcode referenced in a user input sequence*/
            ARG3 = 3,
            /**optional argument 4 for the opcode referenced in a user input sequence*/
            ARG4 = 4,
            /**how many characters to display on each line of the console before wrapping to the next*/
            CHARS_PER_LINE = (int)(W * 0.185);

    /**a handle to the Smart Grid Universe object*/
    private SmartGridUniverse universe;

    /**a container for a user input sequence entered on the console, that is then parsed*/
    private String[] input;
    /**the console objects that will carry out specific commands*/
    private ArrayList<ConsoleOp> ops;
    /**characteristic of the Singleton pattern to ensure only one Console instance is ever created*/
    private static Console instance;
    /**the frame for the window*/
    private JFrame frame;
    /**the textbox where the user enters the command*/
    private JTextField command;
    /**the messages that are printed to the console*/
    private String[] messages = new String[LINE_COUNT];
    /**the list used to display all of the console messages*/
    private JList list;

    private Console() {
        // assign the Smart Grid Universe handle
        universe = SmartGridUniverse.getInstance();
        // give the console window a title
        frame = new JFrame("Console");
        // build the console operation objects that process the commands
        buildOpsList();

        // initialise the messages array so that the correct amount of space is allocated in the JList
        for(int x = 0; x < messages.length; x++) messages[x] = " ";

        // show the manual to the user upon load
        showManual();
        // write a new line separator
        writeToConsole("#");
    }

    /**Adds a scroll bar to the console output list
     *
     * @param list the list that should have a scroll bar attached to it
     * @param scroll the scroll bar to attach to the list
     */
    private void setScroll(JList list, JScrollPane scroll){
        // initialize the scroll pane
        scroll = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        scroll.getViewport().add(list);
        scroll.setBounds(0,0,W - 5,H-50);
        scroll.setVisible(true);

        // make sure we are always shown the last line the messages list
       list.ensureIndexIsVisible(LINE_COUNT -1);

       // add the scroll pane that contains the list
        frame.getContentPane().add(scroll);

    }

    /**Used to initialize the console upon first load
     *
     */
    private void setUpConsole(){
      
        Container cp = frame.getContentPane();
        cp.setLayout(null);
        command  = new JTextField(50);

        // set position
        command.setBounds(0,H-50,W,20);
        
        // set colors
        command.setBackground(Color.BLACK);
        command.setForeground(Color.YELLOW);
        
        // set misc. properties
        command.setEditable(true);
        command.requestFocusInWindow();
        command.addActionListener(this);

        // create the console list
        list = new JList(messages);
        list.setBackground(Color.BLACK);
        list.setForeground(Color.GREEN);
        list.addKeyListener(this);
        // assign a scroll bar to the list
        setScroll(list, new JScrollPane());

        // add the command input text box to the console
        cp.add(command);

        // misc. frame set up code
        frame.setPreferredSize(FRAME_SIZE);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        // To avoid text field command breaking we need to cast source of e!
        JTextField evt = (JTextField)e.getSource();
        String s = evt.getText();//command.getText();
        // show the user the input sequence they entered
        writeToConsole("INPUT : " + s);
        // attempt to parse the input sequence and perform the associated command
        parseInput(s);
        writeToConsole("#");
        // update the messages on display to the user
        list.setListData(messages);
        // clear the command input box
        evt.setText("");
        // Always make sure bottom line is showing
        list.ensureIndexIsVisible(LINE_COUNT-1);
    }

    public void keyReleased(KeyEvent e){}
    public void keyPressed(KeyEvent e){ list.ensureIndexIsVisible(list.getSelectedIndex());}
    public void keyTyped(KeyEvent e){ }

    /**Used to output messages to the console for display to the user
     *
     * @param s the message to display to the user
     */
    public final void writeToConsole(String s){
        // the number of strings to output to the console
        String[] insert = null;
        int len = s.length();
        // if the length of the output string is greater than number of characters
        // we allow per line we need to wrap it round
        if(len >= CHARS_PER_LINE){
            // maths to work out how many lines we need to wrap the string on to
            float y = (float)len / (float)CHARS_PER_LINE;
            int y1 = (int)y;
            int count = y1;
            if((y - y1) > 0) ++count;

            // create enough memory to store the now-split string
            insert = new String[count];

            // write each part of the string to its corresponding index
            for(int p = 0; p < insert.length; p++){
                int start = p * CHARS_PER_LINE, end = CHARS_PER_LINE * (p+1);
                String sub = null;
                
                // gradually move along the original string extracting each required part
                // upon every iteration
                if(end > s.length()){
                    sub = s.substring(start);
                }
                else sub = s.substring(start, end);
                // add the string to the array
                insert[p] = sub;
            }
        }
        // otherwise we do not need to split the string
        else insert = new String[]{s};

        // for all the strings to output
        for(int index = 0; index < insert.length; index++){
            // move the currently displayed messages back a position
            // to make space for the new messages
            for(int x = 0; x < messages.length - 1; x++){
                messages[x] = messages[x+1];
            }
            // insert the new message to be displayed
            messages[messages.length-1] = insert[index];
        }
    }

    
    /**Ensures only one Console object is created in the life time of the system
     *
     * @return always the same Console object, used across the system
     */
    public static Console getInstance() {
        // instantiate the static instance if needs be
        if (instance == null) {
            instance = new Console();
        }
      
        return instance;
    }

    /**Build the list of all possible commands that can be executed from
     * the console
     */
    private void buildOpsList() {
        ops = new ArrayList<ConsoleOp>();

        ops.add(new _opPause());
        ops.add(new _opGo());
        //ops.add(new _opReset());
        ops.add(new _opExit());
        ops.add(new _opGui());
        ops.add(new _opUni());
        ops.add(new _opList());
        ops.add(new _opAdd());
        ops.add(new _opRemove());
        ops.add(new _opManual());
    }

    public void open(){
      setUpConsole();
      buildOpsList();
      if(universe.isPaused()){
            writeToConsole("ENTER 'go' TO START THE SIMULATION!");
            writeToConsole("#");
      }
    }

    public void close() {
        frame.dispose();
    }

    /**Get the last input sequence entered by the user
     *
     * @return the last input sequence entered by the user
     */
    public String[] getInput() {
        return input;
    }

    /**Parses the input sequence entered by the user on the console,
     * and executes the corresponding operation
     *
     * @param in the input sequence entered by the user on the console
     */
    private void parseInput(String in) {
            // split the input up in to parsable chunks
            input = in.split(" ");

            try {
                // a flag to denot successful execution of a corresponding operation
                boolean opExec = false;
                // look through the list of operations to try and find a corresponding operation
                for (ConsoleOp op : ops) {
                    // use the identifier of a command to see whether it matches the opcode input
                    if (op.getIdentifier().equalsIgnoreCase(input[OP])) {
                            // execute the operation
                            op.operation();
                            // set the flag
                            opExec = true;
                        break;
                   }
                }
                if (!opExec) {
                    // if we havent found a corresponding operation for the input, display an error
                    showError("No valid command matches the command you have entered."); 
                }

            } catch (Exception e) {
                // if an error occurs during execution of a command tell the user
                showError("Malformed input. Please check syntax and try again");
            }
    }

    public void update() {
        if(list!=null) list.ensureIndexIsVisible(list.getSelectedIndex());
        frame.repaint();
    }

    /**Displays the operation manual to the user via the console,
     * detailing all the commands and their usages
     */
    public final void showManual() {
        writeToConsole("SYSTEM COMMANDS, AND THEIR USAGES:");

        for(ConsoleOp op : ops){

           for(String s : op.getSyntax()){
                writeToConsole(s);
           }
        }


    }

    /**Display an error message to the console
     *
     * @param error the error message to display
     */
    public void showError(String error) {
       writeToConsole("ERROR : " + error);
    }
}
