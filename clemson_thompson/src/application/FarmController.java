package application;

import java.net.URL;


import java.util.ResourceBundle;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import java.util.Map;
import java.util.HashMap;






public class FarmController implements Initializable {
	
	private static final int DEFAULT_X = 100;
	private static final int DEFAULT_Y = 100;
	private static final int DEFAULT_WIDTH = 50;
	private static final int DEFAULT_HEIGHT = 50;
	private Map<FarmComponent, javafx.scene.Node> componentVisuals = new HashMap<>();



    @FXML
    private TreeView<FarmComponent> listofFarm;
    
    
    
    // Buttons for adding new items and item containers
    @FXML
    private Button addItemButton;
    @FXML
    private Button addItemContainerButton;

    // Buttons for item commands
    @FXML
    private Button renameButton;
    @FXML
    private Button changeLocationButton;
    @FXML
    private Button changePriceButton;
    @FXML
    private Button changeDimensionsButton;
    @FXML
    private Button deleteButton;

    // Drone action radio buttons
    @FXML
    private RadioButton scanFarmRadioButton;
    @FXML
    private RadioButton sprayPesticideRadioButton;
    @FXML
    private RadioButton locateEquipmentRadioButton;
    @FXML
    private RadioButton checkCropsRadioButton;

    @FXML
    private ToggleGroup droneActionsGroup;

    private ItemContainer rootContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Create the root container "Farm"
        rootContainer = new ItemContainer("Farm");

        // Create the TreeView root node and set the root container as its value
        TreeItem<FarmComponent> rootNode = new TreeItem<>(rootContainer);
        rootNode.setExpanded(true); // Ensure the root node is always expanded

        // Set the TreeView root to the farm root node
        listofFarm.setRoot(rootNode);
        listofFarm.setShowRoot(true);

        // Clear visualization pane (if needed, for consistency)
        visualizationPane.getChildren().clear();

