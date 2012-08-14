package gui;
import javax.swing.*;
import java.awt.*;
import universe.IObserver;
import java.awt.event.*;
import infopackage.ConsumptionPackage;

/**Represents a clickable self-contained region on the GUI
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public abstract class ClickablePanel extends JPanel implements ActionListener{
    /**the dimensions that this panel takes up on the screen*/
    protected Dimension dimension;
    /**the background color of this panel*/
    protected Color background;
    /**stores the consumption data for a house to use in informative displays to the user*/
    public  ConsumptionPackage _cp;

    /**Creates a new ClickablePanel with the supplied
     * dimension and background colour
     *
     * @param dim dimension defining the height and width of the ClickablePanel
     * @param bg the background colour of the ClickablePanel
     */
    public ClickablePanel(Dimension dim, Color bg){
        dimension = dim;
        background = bg;
        this.setMinimumSize(dimension);
        this.setMaximumSize(dimension);
    }

    /**Makes the data/contents of this ClickablePanel visible
     * to the contents pane of this ClickablePanel
     *
     * @param g the graphics object belonging to this ClickablePanel
     * used in painting data to the contents pane.
     */
    @Override
    public void paintComponent(Graphics g) {
       // set the background of the panel
       g.setColor(background);
       g.fillRect(0,0 , dimension.width, dimension.height);
       // call to the subclass-implemented draw method - ensuring the frame is repainted each time
       drawPanel(g);
    }

    /**A method used by a house to securely submit its consumption data without other objects
     * in the system being able to access it
     *
     * @param cp the consumption data for the house in question
     */
    public final void consumption(ConsumptionPackage cp){
        _cp = cp;
    }


    /**The repaint method that subclasses should use to redraw themselves
     * and update the data they are presenting to the user
     *
     * @param g the graphics object to use for painting
     */
    public abstract void drawPanel(Graphics g);
    /**Because not all regions of a normal panel are clickable by default, this method
     * allows the subclass panel to parse click co-ordinates to work out what the user
     * clicked on
     *
     * @param x the x co-ordinate of the click
     * @param y the y-co-ordinate of the click
     */
    public abstract void parseClick(int x, int y);

    /**Performs action triggered by ActionListener's action event
     *
     * @param event the action event performing this action
     */
    public abstract void actionPerformed(ActionEvent event);
}
