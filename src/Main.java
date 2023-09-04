import java.util.*;
public class Main {
    private static final Map<String, String> galacticToRoman = new HashMap<>();
    private static final Map<String, Double> metalPrices = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            if (line.trim().isEmpty()){
                break;
            }

            conversion(line);
        }

        while (scanner.hasNextLine()){
            String question = scanner.nextLine();
            if(!question.trim().isEmpty()){
                query(question);
            }
        }

        scanner.close();
    }


    private static void conversion(String line){
        String[] parts = line.split( " ");
        if (parts.length == 3 && parts[1].equals("is")){
            String galacticUnit = parts[0];
            String romanNumber = parts[2];
            galacticToRoman.put(galacticUnit, romanNumber);
        }
        else if (parts.length >= 4 && parts[parts.length -1].equals("Credits") && parts[parts.length - 3].equals("is")){
            double value = calculateValue(parts, 0, parts.length-4);
            double metalPrice = Double.parseDouble(parts[parts.length-2]);
            double pricePerUnit = metalPrice / value;
            String metalName = parts[parts.length - 4];
            metalPrices.put(metalName, pricePerUnit);
        }
        else{
            System.out.println("Invalid conversion: " + line);
        }
    }

    private static void query(String question){
        String[] parts = question.split(" ");
        double value = calculateValue(parts, 3, parts.length-1);
        if (parts[0].equals("how") && parts[1].equals("much")){
            System.out.println(question + " is " + (int)value);
        }else if(parts[0].equals("how") && parts[1].equals("many") && parts[2].equals("Credits") && parts[3].equals("is")) {
            String metalName = parts[parts.length - 1];
            double totalValue = value * metalPrices.getOrDefault(metalName, 0.0);
            System.out.println(question + " is " + (int)totalValue + " Credits");
        }else if(value == -1){
            System.out.println("Invalid question: " + question);
        }
        else {
            System.out.println(question);
        }

    }

    private static double calculateValue(String[] parts, int start, int end){
        double value = 0;
        for (int i = start; i<end; ){
            if (i + 1 < end && galacticToRoman.containsKey(parts[i]) && galacticToRoman.containsKey(parts[i+1])){
                String roman1 = galacticToRoman.get(parts[i]);
                String roman2 = galacticToRoman.get(parts[i+1]);
                if (isSubstracionValid(roman1, roman2)){
                    value += getRomanNumberValue(roman2) - getRomanNumberValue(roman1);
                    i+= 2;
                }
                else {
                    return -1;
                }
            }
            else if (galacticToRoman.containsKey(parts[i])){
                value += getRomanNumberValue(galacticToRoman.get(parts[i]));
                i++;
            }
            else {
                return -1;
            }
        }
        return value;
    }

    private static boolean isSubstracionValid(String roman1, String roman2) {
        String validSubstractions = "IV,IX,XL,XC,CD,CM";
        String combinedRoman = roman1 + roman2;
        return validSubstractions.contains(combinedRoman);
    }

    private static int getRomanNumberValue(String romanNumber){
        int value = 0;
        for (int i = 0; i < romanNumber.length(); i++){
            int current = romanToDecimal(romanNumber.charAt(i));
            int next = (i + 1 < romanNumber.length()) ? romanToDecimal(romanNumber.charAt(i+1)) : 0;
            if (current >= next) {
                value += current;
            } else {
                value -= current;
            }
        }
        return value;
    }
    private static int romanToDecimal(char c){
        switch (c){
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'L':
                return 10;
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