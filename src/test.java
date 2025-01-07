

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions .*;
import static org.junit.jupiter.api.Assertions.assertEquals;

    /**
     * This JUnit class represents a very partial test class for Ex1.
     * Make sure you complete all the needed JUnits
     */
    class FormulaValidatorTest {


        @Test
        public void testValidFormula() {

            // נוסחאות חוקיות
            //   assertTrue(SCell.isForm("=-A4+A500"));
            assertTrue(SCell.isForm("=3+5"));
            assertFalse(SCell.isForm("=(25-+33)"));
        }

        @Test
        public void testInvalidFormulaContainsSpace() {

            // נוסחאות עם רווחים (לא חוקיות)
            assertFalse(SCell.isForm("=23 + 45"));
            //  assertFalse(SCell.isForm("=A1 B2"));
        }

        @Test
        public void testInvalidFormulaStartsWithInvalidOperator() {

            // נוסחאות שמתחילות באופרטור (לא חוקיות)
            assertTrue(SCell.isForm("=((25/4))"));
            assertTrue(SCell.isForm("=-3+5"));
        }

        @Test
        public void testInvalidFormulaConsecutiveOperators() {

            // נוסחאות עם אופרטורים רצופים (לא חוקיות)
            //   assertFalse(SCell.isForm("=*A1+B2"));
            //  assertFalse(SCell.isForm("=A1**B2"));
        }

        @Test
        public void testIndexOperatorMin() {
            // בדיקות לפונקציה indexOperatorMin
            assertEquals(5, SCell.indexOperatorMin("=((25/4))"));
            assertEquals(3, SCell.indexOperatorMin("=-3+5"));
            assertEquals(10, SCell.indexOperatorMin("=(2*(3+5))/7"));
            //  assertEquals(2, SCell.indexOperatorMin("=A1+B2"));
        }

        @Test
        public void testInvalidFormulaUnmatchedParentheses() {

            // נוסחאות עם סוגריים לא תואמים (לא חוקיות)
            //   assertFalse(SCell.isForm("=A1+(B2"));
            //  assertFalse(SCell.isForm("=A1+B2)"));
            //  assertFalse(SCell.isForm("=A1+(B2+C3"));
        }

        @Test
        public void testInvalidFormulaInvalidCharacter() {

            // נוסחאות עם תו לא חוקי
            // assertFalse(SCell.isForm("=A1&+B2"));
            // assertFalse(SCell.isForm("=A1@B2"));
        }
       @Test
        public void testComputeExpression() {
           // טסטים בסיסיים


           // טסטים עם סוגריים
           // assertEquals( 8.0,SCell.computeForm("=(3+5)") );
           assertEquals(16, SCell.computeForm("=((3+5)*2)"));
           assertEquals(-2, SCell.computeForm("=(3+5-2-8)"));
       }
           @Test
           public void testSimpleMultiplication() {
               assertEquals(15.0, SCell.computeForm("=3*5"));
               assertEquals(21.0, SCell.computeForm("=7*3"));
           }

           @Test
           public void testSimpleDivision() {
               assertEquals(0.6, SCell.computeForm("=3/5" ));
               assertEquals(2.5, SCell.computeForm("=5/2"));
           }

           @Test
           public void testWithParentheses() {
               assertEquals(16.0, SCell.computeForm("=((3+5)*2)"));
               assertEquals(16.0, SCell.computeForm("=(2*(3+5))"));
           }

           @Test
           public void testComplexExpressions() {
               assertEquals(-2.0, SCell.computeForm("=(3+5-2-8)"));
               assertEquals(14.0, SCell.computeForm("=(2+3*4)"));
               assertEquals(20.0, SCell.computeForm("=(2+3)*(2+2)"));
           }

           @Test
           public void testNestedParentheses() {
               assertEquals(22.0, SCell.computeForm("=(2+(3*4)+8)"));
               assertEquals(45.0, SCell.computeForm("=((2+3)*(4+5))"));
           }

           @Test
           public void testMultipleOperations() {
               assertEquals(7.0, SCell.computeForm("=1+2+4"));
               assertEquals(-5.0, SCell.computeForm("=1-2-4"));
               assertEquals(24.0, SCell.computeForm("=2*3*4"));
               assertEquals(0.75, SCell.computeForm("=3/2/2"));
           }

           @Test
           public void testMixedOperations() {
               assertEquals(11.0, SCell.computeForm("=1+2*5"));
               assertEquals(15.0, SCell.computeForm("=(1+2)*5"));
               assertEquals(7.0, SCell.computeForm("=1*2+5"));
           }

    }
