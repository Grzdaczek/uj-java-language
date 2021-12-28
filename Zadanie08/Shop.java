import java.util.HashMap;
import java.util.Map;

public class Shop implements ShopInterface {
	
	Map<String, Integer> warehouse = new HashMap<String, Integer>();
	Map<String, Object> locks = new HashMap<String, Object>();
	
	@Override
	public void delivery(Map<String, Integer> goods) {
		goods.forEach((key, value) -> {
			synchronized (warehouse) {
				warehouse.merge(key, value, (a, b) -> a + b);
			}

			if (locks.containsKey(key)) {
				Object lock = locks.get(key);
				synchronized (lock) {
					lock.notifyAll();
				}
			}
		});
	}

	@Override
	public boolean purchase(String productName, int quantity) {
		if (!locks.containsKey(productName))
			locks.put(productName, new Object());

		Object lock = locks.get(productName);

		synchronized (lock) {
			synchronized (warehouse) {
				Integer available = warehouse.get(productName);
				if (available != null && available >= quantity) {
					warehouse.put(productName, available - quantity);
					return true;
				}
			}
			
			try {
				lock.wait();
			}
			catch (Exception e) {
				return false;
			}
			
			synchronized (warehouse) {
				Integer available = warehouse.get(productName);
				if (available != null && available >= quantity) {
					warehouse.put(productName, available - quantity);
					return true;
				}
			}

			return false;
		}
	}

	@Override
	public Map<String, Integer> stock() {
		return warehouse;
	}
}
