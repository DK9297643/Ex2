import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ex2Sheet implements Sheet {
    private Cell[][] table;
    // Add your code here

    // ///////////////////
    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for (int i = 0; i < x; i = i + 1) {
            for (int j = 0; j < y; j = j + 1) {
                table[i][j] = new SCell("");
            }
        }
        eval();
    }

    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    @Override
    public String value(int x, int y) {
        if (!isIn(x, y)) {
            return Ex2Utils.ERR_FORM;  // שגיאה אם מחוץ לגבולות
        }

        // קבלת התא במיקום המבוקש
        Cell cell = get(x, y);
        if (cell == null) {
            return Ex2Utils.EMPTY_CELL;  // תא ריק
        }

        // הערכת הערך בתא
        String result = eval(x, y);  // מעריך את התא ומטפל בנוסחאות

        // בדיקת סוגי שגיאות ותרגומם למחרוזות המתאימות
        int cellType = cell.getType();
        if (cellType == Ex2Utils.ERR_CYCLE_FORM) {
            return Ex2Utils.ERR_CYCLE;
        }
        if (cellType == Ex2Utils.ERR_FORM_FORMAT) {
            return Ex2Utils.ERR_FORM;
        }
//        if (result.equals(String.valueOf(Ex2Utils.ERR_CYCLE_FORM))) {
//            return Ex2Utils.ERR_CYCLE;   // מחזיר "ERR_CYCLE!"
//        } else if (result.equals(String.valueOf(Ex2Utils.ERR_FORM))) {
//            return Ex2Utils.ERR_FORM;    // מחזיר "ERR_FORM!"
//        }

        return result;  // מחזיר את הערך המחושב
//        if (!isIn(x, y)) {
//            return Ex2Utils.EMPTY_CELL;
//        }
//        Cell cell = get(x, y);
//        if (cell == null) {
//            return Ex2Utils.EMPTY_CELL;
//        }
//
//        String result = eval(x, y);
//
//        // תרגום קודי שגיאה למחרוזות המתאימות
//        if (result.equals(String.valueOf(Ex2Utils.ERR_CYCLE_FORM))) {
//            return Ex2Utils.ERR_CYCLE;
//        }
//        if (result.equals(String.valueOf(Ex2Utils.ERR_FORM))) {
//            return Ex2Utils.ERR_FORM ;
//        }
//
//        return result;
    }


    @Override
    public int width() {
        return table.length;
    }

    @Override
    public int height() {
        return table[0].length;
    }

    @Override
    public void set(int x, int y, String s) {
        if (!isIn(x, y)) {
            throw new IllegalArgumentException("קואורדינטות מחוץ לגבולות הגיליון");
        }
        if (s == null) {
            s = "";
        }
        table[x][y] = new SCell(s);
    }
