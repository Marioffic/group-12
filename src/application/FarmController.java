package application;

import java.net.URL;



import java.util.ResourceBundle;
import java.util.Optional;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import java.util.Map;
import java.util.HashMap;
import javafx.scene.control.Label;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;







public class FarmController implements Initializable {
	
	private static final int DEFAULT_X = 100;
	private static final int DEFAULT_Y = 100;
	private static final int DEFAULT_WIDTH = 50;
	private static final int DEFAULT_HEIGHT = 50;
	// private Map<FarmComponent, javafx.scene.Node> componentVisuals = new HashMap<>();
	private Map<FarmComponent, List<javafx.scene.Node>> componentVisuals = new HashMap<>();




    @FXML
    private TreeView<FarmComponent> listofFarm;
    
    @FXML
    public void scanFarm(ActionEvent event) {
        if (scanFarm.isSelected()) {
            performFarmScan();
        }
    }
    @FXML
    private void performFarmScan() {
        if (drone == null) {
            System.out.println("Drone is null");
            return;
        }

        // Assuming droneVisuals is correctly initialized with the drone's image view
        List<javafx.scene.Node> droneVisuals = componentVisuals.get(drone);
        if (droneVisuals == null || droneVisuals.isEmpty()) {
            System.out.println("No visuals found for drone");
            return;
        }

        // Get the drone's image view (assuming it's the first visual in the list)
        ImageView droneImageView = (ImageView) droneVisuals.get(0);

        // Reset drone's position to start at (0, 0) before starting the scan
        double startX = 0;
        double startY = DEFAULT_Y - 90;
        droneImageView.setLayoutX(startX);
        droneImageView.setLayoutY(startY);

        // Debug print current drone position
        System.out.println("Drone start position: X=" + droneImageView.getLayoutX() + ", Y=" + droneImageView.getLayoutY());

        // Create a list to store all container locations
        List<double[]> containerLocations = new ArrayList<>();

        // Recursively collect all container locations (checking each item container's position)
        collectContainerLocations(rootContainer, containerLocations);

        // Debug print container locations
        System.out.println("Found " + containerLocations.size() + " container locations:");
        for (double[] location : containerLocations) {
            System.out.println("Container location: X=" + location[0] + ", Y=" + location[1]);
        }

        // Create a sequential transition to move the drone through all locations
        SequentialTransition farmScanSequence = new SequentialTransition();

        // First, move to each container (use absolute coordinates)
        for (double[] location : containerLocations) {
            // Create a TranslateTransition to move the drone to the container
        	 droneImageView.toFront();
            TranslateTransition moveToContainer = new TranslateTransition(Duration.seconds(1.5), droneImageView);
            System.out.println("Moving drone to: X=" + location[0] + ", Y=" + location[1]);
            droneImageView.toFront();


            // Set the absolute target positions using setToX and setToY
            moveToContainer.setToX(location[0]);
            moveToContainer.setToY(location[1]);
            
            // Add the move transition to the sequence
            System.out.println("Found " + containerLocations.size() + " container locations:");
            farmScanSequence.getChildren().add(moveToContainer);
        }

        // After the last container, return to the starting position (e.g., (0, 0))
        TranslateTransition returnToStart = new TranslateTransition(Duration.seconds(1.5), droneImageView);
        returnToStart.setToX(0);
        returnToStart.setToY(startY);  // Make sure it returns to the same Y position (not 0)
        farmScanSequence.getChildren().add(returnToStart);

        // Play the entire sequence of movements
        droneImageView.toFront();
        farmScanSequence.play();
    }

    private void collectContainerLocations(FarmComponent component, List<double[]> locations) {
        System.out.println("Checking component: " + component.getClass().getSimpleName());

        // Ensure the component is an actual ItemContainer and not something else
        if (component instanceof ItemContainer) {
            ItemContainer itemContainer = (ItemContainer) component;

            double x = itemContainer.getX();
            double y = itemContainer.getY();

            // Check for valid coordinates (ensure non-zero, non-null coordinates)
            if (x != 0 && y != 0) {  // Adjust this check based on your needs
                // Debug output to verify the correct coordinates are being collected
                System.out.println("Found container at: X=" + x + ", Y=" + y);

                locations.add(new double[] { x, y });
            } else {
                // Debug log for invalid locations (you can remove or comment this after testing)
                System.out.println("Skipping invalid container at: X=" + x + ", Y=" + y);
            }

            // Recursively collect locations from child containers
            for (FarmComponent child : itemContainer.getChildren()) {
                collectContainerLocations(child, locations);
            }
        }
    }



    
    
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

 
    @FXML
    private RadioButton scanFarm;
    
