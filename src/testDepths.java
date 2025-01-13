import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;




public class testDepths {


    @Test
    void testEmptyCellsDepth() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);
        int[][] depths = sheet.depth();

        // כל התאים הריקים צריכים להיות בעומק 0
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                assertEquals(0, depths[i][j]);
            }
        }
    }




    @Test
    void testMixedContent() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        // תא טקסט
        sheet.set(0, 0, "Hello");

        // תא מספרי
        sheet.set(1, 0, "42");

        // נוסחה שמשתמשת במספר
        sheet.set(2, 0, "=B1*2");  // תלוי ב-B1

        int[][] depths = sheet.depth();

        assertEquals(0, depths[0][0]); // טקסט - עומק 0
        assertEquals(0, depths[1][0]); // מספר - עומק 0
        assertEquals(1, depths[2][0]); // נוסחה - עומק 1
    }



}
