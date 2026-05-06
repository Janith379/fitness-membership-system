import java.nio.file.*;
import java.util.List;
import java.time.LocalDate;

public class Test {
    public static void main(String[] args) throws Exception {
        String filePath = "src/main/resources/data/members.txt";
        List<String> allLines = Files.readAllLines(Paths.get(filePath));
        System.out.println("Total lines: " + allLines.size());
        for (int i = 1; i < allLines.size(); i++) {
            String line = allLines.get(i).trim();
            if (!line.isEmpty()) {
                String[] parts = line.split(",", -1);
                System.out.println("Line " + i + " parts length: " + parts.length);
                if (parts.length < 12) {
                    System.out.println("  -> less than 12 parts!");
                    continue;
                }
                try {
                    String memberId = parts[0].trim();
                    String fullName = parts[1].trim();
                    String email = parts[2].trim();
                    String phone = parts[3].trim();
                    int age = Integer.parseInt(parts[4].trim());
                    String gender = parts[5].trim();
                    String membershipType = parts[6].trim();
                    int durationMonths = Integer.parseInt(parts[7].trim());
                    LocalDate joinDate = parts[8].trim().isEmpty() ? null : LocalDate.parse(parts[8].trim());
                    LocalDate expiryDate = parts[9].trim().isEmpty() ? null : LocalDate.parse(parts[9].trim());
                    String notes = parts.length > 11 ? parts[11].trim() : "";
                    System.out.println("  -> Parsed successfully: " + memberId);
                } catch (Exception e) {
                    System.out.println("  -> Exception: " + e.getMessage());
                }
            }
        }
    }
}
