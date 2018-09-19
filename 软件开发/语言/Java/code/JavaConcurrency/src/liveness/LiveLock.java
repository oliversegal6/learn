package threads.liveness;

public class LiveLock {
	public static void main(String[] args) throws InterruptedException {
		Buyer buyer = new Buyer();
		Seller seller = new Seller();
		
		new Thread(()->	buyer.payMoney(seller)).start();
		new Thread(()-> seller.deliverProduct(buyer)).start();
	}
}

class Buyer {
	boolean moneyPaid;
	
	public void payMoney(Seller seller) {
		while (!seller.isProductDelived()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Product not received yet ...");
		}
		moneyPaid = true;
		System.out.println("Buyer paying money ...");
	}

	public boolean isMoneyPaid() {
		return moneyPaid;
	}
}

class Seller {
	boolean productDelived;
	
	public void deliverProduct(Buyer buyer) {
		while (!buyer.isMoneyPaid()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Money not received yet ...");
		}
		productDelived = true;
		System.out.println("Seller delivering product!");
	}

	public boolean isProductDelived() {
		return productDelived;
	}
	
}
