package security;
import infopackage.InfoPackage;

/**Provides a common encryption function entry point for the
 * whole model which ensures that all components will be using
 * the same encryption function, thus preserving the cryptographic
 * communication channel.
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class EncryptionFunction {

    /**Characteristic of the Singleton Design Pattern
     */
    private static EncryptionFunction instance;
    /**An instance of an IEncryptor that will serve as the encryption
     * function for the whole model
     */
    private IEncryptor method = new ROTEncryptor();

    /**Characteristic of the Singleton Design Pattern
     */
    private EncryptionFunction() {
    }

    /**Retrieves the same instance of EncryptionFunction each time,
     * ensuring that only one is ever created during a simulation
     *
     * @return the single global instance of EncryptionFunction
     */
    public static EncryptionFunction getInstance() {

        if (instance == null) {
            instance = new EncryptionFunction();
        }

        return instance;
    }

    /**Returns the InfoPackage encrypted using the given modulus as a
     * parameter for a specific algorithm
     *
     * @param data The InfoPackage to encrypt
     * @param modulus The modulus under which the data is encrypted
     * @return The encrypted InfoPackage data
     */
    public InfoPackage encryptData(InfoPackage data, int modulus) {
        // we pass the processing to the currently associated encryption method
        return method.encryptData(data, modulus);
    }

    /**Returns the InfoPackage decrypted using the given modulus as a
     * parameter for a specific algorithm
     *
     * @param data The InfoPackage to decrypt
     * @param modulus The modulus under which the data is decrypted
     * @return The decrypted InfoPackage data
     */
    public InfoPackage decryptData(InfoPackage data, int modulus) {
        // we pass the processing to the currently associated encryption method
        return method.decryptData(data, modulus);
    }
}
