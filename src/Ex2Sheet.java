import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    }

    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    @Override
    public String value(int x, int y) {
        if (!isIn(x, y)) {
            return Ex2Utils.EMPTY_CELL;
        }
        Cell cell = get(x, y);
        if (cell == null) {
            return Ex2Utils.EMPTY_CELL;
        }
        return eval(x, y);  // מחזיר את ה

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
        // Add your code here

        // ///////////////////
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
        int[][] ans = new int[width()][height()];
        // Add your code here

        // ///////////////////
        return ans;
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
       String ans = null;
       if(get(x, y) != null) {
           ans=get(x, y).toString();

       }else {
           return Ex2Utils.ERR_FORM;
       }
       Cell cell = get(x, y);
       if (cell != null) {
           if(cell.getType() == Ex2Utils.NUMBER){
               try {
                   double num = Double.parseDouble(cell.getData());
                   ans = String.valueOf(num);
               }catch (NumberFormatException e){
                   ans = Ex2Utils.ERR_FORM;
               }

               }else if (cell.getType() == Ex2Utils.FORM){
               try {
                   String formula = cell.getData().substring(1);
                   for (int col =0 ; col < width() ; col++) {
                       for (int row = 0 ; row < height() ; row++) {
                           String ref = Ex2Utils.ABC[col] + row;
                           if (formula.contains(ref)) {
                               String cellVal = eval(col,row);
                               formula = formula.replace(ref, cellVal);
                           }
                       }

                   }
                double result = SCell.computeForm(formula);
                   ans = String.valueOf(result);
           }   catch (NumberFormatException e){
                   ans = Ex2Utils.ERR_FORM;
               }
           }
       }else if (cell.getType() == Ex2Utils.TEXT){
           ans = cell.getData();
       }else {
           ans = Ex2Utils.EMPTY_CELL;
       }

    return ans;
    }

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
        CellEntry ce = new CellEntry();
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
    private String replaceReferences(String formula) {
        Pattern pattern = Pattern.compile("[A-Z][0-9]+");
        Matcher matcher = pattern.matcher(formula);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String cellRef = matcher.group();
            int col = cellRef.charAt(0) - 'A';
            int row = Integer.parseInt(cellRef.substring(1)) - 1;

            if (isIn(col, row)) {
                String value = value(col, row);
                try {
                    Double.parseDouble(value);
                    matcher.appendReplacement(result, value);
                } catch (NumberFormatException e) {
                    matcher.appendReplacement(result, "0");
                }
            } else {
                matcher.appendReplacement(result, "0");
            }
        }
        matcher.appendTail(result);
        return result.toString();
        }
    }