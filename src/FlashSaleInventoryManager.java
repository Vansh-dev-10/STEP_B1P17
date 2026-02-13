import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FlashSaleInventoryManager {

    // Product stock storage (O(1) lookup)
    private ConcurrentHashMap<String, Integer> stockMap;

    // Waiting list (FIFO)
    private LinkedHashMap<String, Queue<Integer>> waitingList;

    public FlashSaleInventoryManager() {
        stockMap = new ConcurrentHashMap<>();
        waitingList = new LinkedHashMap<>();
    }

    // Add product stock
    public void addProduct(String productId, int stock) {
        stockMap.put(productId, stock);
        waitingList.put(productId, new LinkedList<>());
    }

    // Instant stock check (O(1))
    public String checkStock(String productId) {
        int stock = stockMap.getOrDefault(productId, 0);
        return productId + " → " + stock + " units available";
    }

    // Thread-safe purchase operation
    public synchronized String purchaseItem(String productId, int userId) {

        int stock = stockMap.getOrDefault(productId, 0);

        // Stock available
        if (stock > 0) {
            stockMap.put(productId, stock - 1);
            return "User " + userId + " → Purchase SUCCESS, " +
                    (stock - 1) + " units remaining";
        }

        // Stock finished → Add to waiting list
        Queue<Integer> queue = waitingList.get(productId);
        queue.add(userId);

        return "User " + userId + " → Stock OUT, added to waiting list, position #"
                + queue.size();
    }

    // Show waiting list
    public void showWaitingList(String productId) {
        Queue<Integer> queue = waitingList.get(productId);

        System.out.println("\nWaiting List for " + productId + ":");
        if (queue.isEmpty()) {
            System.out.println("No users waiting.");
        } else {
            int pos = 1;
            for (int user : queue) {
                System.out.println("Position #" + pos + " → User " + user);
                pos++;
            }
        }
    }

    // Main simulation
    public static void main(String[] args) {

        FlashSaleInventoryManager manager = new FlashSaleInventoryManager();

        // Add product with 100 stock
        manager.addProduct("IPHONE15_256GB", 100);

        // Check stock
        System.out.println(manager.checkStock("IPHONE15_256GB"));

        // Simulate purchases
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));

        // Simulate 100 purchases
        for (int i = 1; i <= 100; i++) {
            manager.purchaseItem("IPHONE15_256GB", i);
        }

        // Stock finished → waiting list
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 99999));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 88888));

        // Show waiting list
        manager.showWaitingList("IPHONE15_256GB");
    }
}
