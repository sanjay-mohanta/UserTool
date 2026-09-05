import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TestRank {
	public static void main(String[] args) {
		Bidder bidder1 = new Bidder();
		Bidder bidder2 = new Bidder();
		Bidder bidder3 = new Bidder();
		
		bidder1.setName("One");
		bidder2.setName("Two");
		bidder3.setName("Three");
		
		bidder1.setQualified(true);
		bidder2.setQualified(true);
		bidder3.setQualified(true);
		
		bidder1.setApplicantScore(100);
		bidder2.setApplicantScore(100);
		bidder3.setApplicantScore(100);
		
		bidder1.setProjectCostScore(100);
		bidder2.setProjectCostScore(100);
		bidder3.setProjectCostScore(100);
		
		
		List<Bidder> qualified = new ArrayList<Bidder>();
		qualified.add(bidder1);
		qualified.add(bidder2);
		qualified.add(bidder3);
		Collections.sort(qualified, new Comparator<Bidder>() {
			public int compare(Bidder x, Bidder y) {
				return tieBreakCompare(x, y);
			}
		});
		
		for (int i = 0; i < qualified.size(); i++) {
			qualified.get(i).rank = i + 1;
		}
		for(Bidder bidder:qualified) {
			System.out.println(bidder.getRank()+" : "+bidder.getName());
		}
	}

	static int tieBreakCompare(Bidder x, Bidder y) { 
		System.out.println("name "+y.getName());
		System.out.println("name "+x.getName());
		
		int byScore = Double.compare(y.applicantScore, x.applicantScore);
		System.out.println("byScore "+byScore);
		if (byScore != 0)
		return byScore;
		
		int byProjectCostScore = Double.compare(y.projectCostScore, x.projectCostScore);
		System.out.println("byProjectCostScore "+byProjectCostScore+" "+y.projectCostScore+" "+x.projectCostScore);
        if (byProjectCostScore != 0) return byProjectCostScore;
        
        return 0;
	}
}
