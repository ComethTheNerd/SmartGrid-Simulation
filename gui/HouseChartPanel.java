package gui;

import appliance.core.Appliance;
import household.HouseHold;
import infopackage.ConsumptionPackage;
import infopackage.IHeader;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import universe.SmartGridUniverse;

/**Presents a chart plotting in real-time,
 * showing HouseHold energy usage and the individual energy
 * usage of appliances of that household
 *
 * @author Tokoni Kemenanabo
 */
public class HouseChartPanel extends ClickablePanel{

    /**Time hour value on the universal clock*/
    private int timeIndx = 0;
    /**Parent component of this ClickablePanel*/
    private View parent;
    /**HouseHold from which charting data is derived*/
    private HouseHold house;
    /**Dimension specifying the size of this ClickablePanel*/
    private Dimension d;
    /**Reference to the singleton instance of the SmartGridUniverse*/
    private SmartGridUniverse universe;
    /**Height of the chart displayed in this ClickablePanel*/
    private int cHeight;
    /**Width of the chart displayed in this ClickablePanel*/
    private int cWidth;
    /**ArrayList of XYSeries for each appliance in connected HouseHold*/
    private ArrayList<XYSeries> appSeries;
    /**ArrayList of Appliances stored in the connected HouseHold*/
    private ArrayList<Appliance> apps;
    /**XYSeries for plotting data about household energy usage*/
    private XYSeries houseUsage;
    /**XYSeries for plotting data about average energy usage across homes on concerning aggregator*/
    private XYSeries averageUsage;

    /**Button to view household information*/
    private JButton back;
    /**Defines properties of the x axis of chart*/
    private ValueAxis xAxis ;
    /**Defines properties of the y axis of chart*/
    private NumberAxis yAxis;
    /**Defines properties of the y axis of  appliances chart*/
    private NumberAxis appYAxis;
    /**Chart for the household usage and average usage*/
    private JFreeChart chart;
    /**Chart for the appliances usage*/
    private JFreeChart appChart;
    /**HashMap from Appliance to Integer specifying current usage of appliance*/
    protected HashMap<Appliance, Integer> recentUses;

    /**Creates a new HouseChartPanel with specified parent, house, dimension and background colour
     *
     * @param parent the parent component of this ClickablePanel
     * @param house the houseHold from which charting data is derived
     * @param d the dimension specifying the size of this ClickablePanel
     * @param c the background colour of this ClickablePanel
     */
    public HouseChartPanel(View parent,HouseHold house,Dimension d, Color c){
        super(d,c);
        this.parent = parent;
        this.house = house;
        this.d = d;
        universe = SmartGridUniverse.getInstance();
        cHeight = (int)(d.height*0.8);//80% of dimension as chart height
        cWidth = (int)(d.width*0.49);//49% of dimension as chart width
        this.setLayout(null);//absolute positioning.
        appSeries = new ArrayList<XYSeries>();//XYseries for each appliance stored here
        apps = null;
        houseUsage = new XYSeries("Household Usage"); //dataseries for household usage
        averageUsage = new XYSeries("Average Usage");//data series for house energy request
        XYSeriesCollection dataGroup = new XYSeriesCollection();//collection of series. Would plot two lines
        dataGroup.addSeries(averageUsage);//add series
        dataGroup.addSeries(houseUsage);//add series
        xAxis = new NumberAxis();//xAxis of the presented charts
        yAxis = new NumberAxis();//yAxis of the presented charts
        appYAxis = new NumberAxis();//yAxis of the appliance usage chart
        recentUses = new HashMap<Appliance, Integer>();

        //XY line chart with dataset of two series- Time:xAxis; Energy:yaxis
        chart = ChartFactory.createXYLineChart("House Usage/ Energy", "Time", "Energy",
                                                                        dataGroup,
                                                                        PlotOrientation.VERTICAL,
                                                                        true, true, false);
        //build details of chart
        build();
    }

    /**Builds details of the charts and buttons of the panel
     *
     */
    private void build(){

        xAxis.setRange(0, 23);
        xAxis.setAutoRange(true);//Scroll chart as needed
        xAxis.setFixedAutoRange(23);//24 values on xAxis each time
        xAxis.setLowerMargin(0.1); // 10% margin
        xAxis.setUpperMargin(0.1);
        xAxis.setLabel("Time");//Label Axis

        yAxis.setRange(-10000,26000);
        yAxis.setFixedAutoRange(36000);//36000 values on yaxis each time
        yAxis.setLowerMargin(0.1); // 10% margin
        yAxis.setUpperMargin(0.1);
    	yAxis.setLabel("Energy");//Label Axis

        appYAxis.setRange(-1000,6000);
        appYAxis.setFixedAutoRange(7000);//7000 values on yaxis each time
        appYAxis.setLowerMargin(0.1); // 10% margin
        appYAxis.setUpperMargin(0.1);
    	appYAxis.setLabel("Energy");//Label Axis
        
        chart.getXYPlot().setDomainAxis(xAxis);//Set xAxis
        chart.getXYPlot().setRangeAxis(yAxis);//set yaxis
        //chart.getXYPlot().zoom(0.5);//-----

        back = new JButton("BACK");//back to house context
        back.setBounds((d.width/2)-40, (int)(d.height-(d.height*0.15)), 80, 30);
        back.addActionListener(this);
        add(back);
      
    }