/////////////////////


    @Override
    public void eval() {
        int[][] dd = depth();

    }

    @Override
    public boolean isIn(int xx, int yy) {
        boolean ans = xx >= 0 && yy >= 0;
        // Add your code here
        if (xx >= 0 && yy >= 0 && xx < width() && yy < height()) {
            return true;
        } else {
            return false;
        }
        /////////////////////
    }

    @Override
    public int[][] depth() {
        // Add your code here
        int[][] depths = new int[width()][height()];

        // עוברים על כל התאים בגיליון
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //  System.out.println(i+","+j+" started");
                depths[i][j] = calculateCellDepth(i, j, new HashSet<>());
               get(i,j).setOrder(depths[i][j] );
                //  System.out.println(i+","+j+" ended "+depths[i][j]);
            }
        }

        return depths;
        // ///////////////////

    }

    private int calculateCellDepth(int x, int y, Set<String> visited) {
        // System.out.println("got in" +x+","+y);
        // System.out.println(visited);

        if (!isIn(x, y)) {
            return 0;
        }

        // קבלת התא
        Cell cell = get(x, y);
        if (cell == null || cell.getData() == null || cell.getData().isEmpty()) {
            return 0;
        }

        String data = cell.getData();

        // אם זה לא נוסחה, העומק הוא 0
        if (!data.startsWith("=")) {
            // System.out.println("no form");
            return 0;
        }

        // יצירת מזהה ייחודי לתא (למשל "0,1" עבור תא בעמודה A שורה 2)
        String cellId = x + "," + y;


        if (visited.contains(cellId)) {
            //  System.out.println("cycle found");
            return Ex2Utils.ERR_CYCLE_FORM;  // מצאנו מעגליות
        }

        // מוסיפים את התא הנוכחי לרשימת המבוקרים
        visited.add(cellId);

        // בדיקת מעגליות - אם כבר ביקרנו בתא זה

        // מוצאים את כל ההפניות לתאים אחרים
        Pattern pattern = Pattern.compile("[A-Za-z][0-9]+");
        Matcher matcher = pattern.matcher(data);

        int maxDepth = 0;  // העומק המקסימלי שנמצא
        boolean hasCircular = false;  // האם נמצאה מעגליות

        // עוברים על כל ההפניות לתאים
        while (matcher.find()) {
            String cellRef = matcher.group();  // למשל "A1"

            // המרת האות לאינדקס עמודה (A->0, B->1, etc.)
            int col = Character.toUpperCase(cellRef.charAt(0)) - 'A';
            // המרת המספר לאינדקס שורה (מתחיל מ-0)
            int row = Integer.parseInt(cellRef.substring(1));

            // חישוב העומק של התא המצוין
            //  System.out.println("IN");
            int refDepth = calculateCellDepth(col, row, visited);
            //  System.out.println("OUT");

            // אם נמצאה מעגליות
            if (refDepth == -1) {
                hasCircular = true;
            } else {
                // מעדכנים את העומק המקסימלי
                maxDepth = Math.max(maxDepth, refDepth);
            }
        }

        // מסירים את התא מרשימת המבוקרים
        System.out.println("removed" + cellId);
        visited.remove(cellId);

        // אם נמצאה מעגליות, מחזירים -1
        if (hasCircular) {
            return -1;
        }

        // אחרת, מחזירים את העומק המקסימלי + 1
        return maxDepth + 1;

    }

    @Override
    public void load(String fileName) throws IOException {
        // Add your code here

        /////////////////////
    }

    @Override
    public void save(String fileName) throws IOException {

        // Add your code here

        /////////////////////
    }

    @Override

    public String eval(int x, int y) {
        // משתמשים בסט לשמירת המסלול שעברנו
        Set<String> visited = new HashSet<>();
        return evalHelper(x, y, visited);
    }

    private String evalHelper(int x, int y, Set<String> visited) {
        String cellId = x + "," + y;
        Cell cell = get(x, y);

        // בודק אם כבר ביקרנו בתא זה - אם כן, זו מעגליות
        if (visited.contains(cellId)) {
            cell.setType(Ex2Utils.ERR_CYCLE_FORM);
            return cell.getData();  // מחזיר את המחרוזת "ERR_CYCLE" במקום -1
        }

//        if (cell == null) {
//            return Ex2Utils.ERR_FORM;
//        }

        // טיפול בסוגי תאים שונים
        switch(cell.getType()) {
            case Ex2Utils.NUMBER:
                try {
                    double num = Double.parseDouble(cell.getData());
                    return String.valueOf(num);
                } catch (NumberFormatException e) {
                    cell.setType(Ex2Utils.ERR_FORM_FORMAT);
                    return Ex2Utils.ERR_FORM;
                }

            case Ex2Utils.FORM:
                try {
                    visited.add(cellId);
                   // String formula = cell.getData().substring(1).toUpperCase();
                    String formula = cell.getData();
                    if (formula.startsWith("=")) {
                        formula = formula.substring(1).trim().toUpperCase();
                    }

// בדיקה אם זו הפניה פשוטה לתא (כמו =A1)
                    if (formula.matches("[A-Z][0-9]+")) {
                        int col = formula.charAt(0) - 'A';
                        int row = Integer.parseInt(formula.substring(1));
                        if (isIn(col, row)) {
                            String val = evalHelper(col, row, visited);
                            visited.remove(cellId);
                            return val;
                        }
                    }

                    // מחליף הפניות לתאים בערכים שלהם
                    for (int col = 0; col < width(); col++) {
                        for (int row = 0; row < height(); row++) {
                            String ref = Ex2Utils.ABC[col] + row;
                            if (formula.matches(".*\\b" + ref + "\\b.*")) {
                                String cellVal = evalHelper(col, row, visited);
                                // אם התקבלה שגיאת מחזוריות מתת-העץ
                                if (cellVal.equals(Ex2Utils.ERR_CYCLE)) {
                                    cell.setType(Ex2Utils.ERR_CYCLE_FORM);
                                    return Ex2Utils.ERR_CYCLE;
                                }
                                if (formula.startsWith("-"))  {
                                    formula = "(" + cellVal + ")";
                                } else {
                                    formula = formula.replaceAll("\\b" + ref + "\\b", "(" + cellVal + ")");
                                }
//                                if (formula.startsWith("-") && ref.equals(formula.substring(1))) {
//                                    formula = "-1*(" + cellVal + ")";
//                                }else {
//                                    formula = formula.replace(ref, "(" + cellVal + ")");
//                                }
                            }
                        }
                    }

                    visited.remove(cellId);
                    double result = SCell.computeForm(formula);
                    return String.valueOf(result);

                } catch (Exception e) {
                    cell.setType(Ex2Utils.ERR_FORM_FORMAT);
                    return Ex2Utils.ERR_FORM;
                }

            case Ex2Utils.TEXT:
                return cell.getData();

            default:
                return Ex2Utils.EMPTY_CELL;
        }
    }
    //    private String evalHelper(int x, int y, Set<String> visited) {