    @FXML
    private RadioButton visitObject;
    @FXML
    private RadioButton visitItem;

    @FXML
    private ToggleGroup droneActionsGroup;

    private ItemContainer rootContainer;
    
    private Item drone;  // Declare the drone object as a class member
    
    


    @Override
    public void initialize(URL location, ResourceBundle resources) {
  
        rootContainer = new ItemContainer("Farm");

        scanFarm.setOnAction(this::scanFarm);
        TreeItem<FarmComponent> rootNode = new TreeItem<>(rootContainer);
        rootNode.setExpanded(true); 

        listofFarm.setRoot(rootNode);
        listofFarm.setShowRoot(true);

        visualizationPane.getChildren().clear();
        
        ItemContainer commandCenter = new ItemContainer("Command Center", 0, 0);
        rootContainer.add(commandCenter);

        
        
        
        
        // Create a TreeItem for the "Command Center"
        TreeItem<FarmComponent> commandCenterNode = new TreeItem<>(commandCenter);
        rootNode.getChildren().add(commandCenterNode);

        // Create visuals for "Command Center"
        Rectangle commandCenterVisual = new Rectangle(0, DEFAULT_Y - 80, DEFAULT_WIDTH , DEFAULT_HEIGHT );
        commandCenterVisual.setFill(Color.LIGHTGREEN);
        commandCenterVisual.setStroke(Color.DARKGREEN);
        visualizationPane.getChildren().add(commandCenterVisual);

        Label commandCenterLabel = new Label("Command Center");
        commandCenterLabel.setLayoutX(0 );
        commandCenterLabel.setLayoutY(DEFAULT_Y - 100 );
        visualizationPane.getChildren().add(commandCenterLabel);

        // Store the visuals in the componentVisuals map
        componentVisuals.put(commandCenter, Arrays.asList(commandCenterVisual, commandCenterLabel));


        System.out.println("TreeView initialized with only the root 'Farm'.");
        
        
        
        
        
        drone = new Item("Drone", 0, DEFAULT_Y - 90); // Adjust the coordinates for the drone
        commandCenter.add(drone);
        
        String droneImagePath = "droneMain.png"; 
        Image droneImage = new Image(getClass().getResourceAsStream(droneImagePath));

        // Create visuals for the "Drone" item
        
        
        
        
        ImageView droneImageView = new ImageView(droneImage);
        droneImageView.setFitWidth(50);  // Set the width of the image (optional)
        droneImageView.setFitHeight(50);  // Set the height of the image (optional)
        droneImageView.setLayoutX(0);  // Set X position for the image
        droneImageView.setLayoutY(DEFAULT_Y - 90);  // Set Y position for the image
        
        visualizationPane.getChildren().add(droneImageView);
        
        
    

        // Create a label for the "Drone"
      
        // Store the visuals for the "Drone" item
        componentVisuals.put(drone, Arrays.asList(droneImageView));
     // Set the Z-index of the drone image view to a high value (e.g., 1 or higher)
        // Higher value means it will be rendered on top

    }

    
    @FXML
    private AnchorPane visualizationPane;
    
