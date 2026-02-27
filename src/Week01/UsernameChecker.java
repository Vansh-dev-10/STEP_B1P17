package Week01;

import java.util.*;

public class UsernameChecker {

    private HashSet<String> usernames;
    private HashMap<String, Integer> attemptCount;

    public UsernameChecker() {
        usernames = new HashSet<>();
        attemptCount = new HashMap<>();

        // Existing users (simulating 10 million users)
        usernames.add("john_doe");
        usernames.add("admin");
        usernames.add("root");
        usernames.add("test");
    }

    // O(1) Username availability check
    public boolean checkAvailability(String username) {
        attemptCount.put(username, attemptCount.getOrDefault(username, 0) + 1);
        return !usernames.contains(username);
    }

    // Suggest alternatives if username is taken
    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            String newName = username + i;
            if (!usernames.contains(newName)) {
                suggestions.add(newName);
            }
        }

        String dotVersion = username.replace("_", ".");
        if (!usernames.contains(dotVersion)) {
            suggestions.add(dotVersion);
        }

        return suggestions;
    }

    // Track most attempted username
    public String getMostAttempted() {
        String most = null;
        int max = 0;

        for (String key : attemptCount.keySet()) {
            if (attemptCount.get(key) > max) {
                max = attemptCount.get(key);
                most = key;
            }
        }

        return most;
    }

    public static void main(String[] args) {
        UsernameChecker checker = new UsernameChecker();

        System.out.println("john_doe available? " + checker.checkAvailability("john_doe"));
        System.out.println("jane_smith available? " + checker.checkAvailability("jane_smith"));

        System.out.println("Suggestions for john_doe: " + checker.suggestAlternatives("john_doe"));

        // Simulate heavy attempts
        for (int i = 0; i < 10543; i++) {
            checker.checkAvailability("admin");
        }

        System.out.println("Most attempted: " + checker.getMostAttempted());
    }
}
