public class ListCommand implements Command {
    private ResourceRepository resourceRepository;
    public ListCommand(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }



    @Override
    public void execute() throws Exception {
        System.out.println("Lista resurselor:");
        if (resourceRepository.getResources().isEmpty()) {
            System.out.println("Nu exista resurse in repository!");
        }
        else {
            for (Resource resource : resourceRepository.getResources()) {
                System.out.println(resource);
            }
        }


    }


}