        System.out.println("TreeView initialized with only the root 'Farm'.");
    }

    
    @FXML
    private AnchorPane visualizationPane;

    @FXML
    public void addItem(ActionEvent event) {
        TreeItem<FarmComponent> selectedItem = listofFarm.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            TextInputDialog dialog = new TextInputDialog("New Item");
            dialog.setTitle("Add Item");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter the item name:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                // Create the new item with default coordinates
                Item newItem = new Item(name, DEFAULT_X, DEFAULT_Y);

                if (selectedItem.getValue() instanceof ItemContainer) {
                    // Add to the selected container
                    ((ItemContainer) selectedItem.getValue()).add(newItem);
                    selectedItem.getChildren().add(new TreeItem<>(newItem));

                    // Create the visual representation and store it in the map
                    Circle itemVisual = new Circle(DEFAULT_X, DEFAULT_Y, 10, Color.GREEN);
                    visualizationPane.getChildren().add(itemVisual);
                    componentVisuals.put(newItem, itemVisual); // Map the item to its visual
                } else {
                    showAlert("Invalid Selection", "You can only add items to an item container.");
                }
            });
        } else {
            showAlert("No container selected", "Please select a container to add an item.");
        }
    }

    @FXML
    public void addItemContainer(ActionEvent event) {
        TreeItem<FarmComponent> selectedItem = listofFarm.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            TextInputDialog dialog = new TextInputDialog("New Item-Container");
            dialog.setTitle("Add Item-Container");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter the container name:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                // Create the new item container with default coordinates
                ItemContainer newContainer = new ItemContainer(name, DEFAULT_X, DEFAULT_Y);

                if (selectedItem.getValue() instanceof ItemContainer) {
                    // Add to the selected container
                    ((ItemContainer) selectedItem.getValue()).add(newContainer);
                    selectedItem.getChildren().add(new TreeItem<>(newContainer));

                    // Create the visual representation and store it in the map
                    Rectangle containerVisual = new Rectangle(
                        DEFAULT_X, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT
                    );
                    containerVisual.setFill(Color.LIGHTBLUE);
                    containerVisual.setStroke(Color.BLUE);
                    visualizationPane.getChildren().add(containerVisual);
                    componentVisuals.put(newContainer, containerVisual); // Map the container to its visual
                } else {
                    showAlert("Invalid Selection", "You can only add item containers to an existing container.");
                }
            });
        } else {
            showAlert("No container selected", "Please select a container to add an item-container.");
        }
    }


    // Method to rename a selected item in the TreeView
    @FXML
    public void renameItem(ActionEvent event) {
        TreeItem<FarmComponent> selectedItem = listofFarm.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            TextInputDialog dialog = new TextInputDialog(selectedItem.getValue().getName());
            dialog.setTitle("Rename Item");
            dialog.setHeaderText(null);
            dialog.setContentText("Please enter the new name:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newName -> {
                selectedItem.getValue().rename(newName);
                selectedItem.setValue(selectedItem.getValue());
            });
        } else {
            showAlert("No item selected", "Please select an item to rename.");
        }
    }

    // Method to change the location of a selected item
    @FXML
    private void changeLocation(ActionEvent event) {
        TreeItem<FarmComponent> selectedItem = listofFarm.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Change Location");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter new coordinates (x, y):");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(input -> {
                try {
                    String[] coordinates = input.split(",");
                    int x = Integer.parseInt(coordinates[0].trim());
                    int y = Integer.parseInt(coordinates[1].trim());

                    FarmComponent selectedComponent = selectedItem.getValue();
                    if (selectedComponent instanceof Item) {
                        ((Item) selectedComponent).setCoordinates(x, y);
                    } else if (selectedComponent instanceof ItemContainer) {
                        ((ItemContainer) selectedComponent).setCoordinates(x, y);
                    }

                    // Update the existing visual
                    javafx.scene.Node visual = componentVisuals.get(selectedComponent);
                    if (visual instanceof Circle) {
                        ((Circle) visual).setCenterX(x);
                        ((Circle) visual).setCenterY(y);
                    } else if (visual instanceof Rectangle) {
                        ((Rectangle) visual).setX(x);
                        ((Rectangle) visual).setY(y);
                    }
                } catch (Exception e) {
                    showAlert("Invalid Input", "Please enter valid integer coordinates in the format x,y.");
                }
            });
        } else {
            showAlert("No Item Selected", "Please select an item to change its location.");
        }
    }


    // Method to change the price of a selected item
    @FXML
    private void changePrice(ActionEvent event) {
        TreeItem<FarmComponent> selectedItem = listofFarm.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            System.out.println("Changing price for: " + selectedItem.getValue().getName());
        } else {
            showAlert("No item selected", "Please select an item to change its price.");
        }
    }

    // Method to change dimensions of a selected item
    @FXML
    private void changeDimensions(ActionEvent event) {
        TreeItem<FarmComponent> selectedItem = listofFarm.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            if (selectedItem.getValue() instanceof ItemContainer) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Change Dimensions");
                dialog.setHeaderText(null);
                dialog.setContentText("Enter new dimensions (width, height):");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(input -> {
                    try {
                        String[] dimensions = input.split(",");
                        int width = Integer.parseInt(dimensions[0].trim());
                        int height = Integer.parseInt(dimensions[1].trim());

                        ItemContainer container = (ItemContainer) selectedItem.getValue();
                        container.setDimensions(width, height);

                        // Update the existing visual
                        javafx.scene.Node visual = componentVisuals.get(container);
                        if (visual instanceof Rectangle) {
                            ((Rectangle) visual).setWidth(width);
                            ((Rectangle) visual).setHeight(height);
                        }
                    } catch (NumberFormatException e) {
                        showAlert("Invalid Input", "Please enter valid integer dimensions in the format width,height.");
                    }
                });
            } else {
                showAlert("Invalid Selection", "Only item containers can have their dimensions changed.");
            }
        } else {
            showAlert("No Item Selected", "Please select an item-container to change its dimensions.");
        }
    }


    // Method to delete a selected item from the TreeView
    @FXML
    private void deleteItem(ActionEvent event) {
        TreeItem<FarmComponent> selectedItem = listofFarm.getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.getParent() != null) {
            if (selectedItem.getParent().getValue() instanceof ItemContainer) {
                ItemContainer parentContainer = (ItemContainer) selectedItem.getParent().getValue();
                parentContainer.remove(selectedItem.getValue());
            }

            javafx.scene.Node visual = componentVisuals.remove(selectedItem.getValue());
            if (visual != null) {
                visualizationPane.getChildren().remove(visual);
            }

            selectedItem.getParent().getChildren().remove(selectedItem);
        } else {
            showAlert("No Item Selected", "Please select an item to delete.");
        }
    }

    @FXML
    public void selectItem(MouseEvent event) {
        TreeItem<FarmComponent> selectedItem = listofFarm.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            System.out.println("Selected item: " + selectedItem.getValue().getName());
        } else {
            showAlert("No item selected", "Please select an item from the list.");
        }
    }
    
    private void updateVisualization() { 
        visualizationPane.getChildren().clear();

        renderComponent(rootContainer);
    }

    private void renderComponent(FarmComponent component) {
        if (component instanceof Item) {
            // Render Item as a circle
            Item item = (Item) component;

            // Get the existing visual from the map
            Circle itemVisual = (Circle) componentVisuals.get(component);
            if (itemVisual == null) {
                // Create a new circle if not already present
                itemVisual = new Circle(item.getX(), item.getY(), 10, Color.GREEN);
                componentVisuals.put(component, itemVisual);
                visualizationPane.getChildren().add(itemVisual);
            } else {
                // Update the existing circle's position
                itemVisual.setCenterX(item.getX());
                itemVisual.setCenterY(item.getY());
                // Ensure the visual is added to the pane
                if (!visualizationPane.getChildren().contains(itemVisual)) {
                    visualizationPane.getChildren().add(itemVisual);
                }
            }

        } else if (component instanceof ItemContainer) {
            // Render ItemContainer as a rectangle
            ItemContainer container = (ItemContainer) component;

            // Get the existing visual from the map
            Rectangle containerVisual = (Rectangle) componentVisuals.get(component);
            if (containerVisual == null) {
                // Create a new rectangle if not already present
                containerVisual = new Rectangle(
                    container.getX(), container.getY(), container.getWidth(), container.getHeight()
                );
                containerVisual.setFill(Color.LIGHTBLUE);
                containerVisual.setStroke(Color.BLUE);
                componentVisuals.put(component, containerVisual);
                visualizationPane.getChildren().add(containerVisual);
            } else {
                // Update the existing rectangle's position and size
                containerVisual.setX(container.getX());
                containerVisual.setY(container.getY());
                containerVisual.setWidth(container.getWidth());
                containerVisual.setHeight(container.getHeight());
                // Ensure the visual is added to the pane
                if (!visualizationPane.getChildren().contains(containerVisual)) {
                    visualizationPane.getChildren().add(containerVisual);
                }
            }

            // Recursively render children
            for (FarmComponent child : container.getChildren()) {
                renderComponent(child);
            }
        }
    }


    // Method to handle drone actions based on radio button selection
    @FXML
    public void handleDroneAction(ActionEvent event) {
        RadioButton source = (RadioButton) event.getSource();
        System.out.println("Drone action selected: " + source.getText());
    }

    // Utility method to show alert messages
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
}
