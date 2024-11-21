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





public class FarmController implements Initializable {
	
	private static final int DEFAULT_X = 100;
	private static final int DEFAULT_Y = 100;
	private static final int DEFAULT_WIDTH = 50; // If items have dimensions
	private static final int DEFAULT_HEIGHT = 50;


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
        
     // Set up the root container
        ItemContainer rootContainer = new ItemContainer("Farm");
        TreeItem<FarmComponent> farm = new TreeItem<>(rootContainer);

        // Create Barn and add its sub-components
        ItemContainer barnContainer = new ItemContainer("Barn");
        TreeItem<FarmComponent> barn = new TreeItem<>(barnContainer);

        ItemContainer liveStockContainer = new ItemContainer("LiveStock");
        TreeItem<FarmComponent> liveStock = new TreeItem<>(liveStockContainer);
        Item animal = new Item("Cow");
        TreeItem<FarmComponent> animalItem = new TreeItem<>(animal);
        liveStockContainer.add(animal);
        liveStock.getChildren().add(animalItem);

        ItemContainer toolsContainer = new ItemContainer("Tools");
        TreeItem<FarmComponent> tools = new TreeItem<>(toolsContainer);
        Item instrument = new Item("Axe");
        TreeItem<FarmComponent> instrumentItem = new TreeItem<>(instrument);
        toolsContainer.add(instrument);
        tools.getChildren().add(instrumentItem);

        ItemContainer harvestContainer = new ItemContainer("Harvested Crops");
        TreeItem<FarmComponent> harvest = new TreeItem<>(harvestContainer);
        Item harvested = new Item("Tomatoes");
        TreeItem<FarmComponent> harvestedItem = new TreeItem<>(harvested);
        harvestContainer.add(harvested);
        harvest.getChildren().add(harvestedItem);

        barnContainer.add(liveStockContainer);
        barnContainer.add(toolsContainer);
        barnContainer.add(harvestContainer);
        barn.getChildren().addAll(liveStock, tools, harvest);

        // Create Crops
        ItemContainer cropsContainer = new ItemContainer("Crops");
        TreeItem<FarmComponent> crops = new TreeItem<>(cropsContainer);
        Item planted = new Item("Potatoes");
        TreeItem<FarmComponent> plantedItem = new TreeItem<>(planted);
        cropsContainer.add(planted);
        crops.getChildren().add(plantedItem);

        // Create Equipment
        ItemContainer equipmentContainer = new ItemContainer("Equipment");
        TreeItem<FarmComponent> equipment = new TreeItem<>(equipmentContainer);
        Item tractor = new Item("Tractor");
        TreeItem<FarmComponent> tractorItem = new TreeItem<>(tractor);
        equipmentContainer.add(tractor);
        equipment.getChildren().add(tractorItem);

        // Create Control Center (Drone)
        Item controlCenter = new Item("Drone");
        TreeItem<FarmComponent> controlCenterItem = new TreeItem<>(controlCenter);

        // Add main components to the farm root
        rootContainer.add(barnContainer);
        rootContainer.add(cropsContainer);
        rootContainer.add(equipmentContainer);
        rootContainer.add(controlCenter);
        farm.getChildren().addAll(barn, crops, equipment, controlCenterItem);

        // Set the root of the TreeView
        listofFarm.setRoot(farm);

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

                    // Add visual representation in the visualizationPane
                    Circle itemVisual = new Circle(DEFAULT_X, DEFAULT_Y, 10, Color.GREEN);
                    visualizationPane.getChildren().add(itemVisual);
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

                    // Add visual representation in the visualizationPane
                    Rectangle containerVisual = new Rectangle(DEFAULT_X, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
                    containerVisual.setFill(Color.LIGHTBLUE);
                    containerVisual.setStroke(Color.BLUE);
                    visualizationPane.getChildren().add(containerVisual);
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
            System.out.println("Changing location for: " + selectedItem.getValue().getName());
        } else {
            showAlert("No item selected", "Please select an item to change its location.");
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
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Change Coordinates");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter new coordinates (x,y):");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(coordinates -> {
                String[] xy = coordinates.split(",");
                if (xy.length == 2) {
                    try {
                        int x = Integer.parseInt(xy[0].trim());
                        int y = Integer.parseInt(xy[1].trim());
                        if (selectedItem.getValue() instanceof Item) {
                            ((Item) selectedItem.getValue()).setCoordinates(x, y);
                        } else if (selectedItem.getValue() instanceof ItemContainer) {
                            ((ItemContainer) selectedItem.getValue()).setCoordinates(x, y);
                        }
                        updateVisualization();
                    } catch (NumberFormatException e) {
                        showAlert("Invalid Input", "Coordinates must be integers.");
                    }
                } else {
                    showAlert("Invalid Input", "Please enter coordinates in the format x,y.");
                }
            });
        } else {
            showAlert("No item selected", "Please select an item to change its coordinates.");
        }
    }


    // Method to delete a selected item from the TreeView
    @FXML
    private void deleteItem(ActionEvent event) {
        TreeItem<FarmComponent> selectedItem = listofFarm.getSelectionModel().getSelectedItem();
        if (selectedItem != null && selectedItem.getParent() != null) {
            ((ItemContainer) selectedItem.getParent().getValue()).remove(selectedItem.getValue());
            selectedItem.getParent().getChildren().remove(selectedItem);
        } else {
            showAlert("No item selected", "Please select an item to delete.");
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
