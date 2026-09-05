import java.util.List;

public class Bidder {

	String name;
	double viabilityAmountScore;
	double plantCapacityScore;
	double projectCostScore;
	double technologyTieUpScore;
	double financialStrengthScore;
	double applicantScore;
	boolean qualified;
	int rank;
	boolean prjctUreaOrSynthetic;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getViabilityAmountScore() {
		return viabilityAmountScore;
	}
	public void setViabilityAmountScore(double viabilityAmountScore) {
		this.viabilityAmountScore = viabilityAmountScore;
	}
	public double getPlantCapacityScore() {
		return plantCapacityScore;
	}
	public void setPlantCapacityScore(double plantCapacityScore) {
		this.plantCapacityScore = plantCapacityScore;
	}
	public double getProjectCostScore() {
		return projectCostScore;
	}
	public void setProjectCostScore(double projectCostScore) {
		this.projectCostScore = projectCostScore;
	}
	public double getTechnologyTieUpScore() {
		return technologyTieUpScore;
	}
	public void setTechnologyTieUpScore(double technologyTieUpScore) {
		this.technologyTieUpScore = technologyTieUpScore;
	}
	public double getFinancialStrengthScore() {
		return financialStrengthScore;
	}
	public void setFinancialStrengthScore(double financialStrengthScore) {
		this.financialStrengthScore = financialStrengthScore;
	}
	public double getApplicantScore() {
		return applicantScore;
	}
	public void setApplicantScore(double applicantScore) {
		this.applicantScore = applicantScore;
	}
	public boolean isQualified() {
		return qualified;
	}
	public void setQualified(boolean qualified) {
		this.qualified = qualified;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public boolean isPrjctUreaOrSynthetic() {
		return prjctUreaOrSynthetic;
	}
	public void setPrjctUreaOrSynthetic(boolean prjctUreaOrSynthetic) {
		this.prjctUreaOrSynthetic = prjctUreaOrSynthetic;
	}

	
}
