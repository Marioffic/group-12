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
import javafx.scene.control.Label;
import java.util.List;
import java.util.ArrayList;







public class FarmController implements Initializable {
	
	private static final int DEFAULT_X = 100;
	private static final int DEFAULT_Y = 100;
	private static final int DEFAULT_WIDTH = 50;
	private static final int DEFAULT_HEIGHT = 50;
	// private Map<FarmComponent, javafx.scene.Node> componentVisuals = new HashMap<>();
	private Map<FarmComponent, List<javafx.scene.Node>> componentVisuals = new HashMap<>();




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
  
        rootContainer = new ItemContainer("Farm");

        TreeItem<FarmComponent> rootNode = new TreeItem<>(rootContainer);
        rootNode.setExpanded(true); 

        listofFarm.setRoot(rootNode);
        listofFarm.setShowRoot(true);

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
                Item newItem = new Item(name, DEFAULT_X, DEFAULT_Y);

                if (selectedItem.getValue() instanceof ItemContainer) {
                    ((ItemContainer) selectedItem.getValue()).add(newItem);
                    selectedItem.getChildren().add(new TreeItem<>(newItem));

                    Circle itemVisual = new Circle(DEFAULT_X, DEFAULT_Y, 10, Color.GREEN);
                    visualizationPane.getChildren().add(itemVisual);

                    Label itemLabel = new Label(name);
                    itemLabel.setLayoutX(DEFAULT_X + 15);
                    itemLabel.setLayoutY(DEFAULT_Y - 15);
                    visualizationPane.getChildren().add(itemLabel);

                    componentVisuals.put(newItem, List.of(itemVisual, itemLabel));
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
                    containerLabel.setLayoutY(DEFAULT_Y - 20);
                    visualizationPane.getChildren().add(containerLabel);
                    componentVisuals.put(newContainer, List.of(containerVisual, containerLabel));
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
                                ((Label) visual).setLayoutY(y - 15);
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
                                    label.setLayoutY(container.getY() - 20);
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
