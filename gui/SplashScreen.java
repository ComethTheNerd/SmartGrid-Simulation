package gui;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;
import utils.Config;
import universe.SmartGridUniverse;

/**This creates the splash screen displayed at the beginning of the simulation
 *
 * @author Tokoni Kemenanabo
 */
public class SplashScreen extends JWindow {

    /**JPanel content Pane*/
    private JPanel windowContent;
    /**The window width*/
    private int windowWidth;
    /** The window height*/
    private int windowHeight;
    /** The screen size of the window*/
    private Dimension screenSize;
    /** The x position of the window*/
    private int xPosition;
    /** The y position of the window*/
    private int yPosition;
    /**Image icon for image displayed*/
    private ImageIcon splash;
    /** JLabel for the splashimage*/
    private JLabel splashImage;

    /**
     * Constructor that creates the splash screen and calls the splash method
     */
    public SplashScreen() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
        }
        //get screen size dimensions
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //get contentPane as JPanel
        windowContent = (JPanel) this.getContentPane();
        //get splash image from directory
        splash = new ImageIcon(this.getClass().getResource("Images/SmartGrid.png"));
        //set window width as image height
        windowWidth = splash.getIconWidth();
        //set window height as image height
        windowHeight = splash.getIconHeight();
        //x position of the window (middle of screen)
        xPosition = (screenSize.width - windowWidth) / 2;
        //y position of the window (middle of screen)
        yPosition = (screenSize.height - windowHeight) / 2;
        //JLabel of splash image which is added to content pane
        splashImage = new JLabel(splash);
        //Present the splash screen
        splash();
    }

    /**
     * Applies the presentational properties of the splash screen
     */
    private void splash() {

        this.setBounds(xPosition, yPosition, windowWidth, windowHeight);
        windowContent.add(splashImage, BorderLayout.CENTER);
        
    }

    /**
     * Displays the splash screen then
     * starts the main simulation after a while
     */
    public void start(){
        this.setVisible(true);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SplashScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setVisible(false);

        //Start the main simulation
        SmartGridUniverse.getInstance().start(Config.DEFAULT_NO_OF_AGGS);

       
    }
}
