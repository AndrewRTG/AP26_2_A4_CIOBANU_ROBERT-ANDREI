import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class SaveCommand implements Command {
    private ResourceRepository repository;
    private String path;

    public SaveCommand(ResourceRepository repository, String path) {
        this.repository = repository;
        this.path = path;
    }

    @Override
    public void execute() throws Exception {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(repository);
            System.out.println("💾 Catalogul a fost salvat cu succes în: " + path);
        }
    }
}