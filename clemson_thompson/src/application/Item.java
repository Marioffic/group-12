package application;

public class Item implements FarmComponent {
    private String name;
    private int x, y;
    private double price;

    public Item(String name, int x, int y) {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Item(String name) {
        this.name = name;
    }

    @Override
    public void rename(String newName) {
        this.name = newName;
    }

    @Override
    public void remove() {
        // Implement remove logic if necessary
    }

    @Override
    public void display() {
        System.out.println("Item: " + name);
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name; // Use the name of the item for display purposes
    }

}
