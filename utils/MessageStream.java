package utils;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Collection;
import java.io.*;
/**Class responsible for printing messages about the simulation during runtime.
 * Messages are displayed asynchronously at regular intervals to guard against
 * flooding the user with information at once. It is a Singleton class.
 *
 * @author Darius Hodaei
 */
public class MessageStream {
    /**Characteristic of the Singleton Pattern
     */
    private static MessageStream instance;
    /**a buffer of messages that have already been display, before they are written to file*/
    private Stack<String> oldMessages;
    /**messages waiting to be displayed*/
    private LinkedList<String> messageQueue;
    
     
    /**the output file path*/
    private String filePath = Config.OUTPUT_FILE_PATH;
    /**the initial message to display*/
    private String currentMessage = "Use 'GO' to start the simulation!";
   
    
    /**how many ticks of the system to display each message for*/
    private int duration = Config.MESSAGE_DURATION;
    /**how many ticks since message was last changed*/
    private int calls = 0;
    
    /**the priority of the message, HIGH will be inserted at the head of the message
     * queue, whereas NORMAL will be inserted at the back
     * 
     */
    public enum PRIORITY{NORMAL, HIGH};



    /**Private constructor - Characteristic of the Singleton Pattern
     */
    private MessageStream(){
        oldMessages = new Stack<String>();
        messageQueue = new LinkedList<String>();
        calls = 0;
        // if the file already exists, wipe it
        wipeFile();
    }

    /**Characteristic of the Singleton Pattern
     *
     * @return The Singleton MessageStream instance
     */
    public static MessageStream getInstance(){
        if(instance == null) instance = new MessageStream();
        return instance;

    }

    /**Sets how long a message is displayed for on the output stream
     * before printing a new one
     *
     * @param duration The time each message should show for
     */
    public void setMessageDuration(int duration){this.duration = duration;}

    /**Called by objects externally when they want to submit a message to
     * the messageQueue for displaying asynchronously to the user
     *
     * @param message The message to be queued for display
     */

    public void submitMessage(String message, PRIORITY messageP){

        if(messageP == PRIORITY.HIGH) messageQueue.add(0, message);
        else messageQueue.add(message);

       
    }

    /**Returns the current message that is being displayed
     *
     * @return The current message being displayed to the user
     */
    public String getCurrentMessage(){return currentMessage;}

    /**Takes the message at the head of the queue and prints it to the
     * output stream.
     *
     * NOTE: This method should check whether the current
     * message has been displayed for messageDuration before printing a
     * new one, if not then just return from the method without updating
     * currentMessage
     */
    public void updateMessage(){
        ++calls;


        // check whether its time to change the message displayed or not
        if(calls >= duration){
            // put the current message in to the history
            oldMessages.add(currentMessage);
            // if we have reached the limit of old messages
            if(oldMessages.size() >= Config.OLD_MESSAGES_LIMIT){
                // we write them to the text file - this is buffered so it
                // only happens every so often for efficiency reasons
                writeMessagesToFile(oldMessages);
                // clear the old messages
                oldMessages = new Stack<String>();
            }
            // if we still have messages to display
            if(!messageQueue.isEmpty()){
                // get the head of the queue and display it
                currentMessage = messageQueue.removeFirst();
                calls = 0;
            }
        }
    }

    /** Called at the end of the simulation run to flush any remaining
     * messages left in the queue to the log file
     */
    public void flush(){

       writeMessagesToFile(oldMessages);
       messageQueue.addFirst(currentMessage);
       writeMessagesToFile(messageQueue);
    }

    /**Writes the message to the persistent log file so that we have a full
     * print out of the system data after the simulation finishes
     *
     * @param messages the messages to print to the output file
     */
    private void writeMessagesToFile(Collection<String> messages){
        try{
           FileWriter outFile = new FileWriter(filePath, true);
           PrintWriter out = new PrintWriter(outFile);
            
            for(String s : messages){
                out.println(s);
            }

            out.flush();
            out.close();

        }
        catch(Exception e){
            System.err.println(e.toString());
        }
        
    }

    /**Deletes the contents of the output file
     *
     */
    private void wipeFile(){
        try{
           FileWriter outFile = new FileWriter(filePath);
           PrintWriter out = new PrintWriter(outFile);

            out.write("");


            out.flush();
            out.close();

        }
        catch(Exception e){
            System.err.println(e.toString());
        }

    }
}
