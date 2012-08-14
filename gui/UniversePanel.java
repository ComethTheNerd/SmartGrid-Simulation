package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import universe.SmartGridUniverse;
import utils.MessageStream;
import universe.ThreadSafeControl;
import universe.UserInput;
import aggregator.Aggregator;
/**The panel ever-present at the top of the screen that displays
 * information about the current state of the simulation universe
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class UniversePanel extends ClickablePanel{
    /**the font to use for drawing the universe clock*/
    Font clockFont = new Font("Clock Font",Font.BOLD,18);
    /**the x co-ordinate of where to draw the clock*/
    private final int clockX = 7;
    
    /**exit the simulation button*/
    private JButton exit;
    /**pause or unpause the simulation button*/
    private JButton startPause;
    /**reset the simulation button*/
    private JButton reset;
    /**add aggregator to the simulation button*/
    private JButton addAgg;
    /**switch to the console view button*/
    private JButton switchToConsole;
    /**the button panel on which all the buttons are mounted*/
    JPanel buttonPanel;
    /**a handle to the Smart Grid Universe single instance*/
    SmartGridUniverse universe;
    /**the parent of this panel*/
    private View parent;

    /**
     * Creates a new UniversePanel with the supplied parent view,
     * dimension and background colour.
     *
     * @param v parent view of this UniversePanel
     * @param dim dimension of this UniversePanel
     * @param bg background colour of this UniversePanel
     */
    public UniversePanel(View v, Dimension dim, Color bg){
        super(dim, bg);
        parent = v;
        universe= SmartGridUniverse.getInstance();
        this.setLayout(null);
        // wire up the buttons
        setUpButtons();
    }

    /**Initializes and wires up all of the buttons
     *
     */
    private void setUpButtons(){
        // set up the Exit button
      exit = new JButton("EXIT");
      exit.setToolTipText("Exit the simulation");
        exit.addActionListener(this);
        this.add(exit);
        exit.setBounds(View.Width - View.BUTTON_W,0,View.BUTTON_W,View.BUTTON_H);
/*
         reset = new JButton("RESET");
         reset.setToolTipText("Reset the simulation");
        reset.addActionListener(this);
        this.add(reset);
        reset.setBounds(View.Width - (2*View.BUTTON_W),0,View.BUTTON_W,View.BUTTON_H);
*/
        // set up the Pause button
        startPause = new JButton("GO");
        startPause.setToolTipText("Start / Stop the simulation");
        startPause.addActionListener(this);
        this.add(startPause);
        startPause.setBounds(View.Width - 2*View.BUTTON_W,0,View.BUTTON_W,View.BUTTON_H);

        // set up the Switch To Command Line button
        switchToConsole = new JButton("CMD");
        switchToConsole.setToolTipText("Switch to Command Line view");
        switchToConsole.addActionListener(this);
        this.add(switchToConsole);
        switchToConsole.setBounds(View.Width - 3*View.BUTTON_W,0,View.BUTTON_W,View.BUTTON_H);

        // set up the Add Aggregator button
        addAgg = new JButton("+A");
        addAgg.addActionListener(this);
        addAgg.setToolTipText("Add Aggregator to simulation");
        this.add(addAgg);
        addAgg.setBounds(View.Width - 4*View.BUTTON_W,0,View.BUTTON_W,View.BUTTON_H);
    }

    public void actionPerformed(ActionEvent event){
        JButton source = (JButton)event.getSource();
        // if the exit button was pressed
        if(source == exit){
            // create a request to asynchronously exit the simulation
            ThreadSafeControl.exitSimulation();
        }
        // if the reset button was pressed
        else if(source == reset){
            // create a request to asynchronously reset the simulation
            ThreadSafeControl.resetSimulation();
        }
        // if the start/pause button was pressed
        else if(source == startPause){
            // get the current pause state of the simulation
            boolean isPaused = universe.isPaused();
            // update the dual text on the button - it has shared functionality, pause/unpause
            if(isPaused) startPause.setText("STOP");
            else startPause.setText("GO");
            // switch the pause state
            ThreadSafeControl.setPause(!isPaused);

        }
        // if the add aggregator button was pressed
        else if(source == addAgg){
            // create a request to asynchronously add an aggregator to the simulation
            ThreadSafeControl.addAggregator();
           
        }
        // if the switch to command line button was pressed
        else if(source == switchToConsole){
            // immediately switch the output channel to the console
            UserInput.getInstance().changeOutput(UserInput.OUTPUT_MODE.Console);
        }
   }

    public void drawPanel(Graphics g){


        g.setColor(Color.WHITE);
        g.drawLine(0,dimension.height - 7,dimension.width,dimension.height - 7);

        // draw the universe time
        Font old = g.getFont();
        g.setFont(clockFont);
        g.drawString(universe.getUniverseTime().toString(), clockX,clockFont.getSize() + 10);

        // show the current system message
        g.setColor(Color.MAGENTA);
        g.drawString("System Message: " + MessageStream.getInstance().getCurrentMessage(),clockX,70);

        // draw the total load of the network
        g.setColor(Color.YELLOW);
        int aggCount = 0, totalLoad = 0, houseCount = 0;
        for(Aggregator a : universe.getAggregators()){
            ++aggCount;
            totalLoad += a.getCumulativeDemand();
            houseCount += a.getAttachedHouses().size();
        }
        g.drawString("Net. Load : " + totalLoad + "kwh, " + aggCount + " A's, " + houseCount + " H's",
                this.dimension.width-(4*View.BUTTON_W),70);
        
        // restore the initial font
        g.setFont(old);
    }

    public void parseClick(int x, int y){
        // clicking anywhere (other than a button) restores the main card to the Grid Overview
        parent.changeView(View.v_OVERVIEW);
    }
}