package security;
import infopackage.InfoPackage;
import infopackage.IHeader;

/**A simple low-power consumption shift-cipher that works by shifting
 * each character of the input data by a given base. This is an example
 * of an IEncryptor implementation
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
public class ROTEncryptor implements IEncryptor{

    /**Returns the InfoPackage encrypted using the given modulus as a
     * parameter for a specific algorithm
     *
     * @param data The InfoPackage to encrypt
     * @param modulus The modulus under which the data is encrypted
     * @return The encrypted InfoPackage data
     */
    public InfoPackage encryptData(InfoPackage data, int modulus){
        /*NOTE: Only the value is encrypted, not the header. This is to
         * make the encryption more resillient to attack, because if the
         * header name was encrypted too, then once the attacker knows the
         * header name they can use brute force until they uncover this header
         * and therefore uncover the private key. Thus we only encrypt the value.
         */
        // For every header in the InfoPackage
        for(IHeader header : data.getHeaders()){
            // set the new shifted value to the header
           data.setValue(header, data.getValue(header) + modulus);
        }
        // Return the fully encrypted data
        return data;

    }

    /**Returns the InfoPackage decrypted using the given modulus as a
     * parameter for a specific algorithm
     *
     * @param data The InfoPackage to decrypt
     * @param modulus The modulus under which the data is decrypted
     * @return The decrypted InfoPackage data
     */
    public InfoPackage decryptData(InfoPackage data, int modulus){
        // Decryption uses the same algorithm as encryption but in reverse (-1 *)
        return encryptData(data, (-1 * modulus));

    }
}