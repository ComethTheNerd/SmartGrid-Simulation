package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import aggregator.Aggregator;
import universe.SmartGridUniverse;
import java.util.ArrayList;
import household.HouseHold;
import java.util.HashMap;
import universe.ThreadSafeControl;
import utils.Config;

/**Presents a graphical overview representation of the current simulation
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class GridOverviewPanel extends ClickablePanel {
    /**A handle to the Smart Grid Universe singleton instance*/
    protected SmartGridUniverse universe;
   
    
    /**how many aggregators to display*/
    protected int aggCount = 0;
    /**the width of each aggregator section on screen*/
    protected int aggregatorWidth = 0;
    /**how many houses to show per line*/
    protected int housesPerLine = 1;

    
    /**maximum houses for the aggregator*/
    protected final int MAX_HOUSES_FOR_AGG = 450;
    /**y co-ordinate of where to draw the aggregator*/
    protected final int AGG_Y = 10;
    /**padding to apply to the aggregator*/
    protected final int AGG_PAD = 17;
    /**relative x co-ordinate of the aggregator label*/
    protected final int AGG_LABEL_X = 10;
    /**y co-ordinate of the aggregator label*/
    protected final int AGG_LABEL_Y = AGG_Y + AGG_PAD;
    /**y co-ordinate of the aggregator's total usage label*/
    protected final int USAGE_LABEL_Y = AGG_Y + 35;
    /**y co-ordinate of the aggregator's houses count label*/
    protected final int HOUSES_LABEL_Y = USAGE_LABEL_Y + 30;
    /**y co-ordinate of where to begin drawing the houses*/
    protected final int HOUSES_Y = HOUSES_LABEL_Y - 10;
    /**relative x co-ordinate of the house label*/
    private int housesLabelX;
    /**the parent of this card*/
    protected View parent;
    /**buttons to perform operations on each aggregator*/
    protected HashMap<Aggregator, JButton[]> buttons;
    
    /**buttons per aggregator*/
    protected final int NUMBER_OF_BUTTONS = 2;
    /**the index of the remove button in the buttons array*/
    protected final int REMOVE_AGG = 0;
    /**the index of the add house button in the buttons array*/
    protected final int ADD_HOUSE = 1;
    /**the aggregators to draw to the screen*/
    private ArrayList<Aggregator> toDraw;
    
    /**the last count of aggregators in the universe*/
    private int lastAggCount = 0;
    /**the last checked highest number of houses attached to an aggregator*/
    private int lastMaxHouseCount = 0;

    public GridOverviewPanel(View v, Dimension dim, Color bg) {
        super(dim, bg);

        parent = v;
        this.setLayout(null);
        universe = SmartGridUniverse.getInstance();

        buttons = new HashMap<Aggregator, JButton[]>();

    }
    

    public void drawPanel(Graphics g) {

        // do the drawing...
        // get the current aggregators
        toDraw = universe.getAggregators();
        int maxHouses = 0;
        // work out if we have too much information to draw on this view,
        // and if so, switch the view
        for (Aggregator a : toDraw) {
            int size = a.getAttachedHouses().size();
            if(size > Config.OVERVIEW_MAX_HOUSES_PER_AGG){
                parent.changeView(View.v_SINGLE_AGG);
                return;
            }
           // calculate the highest number of houses attached to any aggregator
            if (size > maxHouses) {
                maxHouses = size;
            }

        }
        // if something has changed since last draw call
        if (maxHouses != lastMaxHouseCount || toDraw.size() != lastAggCount) {
            if (toDraw.size() > Config.OVERVIEW_MAX_AGGS_ON_SCREEN || maxHouses > MAX_HOUSES_FOR_AGG) {
                parent.changeView(View.v_SINGLE_AGG);
                return;
            }



            alignAggregators(); // align the aggregators on the screen, work out co-ordinates

            // draw each aggregator in turn
            for (int x = 0; x < toDraw.size(); x++) {
                Aggregator focus = toDraw.get(x);

                int aggXPos = x * aggregatorWidth;

                drawAggregator(g, focus, aggXPos, View.AGG_H, aggregatorWidth);
            }

        }
    }


    public void actionPerformed(ActionEvent event) {
        JButton button = (JButton) event.getSource();

        // look through all of the aggregator button sets to find the pressed button
        for (Aggregator a : buttons.keySet()) {

            JButton[] jbs = buttons.get(a);
            // if the remove button of this aggregator was clicked
            if (jbs[REMOVE_AGG] == button) {
                // create a request to remove it from the simulation asynchronously
                ThreadSafeControl.removeAggregator(a);
                for (JButton jb : jbs) {
                    jb.removeActionListener(this);
                    this.remove(jb);
                }
            }
            // if the add house button of this aggregator was clicked
            if (jbs[ADD_HOUSE] == button) {
                // create a request to add a house to this aggregator
                ThreadSafeControl.addHouse(a, new HouseHold(a));
            }
        }


    }

    /**Calculates the dimensions for the aggregators that will be drawn
     * upon next repaint, dynamically resizing them based on their current
     * state, and the state of the universe
     *
     */
    private void alignAggregators() {
        // the number of aggregators to draw
        int noOfAgs = toDraw.size();
        // if nothing has changed since last time, return
        if (noOfAgs == aggCount) {
            return;
        }

        aggCount = noOfAgs;

        // work out the width of an aggregator on-screen dynamically
        if (noOfAgs > 0) {
            aggregatorWidth = dimension.width / toDraw.size();
        }
        // how many houses to draw on each line for an aggregator
        housesPerLine = aggregatorWidth / View.PADDED_HOUSE_W;
    }

    /**Draws a representation of the given Aggregator to the screen
     *
     * @param g the graphics object to draw with
     * @param agg the Aggregator instance to draw a representation of
     * @param x the x co-ordinate of where to begin drawing
     * @param y the y co-ordinate of where to begin drawing
     * @param aggWidth the width allowed to be taken up by this aggregator
     */
    public void drawAggregator(Graphics g, Aggregator agg, int x, int y, int aggWidth) {


        g.setColor(Color.BLUE);
        g.fillRect(x + 3, AGG_Y, aggWidth - 6, y);
        int buttonWidth = aggWidth / 5;

        /**************************************************/
        // if this is a new aggregator we need to create and wire up
        // buttons for it
        if (!buttons.containsKey(agg)) {

            JButton[] jbs = new JButton[NUMBER_OF_BUTTONS];

            jbs[REMOVE_AGG] = new JButton("X");
            jbs[REMOVE_AGG].addActionListener(this);
            this.add(jbs[REMOVE_AGG]);

            jbs[ADD_HOUSE] = new JButton("+H");
            jbs[ADD_HOUSE].addActionListener(this);
            this.add(jbs[ADD_HOUSE]);

            buttons.put(agg, jbs);
        }



        JButton[] jbs = buttons.get(agg);
        jbs[REMOVE_AGG].setBounds(x + aggregatorWidth - (buttonWidth), AGG_Y, buttonWidth, y);
        jbs[ADD_HOUSE].setBounds(x + aggregatorWidth - (buttonWidth * 2), AGG_Y, buttonWidth, y);


        /**************************************************/
        g.setColor(Color.WHITE);
        Font oldFont = g.getFont();
        g.setFont(View.labelFont);
        g.drawString(agg.toString(), x + AGG_LABEL_X, AGG_LABEL_Y);
        g.setFont(oldFont);

        // get the attached houses ready to draw them
        ArrayList<HouseHold> houses = agg.getAttachedHouses();

        // draw the labels
        int noOfHouses = houses.size();
        housesLabelX = x;
        g.setColor(Color.YELLOW);
        g.drawString(agg.getCumulativeDemand() + " kw/h", housesLabelX + 10, USAGE_LABEL_Y);

        g.setColor(Color.WHITE);
        g.drawString(noOfHouses + " Homes", housesLabelX + 5, HOUSES_LABEL_Y);

        int y2 = HOUSES_Y, x1 = x + 3;
        // draw each house
        for (int i = 0; i < noOfHouses; i++) {
            // move to next line when max on a line is reached
            if (i % housesPerLine == 0) {
                y2 += View.PADDED_HOUSE_H;
                x1 = x + 3;
            }

            HouseHold h = houses.get(i);

            drawHouse(g, h.getCurrentState().getColor(), x1, y2, View.HOUSE_W, View.HOUSE_H);
            x1 += View.PADDED_HOUSE_W;
        }

    }

   /**Draws a representation of a house to the screen
     *
     * @param g the graphics object to draw with
     * @param col the color to draw this house
     * @param x the x co-ordinate of where to begin drawing
     * @param y the y co-ordinate of where to begin drawing
     * @param aggWidth the width of the house drawing
    * @param height the height of the house drawing
     */
    protected void drawHouse(Graphics g, Color col, int x, int y, int width, int height) {
        // save the current graphics color to restore after
        Color original = g.getColor();

        int roofHeight = (int) (height / 4), houseHeight = height - roofHeight;
        g.setColor(col);
        // draw the body of the house
        g.fillRect(x, y + roofHeight, width, houseHeight);

        // draw the roof!
        Polygon p = new Polygon();
        p.addPoint(x, y + roofHeight);
        p.addPoint(x + width, y + roofHeight);
        p.addPoint(x + (width / 2), y);
        g.fillPolygon(p);
        // restore initial color of graphics object
        g.setColor(original);
    }

    public void parseClick(int x, int y) {
        // if we have no aggregators we have nothing to click on
        if (toDraw.isEmpty()) {
            return;
        }
        parseHouse(x,y);
    }

    /**Mathematically works out which house was clicked on
     * based on the click co-ordinates
     *
     * @param x the x position of the click
     * @param y the y position of the click
     */
    protected void parseHouse(int x, int y){
        // mathematically work out the click region based on number of aggregators
        int agg = x / aggregatorWidth,
                locX = (x % aggregatorWidth) / View.PADDED_HOUSE_W,
                locY = ((y - HOUSES_Y) - (View.PADDED_HOUSE_H * 2)) / View.PADDED_HOUSE_H;
 
        // house number is based on how many houses per line
        int houseNo = (locY * housesPerLine) + locX;

        Aggregator aggregator = toDraw.get(agg);
        // if a valid house is clicked on then we need to change the view to a House Context panel
        if (houseNo >= 0 && houseNo < aggregator.getAttachedHouses().size()) {
            // Load Detailed View Of House Here
            parent.setHouseContext(aggregator.getAttachedHouses().get(houseNo));
            parent.changeView(View.v_HOUSE_CONTEXT);
        } else {
            return;// invalid click area, no valid house in this region
        }

    }
}
