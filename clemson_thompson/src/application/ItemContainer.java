package application;
import java.util.ArrayList;
import java.util.List;

public class ItemContainer implements FarmComponent {
    private String name;
    private List<FarmComponent> children = new ArrayList<>();
    private int x, y;

    public ItemContainer(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public ItemContainer(String name) {
        this.name = name;
    }

    @Override
    public void rename(String newName) {
        this.name = newName;
    }

    @Override
    public void remove() {
        for (FarmComponent child : children) {
            child.remove();
        }
    }

    public void add(FarmComponent component) {
        children.add(component);
    }

    public void remove(FarmComponent component) {
        children.remove(component);
    }

    @Override
    public void display() {
        System.out.println("ItemContainer: " + name);
        for (FarmComponent child : children) {
            child.display();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public List<FarmComponent> getChildren() {
        return children;
    }
    
    @Override
    public String toString() {
        return name; // Use the name of the container for display purposes
    }

}
