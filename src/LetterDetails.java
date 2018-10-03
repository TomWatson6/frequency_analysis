public class LetterDetails implements Comparable<LetterDetails> {

    private char letter;
    private int frequency;

    public LetterDetails(char letter, int frequency) {

        this.letter = letter;
        this.frequency = frequency;

    }

    public char getLetter() {
        return letter;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void incrementFrequency() {
        frequency++;
    }

    @Override
    public String toString() {

        return letter + " = " + frequency;

    }

    public int compareTo(LetterDetails details) {

        return details.frequency >= frequency ? 1 : -1;

    }

}
