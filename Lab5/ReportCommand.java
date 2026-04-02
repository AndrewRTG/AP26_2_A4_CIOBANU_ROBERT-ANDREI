import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class ReportCommand implements Command {
    private ResourceRepository repository;

    public ReportCommand(ResourceRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() throws Exception {

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);

        // Ii dam la freemarker locatia curenta pentru fisierul report.ftl
        cfg.setClassForTemplateLoading(this.getClass(), "/");
        cfg.setDefaultEncoding("UTF-8"); //setam caracterele default pentru limbaj
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER); // daca in report.ftl am scris numele atributelor gresit sau numele autorului este null in lista de resurse va arunca o eroare pe care o prindem cu catch in main

        //  Map-ul care face legatura intre Java și HTML
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("resurse", repository.getResources());

        //incarca sablonul
        Template template = cfg.getTemplate("report.ftl");

        //genereaza html ul
        File outputFile = new File("raport_catalog.html");
        Writer consoleWriter = new FileWriter(outputFile);
        template.process(dataModel, consoleWriter);
        consoleWriter.close();

        System.out.println("Raport generat cu succes la: " + outputFile.getAbsolutePath());

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(outputFile.toURI());
        } else {
            System.err.println("Eroare: Sistemul nu suportă deschiderea automată a fișierului.");
        }
    }
}