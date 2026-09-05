
public class Basic {
	public static void main(String[] args) {
		double basic = 80000.00;
		double da = 54.1;
		double hra = 30.00;
		double perks = 35.00;
		double salary = basic*(100+da+hra+perks)/100;
		System.out.println("Salary : "+salary);
		double pf = (basic+basic*da/100)*0.12;
		System.out.println("Pf : "+pf);
		double incometax = 30000.00;
		System.out.println("Income Tax : "+incometax);
		double netsalary = salary - pf - incometax;
		System.out.println("Net Salary : "+netsalary);
		for(int i=0;i<=10;i++) {
			basic = basic*1.03;
			System.out.println(basic);
		}
	}
}
