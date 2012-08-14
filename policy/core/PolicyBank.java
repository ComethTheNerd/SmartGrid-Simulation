package policy.core;
import policy.compositions.*;
import household.HouseHold;
import java.lang.reflect.Constructor;
import java.security.SecureClassLoader;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Config;
import API.RegisterPolicies;
import policy.BigDinner;
import policy.ChilledWeekend;
import policy.CookedLunch;
import policy.EarlySwimmer;
import policy.Environmentalist;
import policy.GreedyDinner;
import policy.HealthyMorning;
import policy.HouseCleaning;
import policy.IronClothes;
import policy.LateSwimmer;
import policy.OnlineGaming;
import policy.RandomDay;
import policy.Recession;
import policy.SummerAirPolicy;
import policy.SundayRoast;
import policy.WarmWeekPolicy;
import policy.WarmWeekendPolicy;
import policy.WatchMovie;
import policy.WorkingWeekPolicy;

/**A Singleton object that stores all classes that implement IPolicy,
 * and therefore can be used as a policy for a HouseHold.
 *
 * @author Tokoni Kemenanabo
 */
public class PolicyBank {

    /**ArrayList storing the Class of registered PolicyComponent subclasses*/
    private ArrayList<Class<? extends PolicyComponent>> components = new ArrayList<Class<? extends PolicyComponent>>();

    /**Used in loading classes into the components ArrayList*/
    private SecureClassLoader cl = (SecureClassLoader) SecureClassLoader.getSystemClassLoader();

    /**Singleton PolicyBank instance*/
    private static PolicyBank instance;
    
    /**PRIVATE CONSTRUCTOR. Characteristic of the Singleton Pattern.
     * To be called once at creation of singleton instance.
     */
    private PolicyBank(){
        components = new ArrayList<Class<? extends PolicyComponent>>();
        loadPolicies();

        /* API POLICY LOADING             */
        RegisterPolicies rh = new RegisterPolicies();
        //Register newly created policies here
        rh.registerApiPolicies(this);
        /**********************************/
    }

    /**Registers (adds) the supplied policy component class to the PolicyBank.
     *
     * @param com the policy component class to be added to the bank
     */
    public void registerPolicyComponent(Class<? extends PolicyComponent> com){

        // add this to your collection if not already present
        if(!components.contains(com)) components.add(com);
          
    }

    /**Characteristic of the Singleton Pattern.
     * Returns the singleton instance of PolicyBank.
     * 
     * @return A Singleton PolicyBank instance
     */
    public static PolicyBank getInstance(){
        if(instance == null) instance = new PolicyBank();
        return instance;
    }

    /**Loads all predefined policies to the PolicyBank
     * This is done at creation of the PolicyBank singleton instance
     */
    private void loadPolicies(){
        components = new ArrayList<Class<? extends PolicyComponent>>();
        components.add(BigDinner.class);
        components.add(CookedLunch.class);
        components.add(EarlySwimmer.class);
        components.add(Environmentalist.class);
        components.add(HealthyMorning.class);
        components.add(HouseCleaning.class);
        components.add(IronClothes.class);
        components.add(LateSwimmer.class);
        components.add(OnlineGaming.class);
        components.add(RandomDay.class);
        components.add(ChilledWeekend.class);
        components.add(GreedyDinner.class);
        components.add(SummerAirPolicy.class);
        components.add(SundayRoast.class);
        components.add(WarmWeekendPolicy.class);
        components.add(WarmWeekPolicy.class);
        components.add(WatchMovie.class);
        components.add(WorkingWeekPolicy.class);
        components.add(Recession.class);

        /*COMPOSITIONS - added 21st March */
        components.add(AverageFamily.class);
        components.add(Bachelor.class);
        components.add(Teenager.class);
        components.add(HealthEnthusiast.class);
        /**********************************/

    }

    /**Returns all policy components stored in the PolicyBank
     *
     * @return An ArrayList of all policy components in policy bank
     */
    public ArrayList<Class<? extends PolicyComponent>> getAllPolicyComponents(){return components;}

    /**Uses the Class loader to load a class represented by className.
     * Class Must be in the policy package.
     *
     * @param className
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public void find(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException{

        //add policy. to the front of the className
        //E.g WorkingWeekPolicy --> policy.WorkingWeekPolicy
        String cname = "policy."+className;

        //load class ; throws classnotfound exception if no such class
        Class z = cl.loadClass(cname);
        //add class to bank
        registerPolicyComponent(z);
        
    }

    /**Returns an ArrayList of randomly selected policies from the policy bank.
     * These selected policies are then attached to the specified HouseHold.
     *
     * @param house the HouseHold requesting the policies
     * @return An ArrayList of randomly selected PolicyComponents
     */
    public ArrayList<PolicyComponent> getRandomPolicy(HouseHold house){

        //total number of policies in the policy bank
        int comSize = components.size();
        //maximum amount of policy components to be selected
        int maxLimit = Config.POLS_MAX_PER_HOUSE;
        //choose random from 0 - (prob-1) , if 0 select else reject. Probability of selection = 0.33
        int prob = 3;
        //random
        Random r = new Random();
        //ArayList of policy components to be returned
        ArrayList<PolicyComponent> policyList = new ArrayList<PolicyComponent>();

        //go through components list while making sure the selected
        //policies is not more than the maxLimit
        for(int i = 0; i < comSize && policyList.size()< maxLimit; i++){

            try{
                //Create new constructor for PolicyComponent with one HouseHold argument
                Constructor x = components.get(i).getDeclaredConstructor(HouseHold.class);

                //if selection index (0 = selection index)
                if(r.nextInt(prob)< 1){
                    //Use constructor to create new Policy Component
                    //add Policy Component to policy list to be returned to house
                    policyList.add((PolicyComponent) x.newInstance(house));
                }

            }
            //instantiation error are mainly caught here
            //When no suitable constructor is found
            //Also when instantiating an abstract class and such
            catch(Exception e){
                try {
                    throw e; // throw exception
                } catch (Exception ex) {
                    Logger.getLogger(PolicyBank.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        //Unfindable bug.. Adds random PolicyComponents to the bank here
        //Must remove this if the components size mysteriously increases
        if(components.size()>comSize)
            components.remove(components.size()-1);

        return policyList;         
    }



}
