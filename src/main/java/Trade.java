import java.util.LinkedHashMap;
import java.util.Map;

public class Trade {
	public static void main(String[] args) {
		Map<Double, Double> buyOrder = new LinkedHashMap<Double, Double>();
		Map<Double, Double> sellOrder = new LinkedHashMap<Double, Double>();
		buyOrder.put(2030.00,1350.00);
		buyOrder.put(2024.00,8460.00);
		buyOrder.put(2022.00,570.00);
		buyOrder.put(2020.00,14910.00);
		buyOrder.put(2019.00,2400.00);
		buyOrder.put(2015.00,4920.00);
		buyOrder.put(2013.00,1620.00);
		buyOrder.put(2010.00,270.00);
		buyOrder.put(2008.00,1373.00);
		System.out.println(buyOrder);
	}
}
