import java.text.DecimalFormat;

public class TestDecrement {
	public static void main(String[] args) {
		DecimalFormat df1 = new DecimalFormat("#.############################################################");
		double decrement = 0.25;
		double bidVal = 2.00;
		double calVal = 0.00;
		for (int i=1;i<20000;i++) {
			calVal = bidVal - bidVal*decrement/100;
			bidVal = calVal;
			System.out.println(i+" : "+df1.format(calVal));
		}
	}
}
