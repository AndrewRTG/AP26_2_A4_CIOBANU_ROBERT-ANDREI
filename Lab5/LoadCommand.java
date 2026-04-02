import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class LoadCommand implements Command {
    private String path;
    private ResourceRepository repository;

    public LoadCommand(ResourceRepository repository, String path) {
        this.repository = repository;
        this.path = path;
    }

    @Override
    public void execute() throws Exception {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
            ResourceRepository loadedRepo = (ResourceRepository) in.readObject();


            repository.getResources().clear();
            repository.getResources().addAll(loadedRepo.getResources());

            System.out.println("📂 Catalogul a fost încărcat din: " + path);
        }
    }
}