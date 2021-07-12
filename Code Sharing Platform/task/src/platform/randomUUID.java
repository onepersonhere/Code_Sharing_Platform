package platform;
import java.util.UUID;

public class randomUUID {
    public static String getUUID() {
        // Creating a random UUID (Universally unique identifier).
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}