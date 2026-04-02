
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
public class ResourceRepository implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private List<Resource> resources = new ArrayList<>();
    public ResourceRepository(String name) {
        this.name = name;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public Resource findResource(String id) {
        for (Resource resource : resources) {
            if (resource.getId().equals(id)) {
                return resource;
            }
        }
        return null;
    }

}
