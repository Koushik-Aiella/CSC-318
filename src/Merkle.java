import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * The type Merkle.
 * @author Koushik Aiella(984161)
 * @version 1.0
 * @since 30-nov-2020
 */
public class Merkle {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        // creates puzzles of 4096 objects and then encrypts them to a file.
        PuzzleCreator puzzleCreator = new PuzzleCreator();
        puzzleCreator.createPuzzles();
        puzzleCreator.encryptPuzzlesToFile("Puzzles.bin");

        // we then crack the puzzles using the crack method in PuzzleCracker class.
        PuzzleCracker bobCrack = new PuzzleCracker("Puzzles.bin");
        Puzzle bobPuzzle = bobCrack.crack(3);

        // assignment of puzzleNumber of bob.
        int puzzleNumberOfBob = bobPuzzle.getPuzzleNumber();

        // encryption of similar puzzle objects found using the keys of alice and bob
        SecretKey keyOfAlice = puzzleCreator.findKey(puzzleNumberOfBob);
        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, keyOfAlice);
            String message = "Testing Merkles Puzzles!";
            byte[] messageByte = message.getBytes();
            byte[] encryptedByte = cipher.doFinal(messageByte);
            bobCrack.decryptMessage(encryptedByte);


        // catches exceptions if there are any errors during compilation of code.
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

    }

}