    @FXML
    public void addItem(ActionEvent event) {
        TreeItem<FarmComponent> selectedItem = listofFarm.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Open the dialog to get the item name from the user
            TextInputDialog dialog = new TextInputDialog("New Item");
            dialog.setTitle("Add Item");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter the item name:");

            // Get the result from the dialog
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                Item newItem = new Item(name, DEFAULT_X, DEFAULT_Y);

                // Check if selectedItem is an instance of ItemContainer
                if (selectedItem.getValue() instanceof ItemContainer) {
                	
                	ItemContainer container = (ItemContainer) selectedItem.getValue();
                	

                	
                	
                    ((ItemContainer) selectedItem.getValue()).add(newItem);
                    selectedItem.getChildren().add(new TreeItem<>(newItem));

                    // Ensure the container has less than 4 items
                    if (container.getItems() == null || container.getItems().size() < 4) {
                    	System.out.println(selectedItem.getChildren().size());
                        // Get the container's coordinates and size (assuming it's 50x50)
                        double containerX = container.getX();
                        double containerY = container.getY();
                        double containerWidth = container.getWidth();
                        double containerHeight = container.getHeight();

                        // Determine the new item's position based on the number of existing items
                        double itemX = containerX;
                        double itemY = containerY;

                        // Place the new item in one of the four corners based on the number of items already in the container
                        if (selectedItem.getChildren().size()== 1) {
                            // First item: top-left corner
                            itemX = containerX;
                            itemY = containerY;
                        } else if (selectedItem.getChildren().size()== 2) {
                            // Second item: top-right corner
                            itemX = containerX + containerWidth;
                            itemY = containerY;
                        } else if (selectedItem.getChildren().size() == 3) {
                            // Third item: bottom-left corner
                            itemX = containerX;
                            itemY = containerY + containerHeight;
                        } else if (selectedItem.getChildren().size() == 4) {
                            // Fourth item: bottom-right corner
                            itemX = containerX + containerWidth;
                            itemY = containerY + containerHeight;
                        }

                        // Update the item's position
                        newItem.setPosition(itemX, itemY);

                        // Add the item to the container
        

                        // Create and add visual representation for the item (Circle and Label)
                        Circle itemVisual = new Circle(itemX, itemY, 10, Color.GREEN);
                        visualizationPane.getChildren().add(itemVisual);

                        Label itemLabel = new Label(name);
                        itemLabel.setLayoutX(itemX + 15); // Position label near the item
                        itemLabel.setLayoutY(itemY - 15); // Position label above the item
                        visualizationPane.getChildren().add(itemLabel);

                        // Store the visual components in the componentVisuals map
                        componentVisuals.put(newItem, Arrays.asList(itemVisual, itemLabel));

                    } else {
                        // Alert when the container is full
                        showAlert("Container Full", "This container can only hold a maximum of 4 items.");
                    }
                } else {
                    // Alert if the selected item is not a container
                    showAlert("Invalid Selection", "You can only add items to an item container.");
                }
            });
        } else {
            // Alert when no container is selected
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
                ItemContainer newContainer = new ItemContainer(name, DEFAULT_X, DEFAULT_Y);

                if (selectedItem.getValue() instanceof ItemContainer) {
                    ((ItemContainer) selectedItem.getValue()).add(newContainer);
                    selectedItem.getChildren().add(new TreeItem<>(newContainer));

                    Rectangle containerVisual = new Rectangle(
                        DEFAULT_X, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT
                    );
                    containerVisual.setFill(Color.LIGHTBLUE);
                    containerVisual.setStroke(Color.BLUE);
                    visualizationPane.getChildren().add(containerVisual);

                    Label containerLabel = new Label(name);
                    containerLabel.setLayoutX(DEFAULT_X + DEFAULT_WIDTH / 2 - 15);
                    containerLabel.setLayoutY(DEFAULT_Y + 15);
                    visualizationPane.getChildren().add(containerLabel);
                    componentVisuals.put(newContainer, Arrays.asList(containerVisual, containerLabel));
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
            dialog.setContentText("Enter the new name:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newName -> {
                FarmComponent component = selectedItem.getValue();
                component.rename(newName);

                selectedItem.setValue(null);
                selectedItem.setValue(component);

                List<javafx.scene.Node> visuals = componentVisuals.get(component);
                if (visuals != null) {
                    for (javafx.scene.Node node : visuals) {
                        if (node instanceof Label) {
                            ((Label) node).setText(newName);
                        }
                    }
                }
            });
        } else {
            showAlert("No Item Selected", "Please select an item to rename.");
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

                    List<javafx.scene.Node> visuals = componentVisuals.get(selectedComponent);
                    if (visuals != null) {
                        for (javafx.scene.Node visual : visuals) {
                            if (visual instanceof Circle) {
                                ((Circle) visual).setCenterX(x);
                                ((Circle) visual).setCenterY(y);
                            } else if (visual instanceof Rectangle) {
                                ((Rectangle) visual).setX(x);
                                ((Rectangle) visual).setY(y);
                            } else if (visual instanceof Label) {
                                ((Label) visual).setLayoutX(x + 15); // Adjust relative position
                                ((Label) visual).setLayoutY(y + 15);
                            }
                        }
                    }
                } catch (Exception e) {
                    showAlert("Invalid Input", "Please enter valid integer coordinates in the format x,y.");
                }
            });
        } else {
            showAlert("No Item Selected", "Please select an item to change its location.");
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

                        List<javafx.scene.Node> visuals = componentVisuals.get(container);
                        if (visuals != null) {
                            for (javafx.scene.Node visual : visuals) {
                                if (visual instanceof Rectangle) {
                                    ((Rectangle) visual).setWidth(width);
                                    ((Rectangle) visual).setHeight(height);
                                } else if (visual instanceof Label) {
                                    Label label = (Label) visual;
                                    label.setLayoutX(container.getX() + width / 2 - 15);
                                    label.setLayoutY(container.getY() + 15);
                                }
                            }
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

    @FXML
    private void changePrice(ActionEvent event) {
        TreeItem<FarmComponent> selectedItem = listofFarm.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            FarmComponent selectedComponent = selectedItem.getValue();
            if (selectedComponent instanceof Item || selectedComponent instanceof ItemContainer) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Change Price");
                dialog.setHeaderText(null);
                dialog.setContentText("Enter the new price:");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(input -> {
                    try {
                        double newPrice = Double.parseDouble(input.trim());
                        if (newPrice < 0) {
                            showAlert("Invalid Input", "The price cannot be negative.");
                            return;
                        }

                        if (selectedComponent instanceof Item) {
                            ((Item) selectedComponent).setPrice(newPrice);
                        } else if (selectedComponent instanceof ItemContainer) {
                            ((ItemContainer) selectedComponent).setPrice(newPrice);
                        }

                        System.out.println("Price updated to: " + newPrice);

                    } catch (NumberFormatException e) {
                        showAlert("Invalid Input", "Please enter a valid number for the price.");
                    }
                });
            } else {
                showAlert("Invalid Selection", "The selected component cannot have its price changed.");
            }
        } else {
            showAlert("No Item Selected", "Please select an item or container to change its price.");
        }
    }

    // Method to delete a selected item from the TreeView
    @FXML
    private void deleteItem(ActionEvent event) {
        TreeItem<FarmComponent> selectedItem = listofFarm.getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.getParent() != null) {
            FarmComponent component = selectedItem.getValue();
            if (component instanceof ItemContainer) {
                deleteContainerContents((ItemContainer) component);
            }

            if (selectedItem.getParent().getValue() instanceof ItemContainer) {
                ItemContainer parentContainer = (ItemContainer) selectedItem.getParent().getValue();
                parentContainer.remove(component);
            }

            List<javafx.scene.Node> visuals = componentVisuals.remove(component);
            if (visuals != null) {
                visuals.forEach(visualizationPane.getChildren()::remove);
            }

            selectedItem.getParent().getChildren().remove(selectedItem);
        } else {
            showAlert("No Item Selected", "Please select an item to delete.");
        }
    }

    private void deleteContainerContents(ItemContainer container) {
        for (FarmComponent child : container.getChildren()) {
            if (child instanceof ItemContainer) {
                deleteContainerContents((ItemContainer) child);
            }

            List<javafx.scene.Node> visuals = componentVisuals.remove(child);
            if (visuals != null) {
                visuals.forEach(visualizationPane.getChildren()::remove);
            }
        }

        container.getChildren().clear();
    }

    @FXML
    public void selectItem(MouseEvent event) {
        TreeItem<FarmComponent> selectedItem = listofFarm.getSelectionModel().getSelectedItem();
        
        if (selectedItem != null && visitObject.isSelected() ) {
        	 FarmComponent selectedComponent = selectedItem.getValue();
            // Only if the component has position-related properties, animate the drone
        	 if (selectedComponent instanceof Item) {
                 Item selectedItemComponent = (Item) selectedComponent;  // Cast to Item
                 double targetX = selectedItemComponent.getX();
                 double targetY = selectedItemComponent.getY();

                 // Animate the drone to the target coordinates
                 animateDrone(targetX, targetY);

             } else if (selectedComponent instanceof ItemContainer) {
                 // If it's an instance of ItemContainer, animate the drone towards it
                 ItemContainer selectedContainerComponent = (ItemContainer) selectedComponent;  // Cast to ItemContainer
                 double targetX = selectedContainerComponent.getX();
                 double targetY = selectedContainerComponent.getY();

                 // Animate the drone to the target coordinates
                 animateDrone(targetX, targetY);
             }
        }
    }
    
    
    private void animateDrone(double targetX, double targetY) {
        // Assume the drone's image view is the first item in the list of visuals for the drone
        ImageView droneImageView = (ImageView) componentVisuals.get(drone).get(0);

        // Get the current position of the drone
        double startX = droneImageView.getLayoutX();
        double startY = droneImageView.getLayoutY();

        // Create a TranslateTransition to animate the drone to the target position
        TranslateTransition moveToTarget = new TranslateTransition(Duration.seconds(1.5), droneImageView);
        moveToTarget.setFromX(startX);
        moveToTarget.setFromY(startY);
        moveToTarget.setToX(targetX);
        moveToTarget.setToY(targetY);

        // Create a TranslateTransition to return the drone to its starting position
        TranslateTransition moveBackToStart = new TranslateTransition(Duration.seconds(1.5), droneImageView);
        moveBackToStart.setFromX(targetX- 20);
        moveBackToStart.setFromY(targetY);
        moveBackToStart.setToX(startX);
        moveBackToStart.setToY(startY);

        // Set the second transition to start after the first one finishes
        moveToTarget.setOnFinished(e -> {
            // Play the second transition (return to start) after the first one ends
            moveBackToStart.play();
        });

        // Play the first transition (move to target)
        moveToTarget.play();
    }

    private void updateVisualization() { 
        visualizationPane.getChildren().clear();

        renderComponent(rootContainer);
    }

    private void renderComponent(FarmComponent component) {
        if (component instanceof Item) {
            Item item = (Item) component;

            List<javafx.scene.Node> visuals = componentVisuals.get(component);
            Circle itemVisual = null;
            Label itemLabel = null;

            if (visuals != null) {
                for (javafx.scene.Node visual : visuals) {
                    if (visual instanceof Circle) {
                        itemVisual = (Circle) visual;
                    } else if (visual instanceof Label) {
                        itemLabel = (Label) visual;
                    }
                }
            } else {
                visuals = new ArrayList<>();
            }

            if (itemVisual == null) {
                itemVisual = new Circle(item.getX(), item.getY(), 10, Color.GREEN);
                visuals.add(itemVisual);
                visualizationPane.getChildren().add(itemVisual);
            } else {
                itemVisual.setCenterX(item.getX());
                itemVisual.setCenterY(item.getY());
            }

            if (itemLabel == null) {
                itemLabel = new Label(item.getName());
                itemLabel.setLayoutX(item.getX() + 15);
                itemLabel.setLayoutY(item.getY() - 15);
                visuals.add(itemLabel);
                visualizationPane.getChildren().add(itemLabel);
            } else {
                itemLabel.setLayoutX(item.getX() + 15);
                itemLabel.setLayoutY(item.getY() - 15);
                itemLabel.setText(item.getName());
            }

            componentVisuals.put(component, visuals);

        } else if (component instanceof ItemContainer) {
            ItemContainer container = (ItemContainer) component;

            List<javafx.scene.Node> visuals = componentVisuals.get(component);
            Rectangle containerVisual = null;
            Label containerLabel = null;

            if (visuals != null) {
                for (javafx.scene.Node visual : visuals) {
                    if (visual instanceof Rectangle) {
                        containerVisual = (Rectangle) visual;
                    } else if (visual instanceof Label) {
                        containerLabel = (Label) visual;
                    }
                }
            } else {
                visuals = new ArrayList<>();
            }

            if (containerVisual == null) {
                containerVisual = new Rectangle(
                    container.getX(), container.getY(),
                    container.getWidth(), container.getHeight()
                );
                containerVisual.setFill(Color.LIGHTBLUE);
                containerVisual.setStroke(Color.BLUE);
                visuals.add(containerVisual);
                visualizationPane.getChildren().add(containerVisual);
            } else {
                containerVisual.setX(container.getX());
                containerVisual.setY(container.getY());
                containerVisual.setWidth(container.getWidth());
                containerVisual.setHeight(container.getHeight());
            }

            if (containerLabel == null) {
                containerLabel = new Label(container.getName());
                containerLabel.setLayoutX(container.getX() + container.getWidth() / 2 - 15);
                containerLabel.setLayoutY(container.getY() - 20);
                visuals.add(containerLabel);
                visualizationPane.getChildren().add(containerLabel);
            } else {
                containerLabel.setLayoutX(container.getX() + container.getWidth() / 2 - 15);
                containerLabel.setLayoutY(container.getY() - 20);
                containerLabel.setText(container.getName());
            }

            componentVisuals.put(component, visuals);

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