    /**Scrolls the chart back to the 0 mark (12am),
     * This is done to preserve the 24 hour limit of a day
     * as represented on the charts.
     */
    public void scrollBackChart(){
        timeIndx = 0;
        resetChart();
    }

    /**Clears the data on the chart display to allow new visible data to be plotted
     */
    public void resetChart(){
        houseUsage.clear();//clear data on series for fresh start
        averageUsage.clear();//clear data on series for fresh start
        for(XYSeries x : appSeries){
            x.clear();//clear data on series for fresh start
        }
    }

    /**Specifies Household for which charts are plotted.
     * Resets data on the chart display and creates a
     * new appliance chart to deal with addition/removal of appliances.
     *
     * @param h the household to produce charts for
     */
    public void setHouse(HouseHold h){
        house = h;//Set house
        appSeries.clear();//reset appliances chart
        recentUses.clear();//reset appliances usage data
        apps = house.getAllAppliances();//get set of appliances from household

        //Create new appliance usage chart and create XYSeries
        //for each appliance present in household
        appChart = ChartFactory.createXYLineChart("Individual Appliance Usage", "Time", "Energy",
                                                                        this.getAppDataset(),
                                                                        PlotOrientation.VERTICAL,
                                                                       true, true, false);
        //Set axes of appliance usage chart again
        appChart.getXYPlot().setDomainAxis(xAxis);
        appChart.getXYPlot().setRangeAxis(appYAxis);

        //Clear all existing data from chart
        //Plotting begins when user requests household data
        resetChart();

    }


    /**Handles the process of presenting information(charts) to the display,
     * using the graphics instance attached to this HouseChartPanel
     *
     * @param g the graphics instance attached to the HouseChartPanel
     */
    @Override
    public void drawPanel(Graphics g) {
        //get up to date Household consumtion
        house._contextHouseConsumption(this);
        //get current time hour
        timeIndx = universe.getUniverseTime().getCurrentHour();

        //Extract household usage and
        //average usage across all houses on concerned aggregator
        int usage = extractUsage();
        int average = extractAverageUsage();

        //The chart goes from right to left so made time values negative(-21 = 9pm)
        houseUsage.add(timeIndx*-1, usage);
        averageUsage.add(timeIndx*-1, average);

        //Get current usages of appliances and plot data on the graph
        updateAppChart();

        //Set title of household chart to current time
        chart.setTitle(universe.getUniverseTime().toString());            

        //Draw chart images
        g.drawImage((Image)chart.createBufferedImage(cWidth, cHeight),
                                                           0,0,this);
        g.drawImage((Image)appChart.createBufferedImage(cWidth, cHeight),
                                                        (int)(d.width*0.5), 0, this);

        //if time is 2300hrs scroll back to 0000hrs
        //this is a cautionary measure as this should be done elsewhere
        if(timeIndx == 23){
            scrollBackChart();
        }
        
    }

    /**Parses the x,y coordinates of a click to compute what item is clicked
     *
     * @param x x-coordinate of click
     * @param y y-coordinate of click
     */
    @Override
    public void parseClick(int x, int y) {
        
    }

    /**Performs actions sent by ActionListener action event
     *
     * @param event the action event performing this action
     */
    @Override
    public void actionPerformed(ActionEvent event) {

        //Done by only the BACK button at the moment
        scrollBackChart();
        recentUses.clear();
        //Change view to the House Context Panel
        parent.changeView(View.v_HOUSE_CONTEXT);
        
    }

    /**Extracts House usage from household consumption package
     *
     * @return house current usage
     */
    public int extractUsage(){

        //index of stored household usage in consumption package
        int cons = ConsumptionPackage.Header.CURRENT_USAGE.getIndex();
        //headers of consumption package
        IHeader[] headers = _cp.getHeaders();

        return _cp.getValue(headers[cons]);

    }

    /**Extracts Average House usage from consumption package
     *
     * @return average house usage
     */
    public int extractAverageUsage(){

        //index of stored average household usage in consumption package
        int cons = ConsumptionPackage.Header.AVERAGE_USAGE.getIndex();
        //headers of consumption package
        IHeader[] headers = _cp.getHeaders();
        return _cp.getValue(headers[cons]);

    }

    /**Updates the applications chart by updating the
     * XYSeries of each appliance
     *
     */
    private void updateAppChart(){
        try{
        for(int i = 0; i<apps.size();i++){
            //submit recent usage to hash map
            apps.get(i).getMostRecentUsage(this);
            //plot next data for this appliance's series
            appSeries.get(i).add(timeIndx*-1,recentUses.get(apps.get(i)));
        }
        }
        catch(Exception e){}
   
    }

    /**Creates and returns a new XYDataset (i.e Collection of XYSeries)
     * containing a XYSeries for each appliance
     *
     * @return a new XYDataset containing a XYSeries for each appliance
     */
    public XYDataset getAppDataset(){

        XYSeriesCollection collection = new XYSeriesCollection();
        for(int i = 0;i<apps.size();i++){
            //Create new xy series for appliance with appliance name
            XYSeries x = new XYSeries(apps.get(i).toString()+" Usage "+i);
            //add series to arraylist for future reference
            appSeries.add(x);
            //Add series to series collection which provides plotting info
            collection.addSeries(x);
        }

        return collection;
    }

    /**Populates HashMap with up to date data about appliance energy usage
     *
     * @param a appliance providing energy data
     * @param energy energy data of appliance
     */
    public void submitMostRecent(Appliance a, int energy){
        recentUses.put(a, energy);
    }


}




