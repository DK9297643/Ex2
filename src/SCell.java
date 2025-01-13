// Add your documentation below:

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SCell implements Cell {
    private String line;
    private int type;
    private int order;

    public SCell(String s) {

        this.line = s;
       determineType();
    }

    @Override
    public int getOrder() {

        if (type == Ex2Utils.TEXT || type == Ex2Utils.NUMBER) {
            return 0;
        }

        // אם זו נוסחה שגויה או יש מעגליות, מחזיר -1
        if (type == Ex2Utils.ERR || type == Ex2Utils.ERR_CYCLE_FORM) {
            return -1;
        }

        // אם זו לא נוסחה תקינה, נחזיר 0
        if (type != Ex2Utils.FORM) {
            return 0;
        }

        // מכאן והלאה מטפלים בנוסחה תקינה
        String formula = line.substring(1);  // מסיר את סימן ה-=
        List<String> dependencies = findCellReferences(formula);

        if (dependencies.isEmpty()) {
            return 1;  // נוסחה בלי תלויות (למשל "=2+3")
        }

        return order;  // מחזיר את הסדר שנקבע מראש
    }

    //@Override
    @Override
    public String toString() {
        return getData();
    }

    @Override
    public void setData(String s) {
        // Add your code here
        if (s == null) {
            this.line = "";
        } else {
            this.line = s;
        }
        // נקבע את סוג התא
        determineType();
    }

    @Override
    public String getData() {
     //
        //  System.out.println("Getting data from SCell: " + line);
       //
        return line;
    }
    @Override
    public int getType() {
        return this.type;
        }


    @Override
    public void setType(int t) {
        // בדיקת תקינות הערך
        this.type = t;
        // אם זו שגיאת מחזוריות, נעדכן גם את הdata
//        if (t == Ex2Utils.ERR_CYCLE_FORM) {
//            this.line = Ex2Utils.ERR_CYCLE;
//        }
//        // אם זו שגיאת פורמט, נעדכן גם את הdata
//        else if (t == Ex2Utils.ERR_FORM_FORMAT) {
//            this.line = Ex2Utils.ERR_FORM;
//        }
    }
//    @Override
//    public int getType() {
//
//
//        if (isText(this.line) ) {
//            this.type = Ex2Utils.TEXT;
//            return type;
//        }else if (isNumber(this.line) ) {
//            this.type = Ex2Utils.NUMBER;
//            return type;
//        }else if (isForm(this.line)) {
//            this.type = Ex2Utils.FORM;
//            return type;
//        }else { this.type = Ex2Utils.ERR_FORM_FORMAT;
//            return type;
//        }
//
//    }
//
//    @Override
//    public void setType(int t) {
//
//       type = t;
//    }

    @Override
    public boolean setOrder(int t) {
        if (t < -1) { // סדר לא יכול להיות קטן מ-1-
            return false;
        }
        this.order = t;
        if (t == -1) {
            this.type = -1; // שגיאת מעגליות
        }
        return true;
//        if (t < -1) return false;  // סדר לא יכול להיות קטן מ-1-
//        order = t;
//        if (t==-1)
//            this.setType(Ex2Utils.ERR_CYCLE_FORM);
//        return true;
    }
    private void determineType() {
        if (line == null || line.isEmpty()) {
            setType(Ex2Utils.TEXT);
            return;
        }

        // בדיקה אם זה נוסחה (מתחיל ב-=)
        if (line.startsWith("=")) {
            if (isForm(line)) {
                setType(Ex2Utils.FORM);
            } else {
                setType(Ex2Utils.ERR);
            }
            return;
        }

        // בדיקה אם זה מספר
        try {
            Double.parseDouble(line);
            setType(Ex2Utils.NUMBER);
        } catch (NumberFormatException e) {
            setType(Ex2Utils.TEXT);
        }
    }
    private List<String> findCellReferences(String formula) {
        List<String> references = new ArrayList<>();

        // תבנית לזיהוי הפניות לתאים (למשל A1, B2 וכו')
        Pattern pattern = Pattern.compile("[A-Za-z]+[0-9]+");
        Matcher matcher = pattern.matcher(formula);

        while (matcher.find()) {
            references.add(matcher.group());
        }

        return references;
    }

    public boolean isNumber(String formula) {
        boolean flag = false;
        try {
            Double.parseDouble(formula);
            return true;
        }catch (NumberFormatException e) {
           return false;
        }
    }
    public boolean isText(String formula) {
        return !isNumber(formula) && !isForm(formula);
    }

    public static boolean isForm(String text) {
        // Reject any spaces in formula


        if (!text.startsWith("="))
            return false;
        text = text.substring(1);
        text = text.replaceAll("[^A-Za-z0-9]", "");

        int balance = 0;  // Track parentheses matching
        char lastChar = ' ';
        System.out.println(" ");
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            char check1= text.charAt(0);
            char check2= text.charAt(text.length() - 1);
            // Track opening/closing parentheses
            if (c == '(') balance++;
            if (c == ')') balance--;
            if (balance < 0) return false;  // Too many closing parentheses

            // Operators validation
            if (isOperator(c)) {
                // Can't start/end with operator
                if ( check1 == '*'|| check1 == '/' ||
                        check2 == '*' || check2 == '/' ||
                        check2 == '+' || check2 == '-' )
                    return false;

                // Can't have consecutive operators
                if (isOperator(lastChar))
                    return false;
            }

            // Verify only valid characters used
            if (!isValidChar(c)) return false;

            lastChar = c;
        }
        if (!(balance == 0))
            return false;

      return true;
    }

    // Helper to check operators (+,-,*,/)
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    // Helper to check if character is allowed in formula
    private static boolean isValidChar(char c) {
        return Character.isDigit(c) ||  // Numbers
                isOperator(c) ||         // Operators
                c == '.' ||              // Decimal point
                c == '(' || c == ')' ||  // Parentheses
                (c >= 'A' && c <= 'Z') || // Cell references
                 (c >= 'a' && c <= 'z');
    }





    public static double computeForm(String text)
    {
        text = text.trim();



        if (text.startsWith("="))
            text = text.substring(1);

//        if (text.startsWith("-")) {
//            // אם יש רק מינוס ומספר
//            if (!containsOperator(text.substring(1))) {
//                return -Double.parseDouble(text.substring(1).trim());
//            }
//        }
////        if (text.startsWith("-")) {
////            String x = text.substring(1);
////            return computeForm("0-1*" + x);
////        }
//        if (text.startsWith("+")) {
//            String x = text.substring(1);
//            return  computeForm(x);
//        }

        while (text.startsWith("(") && text.endsWith(")") && isBalanced(text.substring(1, text.length() - 1))) {
            text = text.substring(1, text.length() - 1);
        }


        if (!containsOperator(text)) {
            return Double.parseDouble(text.trim());
        }


            int minIndex = indexOperatorMin(text);
             char operator = text.charAt(minIndex);

            // פיצול הביטוי לשני חלקים

            String leftPart = text.substring(0, minIndex);
            String rightPart = text.substring(minIndex + 1);


            double leftValue = computeForm(leftPart);
            double rightValue = computeForm(rightPart);

        return calculate(leftValue, rightValue, operator);

      }
   // public static boolean isCellReference(String text) {
     //   return text.matches("[A-Za-z][0-9]+");  // למשל A1, B2 וכו'
  //  }
      private static boolean containsOperator(String text) {
        return text.contains("+") || text.contains("-") || text.contains("*") || text.contains("/");
    }
    private static double calculate(double leftValue , double  rightValue, char operator) {



        switch (operator) {
            case '+': return leftValue + rightValue;
            case '-': return leftValue - rightValue;
            case '*': return leftValue * rightValue;
            case '/': return leftValue / rightValue;
            default: throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }
    public static int indexOperatorMin(String text) {
        int index = 0; // רמת הסוגריים
        double minValue = Double.MAX_VALUE; // התחלה עם ערך מקסימלי
        int minIndex = -1; // אינדקס האופרטור עם הערך הכי נמוך
       // text = text.substring(1);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '(') {
                index++; // סוגריים סוגרים - עלייה ברמת הסוגריים
            } else if (c == ')') {
                index--; // סוגריים פותחים - ירידה ברמת הסוגריים
            }

            double operatorValue = Double.MAX_VALUE;

            // חישוב ערך האופרטור
            if (c == '-' || c == '+') {
                operatorValue = 0.25 + index;
            } else if (c == '*' || c == '/') {
                operatorValue = 0.50 + index;
            }

            // בדיקת הערך המינימלי
            if (operatorValue <= minValue) {
                minValue = operatorValue; // עדכון הערך המינימלי
                minIndex = i; // עדכון האינדקס של האופרטור
            }
        }

        return minIndex; // מחזיר את האינדקס של האופרטור עם הערך הכי נמוך
    }
    private static int countOperators(String text) {
        int count = 0;
        for (char c : text.toCharArray()) {
            if (c == '+' || c == '-' || c == '*' || c == '/') {
                count++;
            }
        }
        return count;
    }
    private static boolean isBalanced(String text) {
        int balance = 0;
        for (char c : text.toCharArray()) {
            if (c == '(') balance++;
            else if (c == ')') balance--;
            if (balance < 0) return false;
        }
        return balance == 0;
    }
    private static boolean containsCellReference(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (Character.isLetter(text.charAt(i))) {
                int j = i + 1;
                while (j < text.length() && Character.isDigit(text.charAt(j))) {
                    j++;
                }
                if (j > i + 1) {
                    String potential = text.substring(i, j);
                    if (isCellReference(potential)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private static boolean isCellReference(String text) {
        return text.matches("[A-Za-z][0-9]+");  // למשל A1, B2 וכו'
    }
    }

