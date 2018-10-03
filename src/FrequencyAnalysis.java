import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FrequencyAnalysis {

    public static final int STARTING_LETTER = 65;
    public static final int CASE_DIFFERENCE = 32;
    public static final int STARTING_LETTER_UPPER = STARTING_LETTER + CASE_DIFFERENCE;
    public static final int LETTER_COUNT = 26;
    public static final String FILE_PATH = "./textFile1.txt";
    public static final String FILE_TO_DECRYPT = "./textFileToDecrypt.txt";
    public static final String DECRYPTED_FILE_V1 = "./decryptedTextFile.txt";
    public static final String DECRYPTED_FILE_V2 = "./decryptedTextFile2.txt";

    private static List<LetterDetails> details;
    private static List<LetterDetails> encryptedDetails;

    public static void main(String[] args) {

        int mostCommonShiftCount = 0;
        int mostCommonShift = 0;

        details = new ArrayList<>();

        //Initialise details list to store the frequency of each character
        for(int i = 0; i < LETTER_COUNT; i++) {

            details.add(new LetterDetails((char) (STARTING_LETTER + CASE_DIFFERENCE + i), 0));

        }

        //Make a variable to store the encrypted letter details
        encryptedDetails = new ArrayList<>();

        for(int i = 0; i < LETTER_COUNT; i++) {
            encryptedDetails.add(new LetterDetails((char) (STARTING_LETTER + CASE_DIFFERENCE + i), 0));
        }

        //Declare a list to store the shift between each of the letters in details and encryptedDetails lists
        List<Integer> shifts = new ArrayList<>();

        //Initialise the list as 0 for every element and makes it's size equal to the size of the alphabet
        for(int i = 0; i < LETTER_COUNT * 2; i++) {
            shifts.add(0);
        }

        //Make the file reader and buffered reader to read in the lines from the text file
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        FileWriter fileWriter = null;
        PrintWriter printWriter = null;

        try {

            //Initialise the file reader and buffered reader to read in the lines from the text file
            fileReader = new FileReader(FILE_PATH);
            bufferedReader = new BufferedReader(fileReader);

            //Make a current line variable so that each line can be read from the file
            String currentLine;

            //Make a current letter variable so that each letter can be read from the current line
            char currentLetter;

            //Loop through the file reading it line by line until the end of the file
            while((currentLine = bufferedReader.readLine()) != null) {

                //Loop through the letters of each line and tally the letters in the details list
                for (int i = 0; i < currentLine.length(); i++) {

                    currentLetter = currentLine.charAt(i);

                    if (Character.isLetter(currentLetter)) {
                        if ((int) currentLetter >= (STARTING_LETTER + CASE_DIFFERENCE) && ((int) currentLetter < STARTING_LETTER + CASE_DIFFERENCE + LETTER_COUNT)) {
                            details.get((int) currentLetter - STARTING_LETTER - CASE_DIFFERENCE).incrementFrequency();
                        } else if((int) currentLetter >= STARTING_LETTER && ((int) currentLetter < STARTING_LETTER + LETTER_COUNT)){
                            details.get((int) currentLetter - STARTING_LETTER).incrementFrequency();
                        }
                    }

                }

            }

            //Todo -- Read the encrypted file here

            //Initialise the file reader and buffered reader to read in the lines from the text file
            fileReader = new FileReader(FILE_TO_DECRYPT);
            bufferedReader = new BufferedReader(fileReader);

            //Loop through the file reading it line by line until the end of the file
            while((currentLine = bufferedReader.readLine()) != null) {

                //Loop through the letters of each line and tally the letters in the details list
                for (int i = 0; i < currentLine.length(); i++) {

                    currentLetter = currentLine.charAt(i);

                    if (Character.isLetter(currentLetter)) {
                        if ((int) currentLetter >= (STARTING_LETTER + CASE_DIFFERENCE)) {
                            encryptedDetails.get((int) currentLetter - STARTING_LETTER - CASE_DIFFERENCE).incrementFrequency();
                        } else {
                            encryptedDetails.get((int) currentLetter - STARTING_LETTER).incrementFrequency();
                        }
                    }



                }

            }

            //Todo -- Attempt to decrypt the file using frequency analysis here

            Collections.sort(details);
            Collections.sort(encryptedDetails);

            //Initialise the file reader and buffered reader to read in the lines from the text file
            fileReader = new FileReader(FILE_TO_DECRYPT);
            bufferedReader = new BufferedReader(fileReader);

            //Initialise the file writer and print writer to write the decrypted text to the new file
            fileWriter = new FileWriter(DECRYPTED_FILE_V1, false);
            printWriter = new PrintWriter(fileWriter);

            //Make a line to write to the new file
            String lineToWrite = "";

            while((currentLine = bufferedReader.readLine()) != null) {

                //Loop through the letters of each line and tally the letters in the details list
                for (int i = 0; i < currentLine.length(); i++) {

                    currentLetter = currentLine.charAt(i);

                    if (Character.isLetter(currentLetter)) {

                        //Find the index of the encrypted details that the letter is of the list
                        int index = getIndex(encryptedDetails, currentLetter);

                        //Find the corresponding letter in the details list for that letter index
                        lineToWrite = lineToWrite + details.get(index).getLetter();

                    } else {

                        lineToWrite = lineToWrite + currentLetter;

                    }

                }

                //Write the new line to the decrypted file
                printWriter.printf("%s" + "%n", lineToWrite);

                //Reset the line for the next iteration of the line reader
                lineToWrite = "";

            }

            //Todo -- Calculate the shift of the Caeser Cipher here

            //Loop through the entirety of the lists of letter details and calculate the shifts for each letter with similar proportions of frequencies
            for(int i = 0; i < LETTER_COUNT; i++) {

                //Get the index by finding the difference between the current letters then adding 26 due to the list accounting for numbers down to negative 26
                //Increment the shift that already exists in that position by 1
                shifts.set((int) details.get(i).getLetter() - (int) encryptedDetails.get(i).getLetter() + 26,
                        shifts.get((int) details.get(i).getLetter() - (int) encryptedDetails.get(i).getLetter() + 26) + 1);

            }

            //Todo -- Decrypt using the correct shift here

            for(int i = 0; i < shifts.size(); i++) {
                if(mostCommonShiftCount < shifts.get(i)) {
                    mostCommonShiftCount = shifts.get(i);
                    mostCommonShift = i - LETTER_COUNT;
                }
            }

            //Reassign the buffered reader and file reader to the encrpted text so that it can be read in
            fileReader = new FileReader(FILE_TO_DECRYPT);
            bufferedReader = new BufferedReader(fileReader);

            //These are assigned to the file to write to, so that the decrypted text can be written to this file
            fileWriter = new FileWriter(DECRYPTED_FILE_V2);
            printWriter = new PrintWriter(fileWriter, false);

            //This is a variable to store the current line to write to the new file
            lineToWrite = "";

            while((currentLine = bufferedReader.readLine()) != null) {

                for(int i = 0; i < currentLine.length(); i++) {

                    currentLetter = currentLine.charAt(i);

                    if(Character.isLetter(currentLetter)) {
                        if(Character.isUpperCase(currentLetter)) {
                            lineToWrite = lineToWrite + ((char) ((((((int) currentLetter) % STARTING_LETTER) +
                                    mostCommonShift) % LETTER_COUNT) + STARTING_LETTER));
                        } else {
                            if((((((int) currentLetter) % STARTING_LETTER_UPPER) +
                                    mostCommonShift)) >= 0) {
                                lineToWrite = lineToWrite + ((char) ((((((int) currentLetter) % STARTING_LETTER_UPPER) +
                                        mostCommonShift) % LETTER_COUNT) + STARTING_LETTER_UPPER));
                            } else {
                                lineToWrite = lineToWrite + ((char) ((((((int) currentLetter) % STARTING_LETTER_UPPER) +
                                        mostCommonShift) + STARTING_LETTER_UPPER) + LETTER_COUNT));
                            }
                        }
                    } else {
                        lineToWrite = lineToWrite + currentLetter;
                    }

                }

                printWriter.printf("%s\n", lineToWrite);
                lineToWrite = "";

            }


        }
        catch(IOException ioe) {

            ioe.printStackTrace();

        }
        finally {

            try {

                if(bufferedReader != null) bufferedReader.close();
                if(fileReader != null) fileReader.close();
                if(fileWriter != null) fileWriter.close();
                if(printWriter != null) printWriter.close();

            }
            catch (IOException ex) {

                ex.printStackTrace();

            }

        }

        System.out.println();
        System.out.println("---- Frequencies ----");
        System.out.println();

        for(int i = 0; i < details.size(); i++) {
            System.out.println(details.get(i) + " | " + encryptedDetails.get(i));
        }

        System.out.println();
        System.out.println("---- Shifts ----");
        System.out.println();

        for(int i = -LETTER_COUNT; i < LETTER_COUNT; i++) {
            System.out.println("Shift of " + i + " = " + shifts.get(i + LETTER_COUNT));
        }

        System.out.println("The most common shift: " + mostCommonShift);

    }

    public static int getIndex(List<LetterDetails> letters, char character) {

        //Declare a variable to hold the index that is found
        int index = -1;

        //Loop through the letters list to find which index equals the character passed in
        for(int i = 0; i < letters.size(); i++) {

            if(letters.get(i).getLetter() == Character.toLowerCase(character)) {
                index = i;
                break;
            }

        }

        return index;

    }

}
