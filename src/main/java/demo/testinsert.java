package demo;

public class testinsert {
	public static void main(String[] args) {
		String latestBid = BidService.getLatestBid();
		BidWebSocket.broadcast(latestBid);
		System.out.println("latestBid "+latestBid);
	}
}
