package security;
import infopackage.InfoPackage;

/**Interface implemented by encryption functions that use a modulus as
 * the basis of their encryption of data
 *
 * @author Darius Hodaei <k1183257@kcl.ac.uk>
 */
 interface IEncryptor {

    /**Returns the InfoPackage encrypted using the given modulus as a
     * parameter for a specific algorithm
     *
     * @param data The InfoPackage to encrypt
     * @param modulus The modulus under which the data is encrypted
     * @return The encrypted InfoPackage data
     */
    InfoPackage encryptData(InfoPackage data, int modulus);

    /**Returns the InfoPackage decrypted using the given modulus as a
     * parameter for a specific algorithm
     *
     * @param data The InfoPackage to decrypt
     * @param modulus The modulus under which the data is decrypted
     * @return The decrypted InfoPackage data
     */
    InfoPackage decryptData(InfoPackage data, int modulus);
}