//        String ans = null;
//        String cellId = String.valueOf(x) + "," + String.valueOf(y);
//        Cell cell = get(x, y);
//        // בודק אם כבר ביקרנו בתא זה - אם כן, זו מעגליות
//        if (visited.contains(cellId)) {
//
//            cell.setType(Ex2Utils.ERR_CYCLE_FORM);
//            return String.valueOf(Ex2Utils.ERR_CYCLE_FORM);
//        }
//
//        if (get(x, y) != null) {
//            ans = get(x, y).toString();
//        } else {
//            cell.setType(Ex2Utils.ERR_FORM_FORMAT);
//            return String.valueOf(Ex2Utils.ERR_FORM);
//        }
//
//
//        if (cell != null) {
//            if (cell.getType() == Ex2Utils.NUMBER) {
//                try {
//                    double num = Double.parseDouble(cell.getData());
//                    ans = String.valueOf(num);
//                } catch (NumberFormatException e) {
//                    ans = String.valueOf(Ex2Utils.ERR_FORM);
//                }
//            } else if (cell.getType() == Ex2Utils.FORM) {
//                try {
//                    // מוסיף את התא הנוכחי לרשימת המבוקרים
//                    visited.add(cellId);
//
//                    String formula = cell.getData().substring(1);
//                    formula = formula.toUpperCase();
//
//                    for (int col = 0; col < width(); col++) {
//                        for (int row = 0; row < height(); row++) {
//                            String ref = Ex2Utils.ABC[col] + row;
//
//                            if (formula.contains(ref)) {
//                                String cellVal = evalHelper(col, row, visited);
//                                // אם התגלתה מעגליות בתת-העץ
//                                if (cellVal.equals(String.valueOf(Ex2Utils.ERR_CYCLE_FORM))) {
//                                    cell.setType(Ex2Utils.ERR_CYCLE_FORM);
//                                    return String.valueOf(Ex2Utils.ERR_CYCLE_FORM);
//                                }
//                                formula = formula.replace(ref, cellVal);
//                            }
//                        }
//                    }
//
//                    // מסיר את התא מרשימת המבוקרים
//                  //  visited.remove(cellId);
//
//                    double result = SCell.computeForm(formula);
//                    ans = String.valueOf(result);
//                } catch (NumberFormatException e) {
//                    cell.setType(Ex2Utils.ERR_FORM_FORMAT);
//                    ans = String.valueOf(Ex2Utils.ERR_FORM);
//                }
//            } else if (cell.getType() == Ex2Utils.TEXT) {
//                ans = cell.getData();
//            } else {
//                ans = Ex2Utils.EMPTY_CELL;
//            }
//        }
//
//        return ans;
//    }


