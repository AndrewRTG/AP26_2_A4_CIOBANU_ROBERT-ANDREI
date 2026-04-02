public enum Concept {
    GRAPH_THEORY("Graph theory"),
    NEURAL_NETWORKS("Neural Networks"),
    ALGORITHM_DESIGN("Algorithm design techniques"),
    OOP("Object-oriented programming"),
    WEB_DEVELOPMENT("Web Development"),
    MUSIC("Music"),
    GENERAL("General/Other");

    private String displayName;

    Concept(String displayName) {
        this.displayName = displayName;
    }


    @Override
    public String toString() {
        return displayName;
    }

    public static class AddCommand implements Command {
        private ResourceRepository resourceRepository;
        private Resource resource;
        public AddCommand(ResourceRepository resourceRepository, Resource resource) {
            this.resourceRepository = resourceRepository;
            this.resource = resource;
        }

        @Override
        public void execute() {
            resourceRepository.getResources().add(resource);
            System.out.println("S-a adăugat: " + resource.getTitle());
        }

    }
}