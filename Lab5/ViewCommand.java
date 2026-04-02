import java.awt.Desktop;
import java.io.File;
import java.net.URI;

public class ViewCommand implements Command {
    private Resource resource;
    public ViewCommand(Resource resource) {
        this.resource = resource;
    }
    @Override
    public void execute() throws Exception {
        Desktop desktop = Desktop.getDesktop();
        try {
            String location = resource.getLocation();

            if (location.startsWith("http://") || location.startsWith("https://")) {
                desktop.browse(new URI(location));
            } else {
                File file = new File(location);
                if (file.exists()) {
                    desktop.open(file);
                } else {
                    throw new InvalidResourceException("Fișierul nu a putut fi găsit fizic la calea: " + location);
                }
            }
        } catch (Exception e) {
            System.err.println("Eroare la deschiderea resursei: " + e.getMessage());
        }
    }
}