//   public String eval(int x, int y) {
//       String ans = null;
//       if(get(x, y) != null) {
//           ans=get(x, y).toString();
//
//       }else {
//           return Ex2Utils.ERR_FORM;
//       }
//
//       Cell cell = get(x, y);
//       if (cell != null) {
//           if(cell.getType() == Ex2Utils.NUMBER){
//               try {
//                   double num = Double.parseDouble(cell.getData());
//                   ans = String.valueOf(num);
//               }catch (NumberFormatException e){
//                   ans = Ex2Utils.ERR_FORM;
//               }
//
//               }else if (cell.getType() == Ex2Utils.FORM){
//               try {
//                   String formula = cell.getData().substring(1);
//                         formula = formula.toUpperCase();
//
//
//                       //if(formula.charAt(i).is)
//                   for (int col =0 ; col < width() ; col++) {
//                       for (int row = 0 ; row < height() ; row++) {
//                            String ref = Ex2Utils.ABC[col] + row;
//
//                           if (formula.contains(ref)) {
//                               String cellVal = eval(col,row);
//                               formula = formula.replace(ref, cellVal);
//                           }
//                       }
//
//                   }
//               double result = SCell.computeForm(formula);
//                   ans = String.valueOf(result);
//           }   catch (NumberFormatException e){
//                   ans = Ex2Utils.ERR_FORM;
//               }
//           }
//       }else if (cell.getType() == Ex2Utils.TEXT){
//           ans = cell.getData();
//       }else {
//           ans = Ex2Utils.EMPTY_CELL;
//      }
//
//    return ans;
//   }

    @Override
    public Cell get(int x, int y) {
        if (isIn(x, y)) {
            return table[x][y];
        } else {
            return null;
        }
    }

    @Override
    public Cell get(String cords) {

        CellEntry ce = new CellEntry(0,0);
        if (!ce.isValid()) {
            return null;
        }

        // חילוץ האות (X) והמספר (Y)
        char columnChar = cords.charAt(0);
        String rowPart = cords.substring(1);

        // המרה של האות לאינדקס עמודה (0 -> A, 1 -> B, וכו')
        int x = Character.toUpperCase(columnChar) - 'A';
        int y = Integer.parseInt(rowPart); // השורות בגיליון מתחילות מ-1


        // בדיקה אם y מחוץ לטווח


        // החזרת התא בקואורדינטות [x][y]
        return table[x][y];
    }

//    private String replaceReferences(String formula) {
//        Pattern pattern = Pattern.compile("[A-Za-z][0-9]+");
//        Matcher matcher = pattern.matcher(formula);
//        StringBuffer result = new StringBuffer();
//
//        while (matcher.find()) {
//            String cellRef = matcher.group();
//            int col = cellRef.charAt(0) - 'A';
//            int row = Integer.parseInt(cellRef.substring(1)) - 1;
//
//            if (isIn(col, row)) {
//                String value = value(col, row);
//                try {
//                    Double.parseDouble(value);
//                    matcher.appendReplacement(result, value);
//                } catch (NumberFormatException e) {
//                    matcher.appendReplacement(result, "0");
//                }
//            } else {
//                matcher.appendReplacement(result, "0");
//            }
//        }
//        matcher.appendTail(result);
//        return result.toString();
//    }
}