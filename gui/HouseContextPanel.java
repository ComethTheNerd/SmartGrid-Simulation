package gui;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import universe.SmartGridUniverse;
import java.awt.event.*;
import household.HouseHold;
import appliance.core.Appliance;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Stack;
import time.ApplianceTimedEvent;
import time.TimedEvent;
import policy.core.PolicyComponent;
import infopackage.*;
import universe.ThreadSafeControl;
import policy.core.PolicyBank;
import household.PowerState;
/**Displays detailed information about an individual HouseHold, including
 * its Appliances, Policy components, and their TimedEvents
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class HouseContextPanel extends ClickablePanel implements ActionListener, ListSelectionListener {
    /**the house being represented by this panel*/
    private HouseHold house;
    /**the parent of this card*/
    private View parent;
    /**a handle to the Smart Grid Universe singleton instance*/
    private SmartGridUniverse universe;

    
    /**appliance classes that can be added to the house*/
    private JComboBox appList;
    /**policy component classes that can be added to the house*/
    private JComboBox polList;
    /**the font to draw information with on the screen*/
    private Font infoBarFont = new Font("infoBar", Font.BOLD, 12);
    /**an array for the appliances and policy component columns on the view*/
    private JList[] _objNames = new JList[2];
    /**an array for the timed events attached to the appliances,
     * and another for the timed events attached to the policy components*/
    private JList[] _eventLists = new JList[2];
    /**the scroll bars to attach to the four columns*/
    private JScrollPane[] _scrolls = new JScrollPane[4];
    /**the headings for each of the four columns*/
    private int[] _headings = new int[4];

    
    /**the remove house button to remove this house from the simulation*/
    private JButton removeHouse;
    /**a button to switch to the HouseChartPanel to show graphical usage information*/
    private JButton graph;
    /**a button to remove the selected Appliances*/
    private JButton removeApps;
    /**a button to remove the selected Policy Components*/
    private JButton removePols;
    
    /**the total number of columns*/
    private final int TOTAL_LISTS = 4;
    /**the index to access columns (lists) relating to Appliances*/
    private final int APPS = 0;
    /**the index to access columns (lists) relating to Policy Components*/
    private final int POLS = 1;
    
    /**the width of the list*/
    private int _listW;
    /**the y co-ordinate of the lists*/
    private int _listY;
    /**the height of the lists*/
    private int _listH;
    /**the padding applied between lists*/
    private int _listPad;
    /**the height of the infobar displaying house usage information at the top of the panel*/
    private int _infoBarH;
    /**the height of the column headings*/
    private int _listTitleH;
    /**the relative x co-ordinate of the remove buttons*/
    private int _remX;

    
    /**all of the appliance class names in the simulation universe*/
    private String[] applianceNames;
    /**all of the policy components class names in the simulation universe*/
    private String[] policyNames;
    /**all of the attached appliance names*/
    private String[] attached;
    /**all of the policy component names*/
    private String[] pols;
    /**all of the appliance classes in the universe*/
    private ArrayList<Class<? extends Appliance>> applianceClasses;
    /**all of the policy component classes in the universe*/
    private ArrayList<Class<? extends PolicyComponent>> policyClasses;
    /**all of the appliances attached to the house*/
    private ArrayList<Appliance> attachedApps;
    /**all of the policy components attached to the house*/
    private ArrayList<PolicyComponent> attachedPols;
    /**all of the ApplianceTimedEvents related to the appliances attached to the house*/
    private ArrayList<ApplianceTimedEvent> ates = new ArrayList<ApplianceTimedEvent>();
    /**all of the TimedEvents related to the policy components attached to the house*/
    private ArrayList<TimedEvent> pctes = new ArrayList<TimedEvent>();
    
    /**the index of the last selected appliance class from the appliance combo box*/
    private int lastAppListIndex = -1;
    /**the index of the last selected policy component class from the policy component combo box*/
    private int lastPolsListIndex = -1;
    /**a handle to the House Chart Panel view containing graphical usage information*/
    private HouseChartPanel ch;

    /**Creates a new HouseContextPanel instance with
     * supplied view parent, household, dimension, color.
     * 
     * @param v parent component of this HouseContextPanel
     * @param h Household represented by HouseContextPanel
     * @param dim dimension of the HouseContextPanel
     * @param bg background color of the HouseContextPanel
     */
    public HouseContextPanel(View v, HouseHold h, Dimension dim, Color bg) {
        super(dim, bg);
        this.setLayout(null);

        universe = SmartGridUniverse.getInstance();
        ch = new HouseChartPanel(v, h, dim, bg);

        populateApplianceComboBox();
        populatePolicyComboBox();
        setUpMetrics();

        parent = v;
        house = h;
    }

    /**Calculates the bounds of a list column on screen
     *
     * @param x the x position to use as a basis for the list co-ordinates
     * @return a Rectangle representing the bounds of where to draw the list on screen
     */
    private Rectangle setListBounds(int x) {

        return new Rectangle(x, _listY, _listW, _listH);

    }

    /**Adds a scroll pane to a list
     *
     * @param list the list to add a scroll pane to
     * @param scroll the scroll pane to add
     * @param x the x position to use as a basis for the list co-ordinates
     */
    private void setScroll(JList list, JScrollPane scroll, int x) {
        scroll = new JScrollPane();
        scroll.getViewport().add(list);
        scroll.setBounds(setListBounds(x));
        scroll.setVisible(true);
        this.add(scroll);
    }

    /**Sets the bounds of the list on screen
     *
     * @param jl the list to set bounds for
     * @param x the x position to use as a basis for the list co-ordinates
     */
    private void setListBounds(JList jl, int x) {
        jl.setBounds(x, _listY, _listW, _listH);
        jl.addListSelectionListener(this);
        Rectangle r = new Rectangle(1, 1, 1, 1);
        this.add(jl);
    }

    /**Set up the geometry for this view, including all the components
     * within it
     */
    private void setUpMetrics() {

        int w = this.dimension.width, h = this.dimension.height;

        _infoBarH = 20;
        _listTitleH = _infoBarH * 2;
        _listY = _infoBarH + (int) (_listTitleH * 1.25);
        _listH = h - (_listY + (_infoBarH * 4));

        int _listAndPadW = w / TOTAL_LISTS;

        _listPad = (_listAndPadW / 15);

        _listW = _listAndPadW - _listPad;

        // set the dynamic width of a button
        int buttonWidth = (int) (w * 0.06);

        // the remove house button set up
        removeHouse = new JButton("X");
        removeHouse.setBounds(w - (buttonWidth + (_listPad / 2)), _infoBarH + 5, buttonWidth, (int) (_infoBarH * 1.5));
        removeHouse.addActionListener(this);
        removeHouse.setToolTipText("Remove House");
        this.add(removeHouse);

        // the graph button set up
        graph = new JButton("DATA");
        _remX = ((int) (w * 0.95)) - (_listPad / 2);
        graph.setBounds(w - ((buttonWidth * 2) + (_listPad / 2)), _infoBarH + 5, buttonWidth, (int) (_infoBarH * 1.5));
        graph.addActionListener(this);
        graph.setToolTipText("View detailed usage information");
        this.add(graph);


        // set up each of the columns (lists)
        int x = _listPad / 2;
        attached = new String[0];
        _objNames[APPS] = new JList();
        _objNames[APPS].setBackground(Color.DARK_GRAY);
        //setListBounds(_objNames[APPS], x);
        _objNames[APPS].addListSelectionListener(this);
        _objNames[APPS].setCellRenderer(new ApplianceCellRenderer());
        /*
        _scrolls[APPS] = new JScrollPane();
        _scrolls[APPS].getViewport().add(_objNames[APPS]);
        _scrolls[APPS].setBounds(setListBounds(x));
        _scrolls[APPS].setVisible(true);
        this.add(_scrolls[APPS]);
         */

        // set up the headings for the columns
        _headings[APPS] = x;
        setScroll(_objNames[APPS], _scrolls[APPS], x);

        // the remove selected appliances button
        removeApps = new JButton("Remove Selected Appliances");
        removeApps.setBounds(x, _listY + _listH, _listW, 40);
        removeApps.addActionListener(this);
        this.add(removeApps);

        // build the timed event lists
        x += _listAndPadW;
        _eventLists[APPS] = new JList();
        setListBounds(_eventLists[APPS], x);
        _eventLists[APPS].setBackground(Color.DARK_GRAY);
        _eventLists[APPS].setCellRenderer(new EventCellRenderer());

        _headings[APPS + 2] = x;
        setScroll(_eventLists[APPS], _scrolls[APPS + 2], x);

        x += _listAndPadW;
        appList.addActionListener(this);
        int _appLW = (int) (_listW * 0.75);
        appList.setBounds(x - (_appLW + _listPad), _listY + _listH, _appLW, 40);
        this.add(appList);

        // set up the remove selected policy components button
        removePols = new JButton("Remove Selected Components");
        removePols.setBounds(x, _listY + _listH, _listW, 40);
        removePols.addActionListener(this);
        this.add(removePols);


        pols = new String[0];
        _objNames[POLS] = new JList();
        _objNames[POLS].setBackground(Color.DARK_GRAY);
        setListBounds(_objNames[POLS], x);
        _objNames[POLS].setCellRenderer(new PolCellRenderer());

        _headings[POLS] = x;
        setScroll(_objNames[POLS], _scrolls[POLS], x);

        // the timed event lists for the policy components
        x += _listAndPadW;
        _eventLists[POLS] = new JList();
        _eventLists[POLS].setBackground(Color.DARK_GRAY);
        setListBounds(_eventLists[POLS], x);
        _eventLists[POLS].setCellRenderer(new PolEventCellRenderer());

        _headings[POLS + 2] = x;
        setScroll(_eventLists[POLS], _scrolls[POLS + 2], x);

        polList.addActionListener(this);
        polList.setBounds((x + _listAndPadW) - (_appLW + _listPad), _listY + _listH, _appLW, 40);
        // polList.setBounds(x - (_appLW + _listPad),_listY + _listH,_appLW,40);
        this.add(polList);

    }

    /**Populates all the possible values in the Appliance
     * selection combo box
     *
     */
    private void populateApplianceComboBox() {
        // get all the classes
        Set<Class<? extends Appliance>> appliances = universe.getAllApplianceTypes();
        applianceNames = new String[appliances.size()];
        applianceClasses = new ArrayList<Class<? extends Appliance>>(appliances.size());
        int index = 0;
        // add each of these to the appliance combo box
        for (Iterator<Class<? extends Appliance>> i = appliances.iterator(); i.hasNext();) {
            Class<? extends Appliance> a = i.next();

            applianceNames[index] = a.getSimpleName();
            applianceClasses.add(index, a);
            ++index;
        }
        // set the new list up
        appList = new JComboBox(applianceNames);
    }

   /**Populates all the possible values in the Policy Component
     * selection combo box
     *
     */
    private void populatePolicyComboBox() {
        // get all the possible classes
        ArrayList<Class<? extends PolicyComponent>> comps = PolicyBank.getInstance().getAllPolicyComponents();
        policyNames = new String[comps.size()];
        policyClasses = new ArrayList<Class<? extends PolicyComponent>>(comps.size());
        // add each to the list
        for (int x = 0; x < comps.size(); x++) {
            Class<? extends PolicyComponent> p = comps.get(x);
            String s = p.toString();
            policyNames[x] = s.substring(s.lastIndexOf('.') + 1);
            policyClasses.add(x, p);

        }
        // set up the new list values
        polList = new JComboBox(policyNames);

    }

    /**Change the HouseHold that is being represented as the subject
     * of this HouseContextPanel
     *
     * @param h the house to set as the new subject
     */
    public void setHouse(HouseHold h) {
        house = h;


        listAppliances();
        listPolicyComponents();

        _eventLists[APPS].setListData(new String[0]);
        _eventLists[POLS].setListData(new String[0]);
    }

    /**Show all of the attached appliances of this house in their list
     */
    private void listAppliances() {
        attachedApps = house.getAllAppliances();
        int appsCount = attachedApps.size();
        // if the count has changed, update the list
        if (appsCount != attached.length) {
            // Populate app names list
            attached = new String[appsCount];
            for (int x = 0; x < attached.length; x++) {
                attached[x] = attachedApps.get(x).toString();
            }
            _objNames[APPS].setListData(attached);

        }
    }
    /**Show all of the attached policy components of this house in their list
     */
    private void listPolicyComponents() {
        // get the policy components attached to this house
        attachedPols = house.getPolicy();
        int polsCount = attachedPols.size();
        // if the count has changed, update the list
        if (polsCount != pols.length) {
            // Populate app names list
            pols = new String[polsCount];
            for (int x = 0; x < pols.length; x++) {
                pols[x] = attachedPols.get(x).toString();
            }
            _objNames[POLS].setListData(pols);

        }
    }
    /**Refreshes the ApplianceTimedEvents being shown in their list
     * to ensure the information being displayed is up to date
     */
    private void refreshAppEventList() {
        // check whether the displayed timed event has finished
        for (int x = 0; x < ates.size(); x++) {
            if (ates.get(x).hasFinished()) {
                ates.remove(x);// if so, remove it
            }
        }
        // get the new appliance timed events
        String[] _aTimedEvents = new String[ates.size()];
        for (int x = 0; x < _aTimedEvents.length; x++) {
            ApplianceTimedEvent a = ates.get(x);
            // set up the string representation of the timed event
            _aTimedEvents[x] = a.getStartTime().toShortString() + "-" + a.getEndTime().toShortString();
        }
        // update the list content
        _eventLists[APPS].setListData(_aTimedEvents);

    }
    /**Refreshes the TimedEvents (relating to policy components) being shown in their list
     * to ensure the information being displayed is up to date
     */
    private void refreshPolEventList() {
        // check if any have finished
        for (int x = 0; x < pctes.size(); x++) {
            if (pctes.get(x).hasFinished()) {
                pctes.remove(x); // remove them if so
            }
        }
        // update the information
        String[] _pTimedEvents = new String[pctes.size()];
        for (int x = 0; x < _pTimedEvents.length; x++) {
            TimedEvent te = pctes.get(x);
            // create the string representaton of the timed event
            _pTimedEvents[x] = te.getStartTime().toShortString() + "-" + te.getEndTime().toShortString();
        }
        // update the list with the new data
        _eventLists[POLS].setListData(_pTimedEvents);
    }

    public void drawPanel(Graphics g) {
        // make sure the timed event lists are up to date
        updateAppTimedEvents();
        updatePolTimedEvents();

        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, dimension.width, _infoBarH);

        g.setColor(Color.BLACK);

        // show the house consumption information across the top
        String info = "#" + house.getId() + "    ";
        // retrieve latest consumption information ready to show
        house._contextHouseConsumption(this);
        // go through the latest consumption information...
        for (IHeader header : _cp.getHeaders()) {
            // add a string representation of each one
            if (header == ConsumptionPackage.Header.CURRENT_STATE) {
                info += header + " : " + PowerState.intToStateString(_cp.getValue(header)) + "    ";
            } else {
                info += header + " : " + _cp.getValue(header) + "    ";
            }
        }
        //...display the latest consumption information
        g.setFont(infoBarFont);
        g.drawString(info, 5, _infoBarH - 5);


        g.setColor(Color.WHITE);
        Font oldFont = g.getFont();
        g.setFont(View.labelFont);

        // LIST HEADINGS
        int padding = _listY - 10;

        g.drawString("APPLIANCES", _headings[APPS], padding);
        g.drawString("TIMED EVENTS", _headings[APPS + 2], padding);
        g.drawString("POLICY", _headings[POLS], padding);
        g.drawString("TIMED EVENTS", _headings[POLS + 2], padding);

        // + COMPONENT LABELS
        g.drawString("+ APP :", appList.getX() - 60, appList.getY() + appList.getHeight() - 15);
        g.drawString("+ POL :", polList.getX() - 60, polList.getY() + polList.getHeight() - 15);

        g.setFont(oldFont);


        // ATTACHED APPLIANCES
        listAppliances();
        refreshAppEventList();

        // ATTACHED POLICY COMPONENTS
        listPolicyComponents();
        refreshPolEventList();

    }

    /**Shows the ApplianceTimedEvents for all of the Appliances
     * currently selected by the user
     */
    private void updateAppTimedEvents() {
        try {
            // get all the selected appliances
            int[] indices = _objNames[APPS].getSelectedIndices();
            ates = new ArrayList<ApplianceTimedEvent>();

            // get the associated timed events for those selected appliances
            for (int x = 0; x < indices.length; x++) {
                TreeSet<ApplianceTimedEvent> hours = attachedApps.get(indices[x]).getUsageHours();

                for (ApplianceTimedEvent ate : hours) {
                    ates.add(ate);

                }

            }
            // update list
            refreshAppEventList();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }
    /**Shows the TimedEvents for all of the PolicyComponents
     * currently selected by the user
     */
    public void updatePolTimedEvents() {

        try {
            // get all of the policy components currently selected
            int[] indices = _objNames[POLS].getSelectedIndices();
            pctes = new ArrayList<TimedEvent>();
            // get the timed events for each one
            ArrayList<PolicyComponent> polC = house.getPolicy();
            for (int x = 0; x < indices.length; x++) {
                Stack<TimedEvent> rules = new Stack<TimedEvent>();

                for (TimedEvent t1 : house.getTimedEventHandler().getCurrentPolicyTimedEvents(polC.get(indices[x]))) {
                    rules.push(t1);
                }
                // add the timed events so that they can all be displayed
                for (TimedEvent te : rules) {
                    pctes.add(te);

                }

            }
            // update event list display
            refreshPolEventList();

        } catch (Exception e) {
            System.out.println(e.toString());
        }


    }

    public void valueChanged(ListSelectionEvent event) {
        JList source = (JList) event.getSource();
        // if the user has selected appliances
        if (source == _objNames[APPS]) {
            updateAppTimedEvents(); // show their timed events
        // if the user has selected policy components
        } else if (source == _objNames[POLS]) {
            updatePolTimedEvents(); // show their timed events

        }
    }

    public void actionPerformed(ActionEvent event) {
        // if the user wants to add an appliance
        if (event.getSource() == appList) {
            try {
                // get the selected index
                int index = appList.getSelectedIndex();
                // get the selected appliance class that maps to the selected index
                Class<? extends Appliance> toAdd = applianceClasses.get(index);
                // create a request to add the appliance asynchronously to the house
                ThreadSafeControl.addAppliance(house, toAdd);
                // record the last selected index
                lastAppListIndex = index;
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        } else if (event.getSource() == polList) {
            try {
                // get the selected index
                int index = polList.getSelectedIndex();

                // get the policy component class that maps to this index
                Class<? extends PolicyComponent> toAdd = policyClasses.get(index);
                // create a request to add the policy component asynchronously to the house
                ThreadSafeControl.addPolicyComponent(house, toAdd);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
            // if the user wishes to remove this house
        } else if (event.getSource() == removeHouse) {
            // create a request to asynchronously remove this house from the simulation
            ThreadSafeControl.removeHouse(house.getAggregator(), house);
            // change the view back to the Grid Overview
            parent.changeView(View.v_OVERVIEW);

            // if the user wishes to remove appliances
        } else if (event.getSource() == removeApps) {
            // create a place to store the appliances to be removed
            ArrayList<Appliance> toRemove = new ArrayList<Appliance>();
            // get all of the selected appliances
            int[] indices = _objNames[APPS].getSelectedIndices();

            try {
                for (int x = 0; x < indices.length; x++) {
                    // add each of the appliance references
                    toRemove.add(attachedApps.get(indices[x]));
                }
                // create a request to asynchronously remove the appliances from the house
                ThreadSafeControl.removeAppliance(house, toRemove);
                _eventLists[APPS].setListData(new String[0]);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
            // if the user wishes to remove policy components from the hosue
        } else if (event.getSource() == removePols) {
            // a collection to hold those policy components to be removed
            ArrayList<PolicyComponent> toRemove = new ArrayList<PolicyComponent>();
            int[] indices = _objNames[POLS].getSelectedIndices();

            try {
                for (int x = 0; x < indices.length; x++) {
                    // add the policy component references to be removeed
                    toRemove.add(attachedPols.get(indices[x]));
                }
                // create a request to asynchronously remove the policy components from the house
                ThreadSafeControl.removePolicyComponent(house, toRemove);
                _eventLists[POLS].setListData(new String[0]);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        } else if (event.getSource() == graph) {
            //parent.setChartContext(house);
            parent.changeView(View.v_CHART);
        }
    }

    public void parseClick(int x, int y) {
    }

    /*********** CELL RENDERERS *****************/
    /**Used to render the Appliance list cells
     */
    private class ApplianceCellRenderer extends JLabel implements ListCellRenderer {

        private ApplianceCellRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            setText(value.toString());
            try {
                // rendered differently depending on whether the appliance is on or not
                if (attachedApps.get(index).isApplianceOn()) {
                    setForeground(Color.BLACK);
                    setBackground(Color.GREEN); // green for on
                } else {
                    setForeground(Color.WHITE);
                    setBackground(Color.RED); // red for off
                }

            } catch (Exception e) {
            }

            // allow for multiple selection and highlighting...
            if (isSelected) {
                setBackground(Color.BLUE);
                setForeground(Color.WHITE);
            }
            if (cellHasFocus) {
                setBackground(Color.BLUE);
                setForeground(Color.WHITE);
            }


            setText(value.toString());
            return this;
        }
    }

    /**Used to render the ApplianceTimedEvents list cells generated from selecting
     * Appliances
     */
    private class EventCellRenderer extends JLabel implements ListCellRenderer {

        private EventCellRenderer() {
            setOpaque(true);

        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {


            try {
                // render color is dependant upon current state of the timed event
                ApplianceTimedEvent ate = ates.get(index);
                TimedEvent.RunState state = ate.getCurrentState();

                if (state == TimedEvent.RunState.Running) {
                    setForeground(Color.BLACK);
                    setBackground(Color.GREEN); // green for running
                } else if (state == TimedEvent.RunState.Waiting) {
                    setForeground(Color.BLACK);
                    setBackground(Color.ORANGE); // amber for waiting
                } else if (state == TimedEvent.RunState.Finished) {
                    setForeground(Color.WHITE);
                    setBackground(Color.RED); // red for finished (before they are removed)
                }

            } catch (Exception e) {
                System.err.println(e.toString());
            }


            if (cellHasFocus) {
                setBackground(Color.BLUE);
                setForeground(Color.WHITE);
            }

            setText(value.toString());
            return this;
        }
    }

    /**Used to render the TimedEvents list cells generated from selecting
     * Policy Components
     */
    private class PolEventCellRenderer extends JLabel implements ListCellRenderer {

        private PolEventCellRenderer() {
            setOpaque(true);

        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {


            try {
                // render color is dependant upon current state of the timed event
                TimedEvent ate = pctes.get(index);
                TimedEvent.RunState state = ate.getCurrentState();


                if (state == TimedEvent.RunState.Running) {
                    setForeground(Color.BLACK);
                    setBackground(Color.GREEN); /// green for running
                } else if (state == TimedEvent.RunState.Waiting) {
                    setForeground(Color.BLACK);
                    setBackground(Color.ORANGE); // amber for waiting
                } else if (state == TimedEvent.RunState.Finished) {
                    setForeground(Color.WHITE);
                    setBackground(Color.RED); // red for finished (before they are removed)
                }

            } catch (Exception e) {
                System.err.println(e.toString());
            }


            if (cellHasFocus) {
                setBackground(Color.BLUE);
                setForeground(Color.WHITE);
            }

            setText(value.toString());
            return this;
        }
    }

    /**Used to render the Policy Components list cells
    */
    private class PolCellRenderer extends JLabel implements ListCellRenderer {

        private PolCellRenderer() {
            setOpaque(true);

        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            // uniform color for all policy components
            setBackground(Color.MAGENTA);
            setForeground(Color.BLACK);

            // allow for multiple selection (highlighting)
            if (isSelected) {
                setBackground(Color.BLUE);
                setForeground(Color.WHITE);
            }
            if (cellHasFocus) {
                setBackground(Color.BLUE);
                setForeground(Color.WHITE);
            }

            setText(value.toString());
            return this;
        }
    }
}