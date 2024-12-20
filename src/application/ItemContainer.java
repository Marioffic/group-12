package application;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.scene.Node;

public class ItemContainer implements FarmComponent {
    private String name;
    private List<FarmComponent> children = new ArrayList<>();
    private int x, y;
    private int width = 50;
    private int height = 50;
    private double price;

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
    
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    
    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

	public List<Item> getItems() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
