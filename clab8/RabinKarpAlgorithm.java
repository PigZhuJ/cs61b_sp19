public class RabinKarpAlgorithm {

    /**
     * This algorithm returns the starting index of the matching substring.
     * This method will return -1 if no matching substring is found, or if the input is invalid.
     */
    public static int rabinKarp(String input, String pattern) {
        int n = input.length();
        int p = pattern.length();
        if (p > n) {
            return -1;
        }

        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < p; i += 1) {
            strBuilder.append(input.charAt(i));
        }
        RollingString rollInput = new RollingString(strBuilder.toString(), p);
        RollingString rollPattern = new RollingString(pattern, p);

        int inputHash = rollInput.hashCode();
        int patternHash = rollPattern.hashCode();
        for (int s = 0; s <= n - p; s += 1) {
            if (inputHash == patternHash) {
                if (rollInput.equals(rollPattern)) {
                    return s;
                }
            }
            if (s < n - p) {
                rollInput.addChar(input.charAt(s + p));
                inputHash = rollInput.hashCode();
            }
        }
        return -1;
    }

}
