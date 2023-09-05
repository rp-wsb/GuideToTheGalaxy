import java.util.*;

public class Main {
    private static final Map<String, String> galacticToRoman = new HashMap<>();
    private static final Map<String, Double> metalPrices = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> inputLines = new ArrayList<>();
        List<String> questions = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.trim().isEmpty()) {
                break;
            }
            if (line.endsWith("?")) {
                questions.add(line);
            } else {
                inputLines.add(line);
            }
        }

        processInput(inputLines);

        for (String question : questions) {
            query(question);
        }

        scanner.close();
    }

    private static void processInput(List<String> inputLines) {
        for (String line : inputLines) {
            String[] parts = line.split("\\s+");
            if (parts.length == 3 && parts[1].equals("is")) {
                String galacticUnit = parts[0];
                String romanNumber = parts[2];
                galacticToRoman.put(galacticUnit, romanNumber);
            } else if (parts.length >= 5 && parts[parts.length - 1].equals("Credits") && parts[parts.length - 2].equals("is")) {
                double metalPrice = Double.parseDouble(parts[parts.length - 3]);
                double value = calculateValue(parts, 0, parts.length - 5);
                double pricePerUnit = metalPrice / value;
                String metalName = parts[parts.length - 4];
                metalPrices.put(metalName, pricePerUnit);
            } else {
                System.out.println("Invalid conversion: " + line);
            }
        }
    }

    private static void query(String question) {
        if (question.startsWith("how much is")) {
            String[] parts = question.split("\\s+");
            double value = calculateValue(parts, 3, parts.length - 2);
            if (value != -1) {
                System.out.println(question.replace(" ?", "") + " is " + (int) value);
            } else {
                System.out.println("I have no idea what you are talking about1");
            }
        } else if (question.startsWith("how many Credits is")) {
            String[] parts = question.split("\\s+");
            String metalName = parts[parts.length - 2];
            double value = calculateValue(parts, 4, parts.length - 3);
            Double metalPrice = metalPrices.get(metalName);
            if (metalPrice == null || value == -1) {
                System.out.println("I have no idea what you are talking about2");
                return;
            }
            double totalValue = value * metalPrice;
            System.out.println(question.replace(" ?", "") + " is " + (int) totalValue + " Credits");
        } else {
            System.out.println("I have no idea what you are talking about3");
        }
    }

    private static double calculateValue(String[] parts, int start, int end) {
        double value = 0;
        StringBuilder unit = new StringBuilder();
        for (int i = start; i <= end; i++) {
            if (galacticToRoman.containsKey(parts[i])) {
                unit.append(galacticToRoman.get(parts[i]));
            } else {
                return -1;
            }
        }
        try {
            value = getRomanNumberValue(unit.toString());
        } catch (NumberFormatException e) {
            return -1;
        }
        return value;
    }

    private static int getRomanNumberValue(String romanNumber) {
        int value = 0;
        for (int i = 0; i < romanNumber.length(); i++) {
            int current = romanToDecimal(romanNumber.charAt(i));
            int next = (i + 1 < romanNumber.length()) ? romanToDecimal(romanNumber.charAt(i + 1)) : 0;
            if (current >= next || next == 0) {
                value += current;
            } else {
                value -= current;
            }
        }
        return value;
    }

    private static int romanToDecimal(char c) {
        switch (c) {
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
            default:
                return 0;
        }
    }
}