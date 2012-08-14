package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import household.HouseHold;
import input.IInput;
/**The GUI output channel parent that contains all of the context-specific
 * view panels
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class View implements MouseListener, IInput{
    /**Gives us access to the user's environment*/
    private static Toolkit tk = Toolkit.getDefaultToolkit();
    
    /**the width of the GUI*/
    public final static int Width = tk.getScreenSize().width;
    /**the height of the GUI*/
    public final static int Height = tk.getScreenSize().height;
    /**uniform aggregator banner height*/
    public final static int AGG_H = 40;
    /**uniform house width*/
    public final static int HOUSE_W = 13;
    /**uniform house height*/
    public final static int HOUSE_H = 17;
    /**general padding*/
    public final static int PADDING = 5;
    /**uniform padded house width*/
    public final static int PADDED_HOUSE_W = HOUSE_W + PADDING;
    /**uniform padded house height*/
    public final static int PADDED_HOUSE_H = HOUSE_H + PADDING;
    /**the universe panel height*/
    public final static int d_UniverseH = 100;
    /**the main card layouts height*/
    public final static int d_ContentH =  Height - d_UniverseH;
    /**uniform width of a button*/
    public final static int BUTTON_W = 100;
    /**uniform height of a button*/
    public final static int BUTTON_H = 35;

    /**the dimensions of the GUI*/
    private Dimension FRAME_SIZE = new Dimension(Width, Height);

    /**the frame on to which the child views are attached*/
    private JFrame frame;
    /**a card layout is used to flick between context-specific simulation views*/
    private JPanel cards;


    
    /**a String identifier for the House Context card*/
    public final static String v_HOUSE_CONTEXT = "House_Context";
    /**a String identifier for the Grid Overview card*/
    public final static String v_OVERVIEW = "Grid_Overview";
    /**a String identifier for the House Chart card*/
    public final static String v_CHART = "House_Chart";
    /**a String identifier for the Single Aggregator card*/
    public final static String v_SINGLE_AGG = "Single_Agg";

    
    /**the Universe Panel*/
    private ClickablePanel p_Universe;
    /**the Grid Overview card*/
    private ClickablePanel p_Overview;
    /**the House Context card*/
    private ClickablePanel p_HouseContext;
    /**the House Chart card*/
    private ClickablePanel p_ChartHouse;
    /**the Single Aggregator card*/
    private ClickablePanel p_SingleAgg;

    
    /**the dimensions of the Universe panel section of the GUI*/
    private Dimension d_universe = new Dimension(Width, d_UniverseH);
    /**the dimensions of the card layout section of the GUI*/
    private Dimension d_content = new Dimension(Width, d_ContentH);
    /**uniform font to use for drawing labels*/
    public final static Font labelFont = new Font("labelFont", Font.BOLD, 14);

    /**
     * Creates a new View instance.
     */
    public View(){
        // call set up
        // NOTE: This is singleton so setUp() will only happen once
        setup();
    }

    /**Returns the frame attached to the GUI
     *
     * @return the frame object attached to the GUI
     */
    public JFrame getFrame(){return frame;}

    /**Returns the contenxt-specific card layout attached to the GUI
     *
     * @return the context-specific card layout attached to the GUI
     */
    public JPanel getPanel(){return cards;}

    /**Changes the current card being shown on the GUI to that
     * identified by the given panelName
     *
     * @param panelName an identifier of the card to change to
     */
    public void changeView(String panelName){
        // cast the layout
        CardLayout cl = (CardLayout)(cards.getLayout());
        // show the card that matches the given panel name
        cl.show(cards, panelName);

    }

    /**Sets the subject of the house context views,
     * used as a precursor to switching to that view so that
     * everything has been loaded and is ready
     *
     * @param h the new subject of the house context views
     */
    public void setHouseContext(HouseHold h){
        // Have to cast the ClickablePanel to its sub-type
        // set the house context panel's subject
        ((HouseContextPanel)p_HouseContext).setHouse(h);
        // at the same time we set the chart panel's subject
        // NOTE: they always represent the same house
        ((HouseChartPanel) p_ChartHouse).setHouse(h);
    }
 

    /**Sets up the metrics of the GUI, and the underlying objects/data structures
     * that are required
     */
    public void setup() {
        // the title
        frame = new JFrame("SmartGrid Simulation - The A Team");
        cards = new JPanel();

        // set up the Universe Panel
        p_Universe = new UniversePanel(this, new Dimension(Width,100), Color.black);

        // create the cards
        p_Overview = new GridOverviewPanel(this, d_content, Color.black);
        p_ChartHouse = new HouseChartPanel(this,null,d_content,Color.black);
        p_HouseContext = new HouseContextPanel(this, null, d_content, Color.black);
        p_SingleAgg = new SingleAggregatorPanel(this, d_content, Color.black);

        // set up frame attributes
        frame.setPreferredSize(FRAME_SIZE);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set up the cards
        cards.setLayout(new CardLayout());
        cards.add(p_Overview, v_OVERVIEW);
        cards.add(p_HouseContext, v_HOUSE_CONTEXT);
        cards.add(p_ChartHouse, v_CHART);
        cards.add(p_SingleAgg, v_SINGLE_AGG);

        // allow the frame to receive mouse events that will be parsed
        frame.addMouseListener(this);

        frame.getContentPane().setLayout(null);

        // set up the geometry of the GUI
        p_Universe.setBounds(0, 0, d_universe.width, d_universe.height);
        frame.add(p_Universe);
        cards.setBounds(0, d_UniverseH, d_content.width, d_content.height);
        frame.add(cards);

        // misc. frame set up
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

     public void mouseEntered(MouseEvent event){}
    public void mouseExited(MouseEvent event){}
    public void mouseReleased(MouseEvent event){}
    public void mousePressed(MouseEvent event){}

    public void mouseClicked(MouseEvent event){

        int y = event.getY();

        // initally we assume the user has clicked on the Universe panel
        ClickablePanel target = p_Universe;

        // however, if the click y co-ordinate is below the Universe panel
        if(y > d_UniverseH){
            // we work out which card is showing at the present time
            for (Component comp : cards.getComponents() ) {
                if (comp.isVisible() == true) {
                    // the target for the click then becomes the visible card
                    target = (ClickablePanel)comp;
                }
            }
            // we calculate the relative y co-ordinate,
            // taking in to account the height of the Universe panel
            y -= d_UniverseH; // relative Y value
        
        }

        // ask the deduced target to parse the click and process it
        target.parseClick(event.getX(), y);
    }

  public void close(){
    frame.dispose();
  }

  public void open(){}

  public void update() {
        frame.repaint();
      
  }

}