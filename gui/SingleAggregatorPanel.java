package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import aggregator.Aggregator;
import java.util.ArrayList;
import household.HouseHold;
import universe.ThreadSafeControl;
import utils.Config;
import java.util.HashMap;

/**When there is too much simulation information to display on the Grid Overview
 * panel we switch to a Single Aggregator detailed view
 *
 * @author Darius Hodaei
 */
public class SingleAggregatorPanel extends GridOverviewPanel {

    /**the current aggregator being represented*/
    private Aggregator subject;
    /**a combo box to select to view another aggregator in the simulation*/
    private JComboBox aggSelector;
    /**all aggregators currently in the simulation universe*/
    private ArrayList<Aggregator> uniAggs;
    /**the last count of the number of aggregators in the simulation universe*/
    int lastSize = 0;
    JButton[] buts;

    /**Creates a new SingleAggregatorPanel instance with supplied
     * View, Dimension and background Color.
     *
     * @param v parent view attached to this SingleAggregatorPanel
     * @param dim dimension of this SingleAggregatorPanel
     * @param bg background colour of SingleAggregatorPanel
     */
    public SingleAggregatorPanel(View v, Dimension dim, Color bg) {
        super(v, dim, bg);

        // set up the view components
        // the aggregator selector box
        aggSelector = new JComboBox();
        aggSelector.setBounds(this.dimension.width - 220, this.dimension.height - 70, 200, 25);
        //aggSelector.addActionListener(this);

        this.add(aggSelector);
        aggSelector.addActionListener(this);
        aggSelector.setEnabled(true);
        aggSelector.setEditable(true);

        // set the width of the aggregator on screen
        aggregatorWidth = this.dimension.width;
        housesPerLine = aggregatorWidth / View.PADDED_HOUSE_W;
    }

    /**Changes the subject Aggregator instance that is being represented
     * by this view
     *
     * @param a the new Aggregator instance to represent in subsequent repaints
     */
    public void setAggregator(Aggregator a) {
        subject = a;
    }

    /**Updates the options shown in the Aggregator selector combo box
     * in order to reflect all of the Aggregators currently in the simulation
     * universe - only called when the Aggregator count changes
     */
    private void updateSelector() {
        // clear the selector items first
        aggSelector.removeAllItems();

        // create an entry for each aggregator currently in the simulation universe
        String[] aggs = new String[uniAggs.size()];
        for (int x = 0; x < aggs.length; x++) {
            aggSelector.addItem(uniAggs.get(x).toString());
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource().getClass() == JComboBox.class) {

            // switch the aggregator being represented when one is selected
            // from the combo box
            try {
                int n = aggSelector.getSelectedIndex();

                setAggregator(uniAggs.get(n));
            } catch (Exception e) {
            }
            return;
        }

        // otherwise the aggregator buttons may have been clicked on...

        JButton button = (JButton) event.getSource();
        // JButton[] jbs = buttons.get(subject);
        // if the remove button was clicked on
        if (buts[REMOVE_AGG] == button) {
            // create a request to asynchronously remove this aggregator
            ThreadSafeControl.removeAggregator(subject);
            /*for (JButton jb : jbs) {
            jb.removeActionListener(this);
            this.remove(jb);
            }
            buttons = new HashMap<Aggregator, JButton[]>();*/
            subject = null;
        }
        // if the add house button was clicked
        if (buts[ADD_HOUSE] == button) {
            // create a request to add a house to this aggregator asynchronously
            ThreadSafeControl.addHouse(subject, new HouseHold(subject));
        }

    }

