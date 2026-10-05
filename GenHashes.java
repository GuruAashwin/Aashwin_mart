import org.mindrot.jbcrypt.BCrypt;

public class GenHashes {
    public static void main(String[] args) {
        String[] passwords = {"Admin@123", "Seller@123", "Buyer@123"};
        for (String pw : passwords) {
            String hash = BCrypt.hashpw(pw, BCrypt.gensalt(12));
            System.out.println(pw + " => " + hash);
            System.out.println("verify=" + BCrypt.checkpw(pw, hash));
        }
    }
}
