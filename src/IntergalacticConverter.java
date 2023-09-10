import java.util.*;

public class IntergalacticConverter {

    private static final Map<String, Integer> ROMAN_VALUES = new HashMap<String, Integer>() {{
        put("I", 1);
        put("V", 5);
        put("X", 10);
        put("L", 50);
        put("C", 100);
        put("D", 500);
        put("M", 1000);
    }};

    private final Map<String, String> intergalacticToRomanMap = new HashMap<>();
    private final Map<String, Double> metalsValueMap = new HashMap<>();

    public static void main(String[] args) {
        IntergalacticConverter converter = new IntergalacticConverter();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) break;
            converter.processLine(line);
        }

        scanner.close();
    }

    void processLine(String line) {
        if (line.endsWith("?")) {
            processQuery(line.substring(0, line.length() - 1).trim());
        } else if (line.contains(" is ") && !line.contains(" Credits")) {
            String[] parts = line.split("\\s+");
            intergalacticToRomanMap.put(parts[0], parts[2]);
        } else if (line.contains(" is ") && line.contains(" Credits")) {
            String[] parts = line.split(" is ");
            String metal = parts[0].substring(parts[0].lastIndexOf(" ") + 1);
            String[] intergalacticNumbers = parts[0].substring(0, parts[0].lastIndexOf(" ")).split("\\s+");

            String roman = convertIntergalacticToRoman(intergalacticNumbers);
            if (roman == null) {
                System.out.println("Error: Unable to convert " + String.join(" ", intergalacticNumbers) + " to Roman numerals.");
                return;
            }
            int value = convertRomanToArabic(roman);
            double perUnitValue = Double.parseDouble(parts[1].replace(" Credits", "")) / value;
            metalsValueMap.put(metal, perUnitValue);
        }
    }

    void processQuery(String query) {
        if (query.startsWith("how much is ")) {
            String[] intergalacticNumbers = query.replace("how much is ", "").split("\\s+");
            String roman = convertIntergalacticToRoman(intergalacticNumbers);
            if (roman == null) {
                System.out.println("I have no idea what you are talking about");
                return;
            }
            int value = convertRomanToArabic(roman);
            System.out.println(String.join(" ", intergalacticNumbers) + " is " + value);
        } else if (query.startsWith("how many Credits is ")) {
            String[] parts = query.replace("how many Credits is ", "").split("\\s+");
            String metal = parts[parts.length - 1];
            String[] intergalacticNumbers = Arrays.copyOf(parts, parts.length - 1);
            String roman = convertIntergalacticToRoman(intergalacticNumbers);

            if (roman == null || !metalsValueMap.containsKey(metal)) {
                System.out.println("I have no idea what you are talking about");
                return;
            }

            int value = convertRomanToArabic(roman);
            System.out.println(String.join(" ", intergalacticNumbers) + " " + metal + " is " + (int) (value * metalsValueMap.get(metal)) + " Credits");
        } else {
            System.out.println("I have no idea what you are talking about");
        }
    }

    String convertIntergalacticToRoman(String[] intergalacticNumbers) {
        StringBuilder roman = new StringBuilder();
        for (String unit : intergalacticNumbers) {
            String romanValue = intergalacticToRomanMap.get(unit);
            if (romanValue == null) return null;
            roman.append(romanValue);
        }
        return roman.toString();
    }

    int convertRomanToArabic(String roman) {
        int totalValue = 0;
        int prevValue = 0;

        for (char symbol : new StringBuilder(roman).reverse().toString().toCharArray()) {
            int value = ROMAN_VALUES.get(String.valueOf(symbol));
            if (value < prevValue) {
                totalValue -= value;
            } else {
                totalValue += value;
            }
            prevValue = value;
        }

        return totalValue;
    }
}
