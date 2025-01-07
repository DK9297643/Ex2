import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        /*   public int[][] depth() {

            // Add your code here
            int[][] depths = new int[width()][height()];

            // עוברים על כל התאים בגיליון
            for (int i = 0; i < width(); i++) {
                for (int j = 0; j < height(); j++) {
                    depths[i][j] = calculateCellDepth(i, j, new HashSet<>());
                }
            }
            return depths;
            // ///////////////////

        }
        private int calculateCellDepth(int x, int y, Set<String> visited) {
            // בדיקה שהקואורדינטות תקינות

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
                System.out.println(x+""+y);
                return 0;
            }

            // יצירת מזהה ייחודי לתא (למשל "0,1" עבור תא בעמודה A שורה 2)
            String cellId = x + "," + y;


            if (visited.contains(cellId)) {

                return -1;  // מצאנו מעגליות
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
                int row = Integer.parseInt(cellRef.substring(1)) - 1;

                // חישוב העומק של התא המצוין
                System.out.println("IN");
                int refDepth = calculateCellDepth(col, row, visited);
                System.out.println("OUT");

                // אם נמצאה מעגליות
                if (refDepth == -1) {
                    hasCircular = true;
                } else {
                    // מעדכנים את העומק המקסימלי
                    maxDepth = Math.max(maxDepth, refDepth);
                }
            }

            // מסירים את התא מרשימת המבוקרים
            System.out.println(visited);
            visited.remove(cellId);

            // אם נמצאה מעגליות, מחזירים -1
            if (hasCircular) {
                return -1;
            }

            // אחרת, מחזירים את העומק המקסימלי + 1
            return maxDepth + 1;    */
        }
    }