    @Override
    public void drawPanel(Graphics g) {
        // if we need to create and wire up the control buttons
        if (buts == null) {
            buts = new JButton[NUMBER_OF_BUTTONS];
            // add the remove aggregator button
            buts[REMOVE_AGG] = new JButton("X");
            buts[REMOVE_AGG].addActionListener(this);
            this.add(buts[REMOVE_AGG]);
            // add the add house button
            buts[ADD_HOUSE] = new JButton("+H");
            buts[ADD_HOUSE].addActionListener(this);
            this.add(buts[ADD_HOUSE]);
            // set the bounds of both buttons on screen
            int buttonWidth = dimension.width / 5;
            buts[REMOVE_AGG].setBounds(dimension.width - (buttonWidth), AGG_Y, buttonWidth, View.AGG_H);
            buts[ADD_HOUSE].setBounds(dimension.width - (buttonWidth * 2), AGG_Y, buttonWidth, View.AGG_H);

        }

        uniAggs = universe.getAggregators();
        // check whether we need to update the options in
        // the aggregator selector combo box
        int currentSize = uniAggs.size();

        // if we now have a number of aggregators we can show on GridOverviewPanel
        if (currentSize <= Config.OVERVIEW_MAX_AGGS_ON_SCREEN) {
            for (JButton jb : buts) {
                jb.removeActionListener(this);
                this.remove(jb);
            }
            buts = null;

            // switch to it
            parent.changeView(View.v_OVERVIEW);
            return;
        }


        // if we do not have an aggregator to draw
        if (subject == null) {
            // get the last aggregator
            subject = uniAggs.get(currentSize - 1); // default
        }

        if (currentSize != lastSize) {
            // if the number of aggregators in the simulation universe has
            // changed we need to update the aggregator selector combo box
            updateSelector();
            lastSize = currentSize;
        }

        Font f = g.getFont();

        g.setFont(View.labelFont);
        g.setColor(Color.WHITE);
        g.drawString("NETWORK IS TOO BIG TO SHOW ON ONE SCREEN. NOW VIEWING SIMULATION IN SINGLE AGGREGATOR MODE.", 10, this.dimension.height - 50);
        g.drawString("> SELECT AGGREGATOR : ", this.dimension.width - 400,
                this.dimension.height - 50);

        g.setFont(f);
        // draw the aggregator to the screen
        drawAggregator(g);
    }

    /**Draws a representation of the given Aggregator to the screen
     *
     * @param g the graphics object to draw with
     */
    public void drawAggregator(Graphics g) {
        // draw aggregator bar
        g.setColor(Color.BLUE);
        g.fillRect(3, AGG_Y, dimension.width - 6, View.AGG_H);

        // show aggregator label
        g.setColor(Color.WHITE);
        Font oldFont = g.getFont();
        g.setFont(View.labelFont);
        g.drawString(subject.toString(), AGG_LABEL_X, AGG_LABEL_Y);
        g.setFont(oldFont);

        // get the attached houses ready to draw them
        ArrayList<HouseHold> houses = subject.getAttachedHouses();

        // draw the labels
        int noOfHouses = houses.size();

        g.setColor(Color.YELLOW);
        g.drawString(subject.getCumulativeDemand() + " kw/h", 10, USAGE_LABEL_Y);

        g.setColor(Color.WHITE);
        g.drawString(noOfHouses + " Homes", 5, HOUSES_LABEL_Y);

        int y2 = HOUSES_Y, x1 = 3;
        // draw each house
        for (int i = 0; i < noOfHouses; i++) {
            // move to next line when max on a line is reached
            if (i % housesPerLine == 0) {
                y2 += View.PADDED_HOUSE_H;
                x1 = 3;
            }

            HouseHold h = houses.get(i);

            drawHouse(g, h.getCurrentState().getColor(), x1, y2, View.HOUSE_W, View.HOUSE_H);
            x1 += View.PADDED_HOUSE_W;
        }

    }

    @Override
    public void parseClick(int x, int y) {
        // parse which house has been clicked on using the width of the aggregator...
        int locX = (x % aggregatorWidth) / View.PADDED_HOUSE_W,
                locY = ((y - HOUSES_Y) - (View.PADDED_HOUSE_H * 2)) / View.PADDED_HOUSE_H;

        // ...and the number of houses drawn on each line
        int houseNo = (locY * housesPerLine) + locX;

        // once we have deduced which house was clicked on///
        if (houseNo >= 0 && houseNo < subject.getAttachedHouses().size()) {
            // Load Detailed View Of House Here
            parent.setHouseContext(subject.getAttachedHouses().get(houseNo));
            parent.changeView(View.v_HOUSE_CONTEXT);
        } else {
            return;// invalid click area, no valid house in this region
        }
    }
}
