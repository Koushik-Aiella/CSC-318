import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;

/**
 * The puzzleCreator class creates puzzle objects
 * which then encrypts it using findKey method.
 * @author Koushik aiella(984161)
 * @version 1.0
 * @since 30-Nov-2020
 */
public class PuzzleCreator {

    /**
     * The Puzzles list.
     */
    public ArrayList<Puzzle> puzzlesList = new ArrayList<Puzzle>();

    /**
     * Instantiates a new Puzzle creator.
     */
    public PuzzleCreator() {

    }

    /**
     * Create puzzles array list which creates 4096 puzzle objects.
     *
     * @return array list
     */
    public ArrayList<Puzzle> createPuzzles() {
        // creates 4096 puzzle objects
        for (int i = 1; i <= 4096; i++) {

            try {
                // using CryptoLib library we generate randomKeys which are then assigned
                // to secretKey object then the objects are added to the puzzles list.
                SecretKey secretKey = CryptoLib.createKey(createRandomKey());
                Puzzle puzzle = new Puzzle(i, secretKey);
                puzzlesList.add(puzzle);

            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }


        }
        return puzzlesList;

    }

    /**
     * Creates a random key byte [ ] array which generates randomKeys.
     *
     * @return secretEndcoded byte [ ]
     */
    public byte[] createRandomKey() {

        try {
            // using Keygenerator we generate random DES keys and then convert them in to
            //bytes in which the last 8 bytes are zeros
            KeyGenerator keyGen = KeyGenerator.getInstance("DES");
            keyGen.init(56);
            SecretKey key = keyGen.generateKey();
            String encoded = Base64.getEncoder().encodeToString(key.getEncoded());
            byte[] secretEncodedByte = Base64.getDecoder().decode(encoded);
            for (int i = 0; i < 8; i++) {
                secretEncodedByte[i] = 0;
            }
            return secretEncodedByte;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Encrypts the puzzle bytes[] using desired keyByte.
     * @param keyByte the key byte
     * @param puzzle  the puzzle
     * @return byte [ ]
     */
    public byte[] encryptPuzzle(byte[] keyByte, Puzzle puzzle) {
        // secretkey object
        SecretKey secretKey = new SecretKeySpec(keyByte, 0, keyByte.length, "DES");
        try {
            // Using the cipher object we encrypt the secretKey which then later used
            // to encryptPuzzles
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedByte = cipher.doFinal(puzzle.getPuzzleAsBytes());
            return encryptedByte;
            // catch statements to identity any errors that has been caught during the process.
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Find key secret key by looping through all the puzzles that has been created using createPuzzles.
     * @param puzzleNumber the puzzle number
     * @return secret key
     */
    public SecretKey findKey(int puzzleNumber) {
        // A loop which loops through arraylist to find desired keys.
        for (int i = 0; i <= puzzlesList.size(); i++) {
            Puzzle puzzle = puzzlesList.get(i);
            if (puzzleNumber == puzzle.getPuzzleNumber()) {
                return puzzle.getKey();
            }
        }
        return null;
    }

    /**
     * Encrypt puzzles to file using a desired fileName.
     * @param fileName the file name
     */
    public void encryptPuzzlesToFile(String fileName) {
        // We have to create a fileOutput stream to write the puzzle objects to file.
        File file;
        FileOutputStream outputStream = null;
        try {
            // here we assign the filename and the desired location we want the file output to.
            file = new File(fileName);
            outputStream = new FileOutputStream(file);
            // encrypts puzzle objects by looping through the arraylist of puzzle objects.
            for (Puzzle puzzles : puzzlesList) {
                SecretKey keyOfPuzzle = findKey(puzzles.getPuzzleNumber());

                byte[] puzzleEncrypted = encryptPuzzle(keyOfPuzzle.getEncoded(), puzzles);

                if (!file.exists()) {
                    file.createNewFile();
                }
                outputStream.write(puzzleEncrypted);
                outputStream.flush();

            }

            // catch an exception if file not found
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // ouputStream closes if the file is not null
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}


