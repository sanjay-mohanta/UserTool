import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class RoundingExample {
    public static void main(String[] args) {
        DecimalFormat df4 = new DecimalFormat("#0.000"); 
		df4.setRoundingMode(java.math.RoundingMode.HALF_UP);
		//BigDecimal value = new BigDecimal("10.1374");
		//double data1= 10.1375;
		//df4.format(value);
		System.out.println(df4.format(new BigDecimal("10.1374")));
    }
}