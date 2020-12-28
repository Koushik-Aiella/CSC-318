import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author Koushik Aiella(984161)
 * @version 1.0
 * @since 30-nov-2020
 */
public class Puzzle {

    /**
     * The Puzzle number.
     */
    public int puzzleNumber;
    /**
     * The Secret key.
     */
    public SecretKey secretKey;

    /**
     * Instantiates a new Puzzle.
     *
     * @param puzzleNumber the puzzle number
     * @param secretKey    the secret key
     */
    public Puzzle(int puzzleNumber, SecretKey secretKey) {
        this.puzzleNumber = puzzleNumber;
        this.secretKey = secretKey;
    }

    /**
     * Gets puzzle number.
     *
     * @return the puzzle number
     */
    public int getPuzzleNumber() {
        return puzzleNumber;
    }

    /**
     * Gets key.
     *
     * @return the key
     */
    public SecretKey getKey() {
        return secretKey;
    }

    /**
     * Get puzzle as bytes byte [ ].
     *
     * @return byte [ ]
     */
    public byte[] getPuzzleAsBytes() {
        byte[] bytePuzzle;

        // A byte array with size 16
        byte[] zeroBitByteArray = new byte[16];

        // A byte array that that is assigned to puzzleNum
        byte[] puzzleByteArray = CryptoLib.smallIntToByteArray(puzzleNumber);

        // A byte array that is assigned to secretKey
        byte[] secretKeyBytes = secretKey.getEncoded();

        // combining of two byte arrays in to one
        byte[] combinedByte = new byte[zeroBitByteArray.length + puzzleByteArray.length + secretKeyBytes.length];
        ByteBuffer byteBuffer = ByteBuffer.wrap(combinedByte);
        byteBuffer.put(zeroBitByteArray);
        byteBuffer.put(puzzleByteArray);
        byteBuffer.put(secretKeyBytes);
        bytePuzzle = byteBuffer.array();
        return bytePuzzle;

    }


}
