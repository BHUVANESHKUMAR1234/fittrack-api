import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class BcryptCheck {
  public static void main(String[] args) {
    BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
    String raw = "Test@1234";
    String hash = "$2a$10$XaNU5jBX3hJYEYZT1x7.9.TWKcGJYLzzRCQ5nE2wcHCoK3gQ1CLcS";
    System.out.println(enc.matches(raw, hash));
  }
}
