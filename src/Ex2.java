public class Ex2 {


    public class Cell {
        private String content;
        private CellType type;
        private Double computedValue;

        public enum CellType {
            NUMBER, TEXT, FORMULA, ERROR
        }

        public void setContent(String content) {
            this.content = content;
            if (isNumber(content)) {
                this.type = CellType.NUMBER;
                this.computedValue = Double.parseDouble(content);
            } else if (isText(content)) {
                this.type = CellType.TEXT;
                this.computedValue = null;
            } else if (isForm(content)) {
                this.type = CellType.FORMULA;
                this.computedValue = computeForm(content);
            } else {
                this.type = CellType.ERROR;
                this.computedValue = null;
            }
        }

        public boolean isNumber(String text) {
            try {
                Double.parseDouble(text);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        public boolean isText(String text) {
            // אם לא מספר ולא נוסחה, אז טקסט
            return !isNumber(text) && !isForm(text);
        }

        public boolean isForm(String text) {
            return text.startsWith("=");
        }

        public Double computeForm(String form) {
            // נניח שהחישוב פשוט - רק נוסחאות פשוטות
            if (form.startsWith("=")) {
                String expression = form.substring(1);
                // פה אפשר להוסיף חישוב נוסחאות
                // נניח שהחישוב פשוט לדוגמה:
                if (expression.equals("1+2")) {
                    return 3.0;
                }
            }
            return null;
        }

        public String getContent() {
            return content;
        }

        public CellType getType() {
            return type;
        }
    }







































}
