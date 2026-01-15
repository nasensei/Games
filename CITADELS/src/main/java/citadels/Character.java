package citadels;

public class Character {
    private String name;
    private String description;
    private int order;

    public Character(String name, String description, int order) {
        this.name = name;
        this.description = description;
        this.order = order;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public int getOrder() {
        return order;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
