 public class testDepths {
    public static void main(String[] args){
    // יצירת גיליון קטן 3x3
    Ex2Sheet sheet = new Ex2Sheet(3, 3);

    // מילוי הגיליון עם ערכים פשוטים
    // שורה 1
        sheet.set(0, 0, "5");           // A1 = 5
        sheet.set(1, 0, "=A1+2");       // B1 = A1+2
        sheet.set(2, 0, "=B1+3");       // C1 = B1+3

    // נקבל את מערך העומקים
    int[][] depths = sheet.depth();

    // הדפסת הערכים בגיליון
    System.out.println("Values in sheet:");
    System.out.println("A1: 5");
    System.out.println("B1: =A1+2");
    System.out.println("C1: =B1+3");
    System.out.println();

    // הדפסת העומקים
    System.out.println("Expected depths:");
    System.out.println("A1: 0 (no dependencies)");
    System.out.println("B1: 1 (depends on A1)");
    System.out.println("C1: 2 (depends on B1 which depends on A1)");
    System.out.println();

    System.out.println("Actual depths:");
    System.out.println("A1: " + depths[0][0]);
    System.out.println("B1: " + depths[1][0]);
    System.out.println("C1: " + depths[2][0]);
}
    }