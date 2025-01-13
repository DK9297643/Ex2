// Add your documentation below:

public class CellEntry  implements Index2D {
    private int x;
    private int y;

    public CellEntry(int xx, int yy) {
        this.x = xx;
        this.y = yy;
    }

    @Override
    public String toString(){

            // המרה של הערך x לאות אנגלית (A, B, וכו')
            char column = (char) ('A' + x); // x הוא אינדקס העמודה (0 -> 'A', 1 -> 'B')

            // y מייצג את השורה
            int row = y ; // השורות מתחילות מ-1 ולא מ-0

            // בניית המחרוזת בפורמט "B3" לדוגמה
            return column + String.valueOf(row);


    }

    public boolean isValid() {
        String str = this.toString(); // מתבססים על המחרוזת שמחזירה מתודת toString()

        // בדיקה אם המחרוזת אינה ריקה ושאורכה לפחות 2
        if (str == null || str.length() < 2) {
            return false;
        }

        // בדיקת התו הראשון - האם הוא אות A-Z או a-z
        char firstChar = str.charAt(0);
        if (!((firstChar >= 'A' && firstChar <= 'Z') || (firstChar >= 'a' && firstChar <= 'z'))) {
            return false;
        }

        // בדיקת החלק המספרי - האם הוא מספר בין 0 ל-99
        try {
            int number = Integer.parseInt(str.substring(1)); // ניסיון להמיר את שאר המחרוזת למספר
            if (number < 0 || number > 99) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false; // אם לא הצלחנו להמיר למספר, זה אינו תקין
        }

        // אם כל הבדיקות עברו, המחרוזת תקינה
        return true;
    }



    @Override
    public int getX() { if (this.x < 0 || this.x > 25) {
        throw new IllegalArgumentException("Number must be between 0 and 25.");
    }

        // המרת המספר לאות אנגלית
        return (char) ('A' + this.x);
        }

    @Override
    public int getY() {
        return this.y;}
}
