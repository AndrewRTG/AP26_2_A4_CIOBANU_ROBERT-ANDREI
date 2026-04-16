import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReportGenerator {

    public void generateHtmlReport(String templatePath, String outputPath) {
        StringBuilder rowsHtml = new StringBuilder();


        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM movie_report_view")) {

            while (rs.next()) {
                rowsHtml.append("<tr>")
                        .append("<td>").append(rs.getString("title")).append("</td>")
                        .append("<td>").append(rs.getDate("release_date")).append("</td>")
                        .append("<td>").append(rs.getInt("duration")).append("</td>")
                        .append("<td>").append(rs.getDouble("score")).append("</td>")
                        .append("<td>").append(rs.getString("genre")).append("</td>")
                        .append("<td>").append(rs.getString("actors") != null ? rs.getString("actors") : "Fără actori").append("</td>")
                        .append("</tr>\n");
            }

        } catch (SQLException e) {
            System.err.println("Eroare la citirea din baza de date: " + e.getMessage());
            return;
        }

        try {
            Path templateFile = Paths.get(templatePath);
            String templateContent = new String(Files.readAllBytes(templateFile));
            String finalHtml = templateContent.replace("{{ROWS}}", rowsHtml.toString());

            Path outputFile = Paths.get(outputPath);
            Files.write(outputFile, finalHtml.getBytes());

            System.out.println("Raportul HTML a fost generat cu succes la: " + outputFile.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Eroare la procesarea fișierelor HTML: " + e.getMessage());
        }
    }
}