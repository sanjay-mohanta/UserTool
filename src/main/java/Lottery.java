import java.util.Random;

public class Lottery {
	public static void main(String[] args) {
		try {
			int buyerCount = 4;
			int buyerList[] = new int[buyerCount];
			buyerList[0] = 65196;
			buyerList[1] = 82581;
			buyerList[2] = 90013;
			buyerList[3] = 90015;
			
			for (int i = 0; i < buyerList.length; i++) {
				System.out.println(buyerList[i]);
			}
			
			int n = buyerList.length;
			Random rnd = new Random();
			while (n > 1) {
				int k = (rnd.nextInt(n) % n);
				System.out.println("k " + k);
				n--;
			
				System.out.println("n " + n);
				if (k < 0 || k >= buyerCount) {
					throw new Exception("Issue in Shuffle");
				}
				int value = buyerList[k];
				buyerList[k] = buyerList[n];
				buyerList[n] = value;
			}
			
			for (int i = 0; i < buyerList.length; i++) {
				System.out.println(buyerList[i]);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
