/*
 * The MIT License
 *
 * Copyright 2018 Canet Ivan & Chourouq Sarah.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.cc.view;

import com.cc.items.Item;
import com.cc.items.ItemContainer;
import com.cc.players.Player;
import com.cc.tof.ToF;
import static com.cc.tof.ToF.println;
import com.cc.utils.Bar;
import com.cc.utils.messages.Message;
import com.cc.world.Direction;
import static com.cc.world.Direction.DOWN;
import static com.cc.world.Direction.EAST;
import static com.cc.world.Direction.NORTH;
import static com.cc.world.Direction.SOUTH;
import static com.cc.world.Direction.UP;
import static com.cc.world.Direction.WEST;
import com.cc.world.Location;
import com.cc.world.Room;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author schourouq
 */
public class InterfaceController implements Initializable {

    @FXML
    private Node LeftMenu, Open,
            BlockStats, BlockWeapon, BlockApparel, BlockEatable, BlockScrolls, BlockOther, BlockAll,
            MoveNorth, MoveSouth, MoveEast, MoveWest;

    @FXML
    private Label Apparel, Weapons, Stats, Eatable, Scrolls, Other, All;

    @FXML
    private MenuItem ViewWeapon, ViewApparel, ViewEatable, ViewScroll, ViewOther,
            ViewAll;

    @FXML
    private Button ButtonRest, ButtonSave, ButtonOpen, ButtonClose, ButtonReadNote,
            ButtonSearchRoom, ButtonGrabItem, ButtonDropItem,
            ButtonUpstairs, ButtonDownstairs;

    @FXML
    private AnchorPane Map;

    @FXML
    private ProgressBar BarHP, BarMana, BarStamina, BarPods;

    private boolean isMenu = false;
    private Timeline move = new Timeline();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Open.setOnMouseEntered(e -> {
            isMenu = true;
            slideRight(LeftMenu);
            slideLeft(BlockStats);
            slideLeft(BlockWeapon);
            slideLeft(BlockApparel);
            slideLeft(BlockEatable);
            slideLeft(BlockScrolls);
            slideLeft(BlockOther);
            slideLeft(BlockAll);
        });
        LeftMenu.setOnMouseExited(e -> {
            isMenu = false;
            slideLeft(LeftMenu);
        });

        showMenu(BlockStats, Stats);
        showMenu(BlockWeapon, Weapons);
        showMenu(BlockApparel, Apparel);
        showMenu(BlockEatable, Eatable);
        showMenu(BlockScrolls, Scrolls);
        showMenu(BlockOther, Other);
        showMenu(BlockAll, All);

        restPopup(ButtonRest);
        ButtonOpen.setOnAction(e -> lootPopup(ToF.getWorld().getPlayer().getCurrentRoom().getItems()));

        update(ToF.getWorld().getPlayer());

        /**
         * ******************************SAVE*********************************
         */
        ButtonSave.setOnAction(e -> ToF.save());

        updateBars();

        // Buttons
        ButtonReadNote.setOnAction(e -> ToF.getWorld().getPlayer().getCurrentRoom().readNotes());

        // Map
        Map.setClip(new Ellipse(
                Map.getPrefWidth() / 2,
                Map.getPrefHeight() / 2,
                Map.getPrefWidth() / 2,
                Map.getPrefHeight() / 2));
        updateMap();
        
        ButtonSearchRoom.setOnAction(e -> onSearch());
        
        // Faire en sorte d'ouvrir la page loot lorsque l'on gagne un combat ou
        // lorsque l'on fouille un coffre.
    }
    
    private void onSearch() {
        ItemContainer items = ToF.getWorld().getPlayer().getCurrentRoom().getItems();
        ToF.getWorld().newMessage(new Message()
                .add("There are " + items.getItems().size() + " items:")
                .addAuto(items.getItems().toArray()));
        
        ToF.getWorld().nextTick();
    }

    /**
     * Displays a listed menu among the main menu.
     *
     * @param block A menu
     * @param label A button
     */
    private void showMenu(Node block, Label label) {
        label.setOnMousePressed(e -> {
            isMenu = true;
            slideLeft(LeftMenu);
            slideRight(block);
        });
    }

    /**
     * Animation for displaying a menu on the game screen.
     *
     * @param menu A menu
     */
    private void slideRight(Node menu) {
        move = new Timeline(new KeyFrame(Duration.seconds(0.5), new KeyValue(menu.layoutXProperty(), 255)));
        move.play();
        if (isMenu) {
            Open.setMouseTransparent(true);
            Open.setOpacity(0);
        }
    }

    /**
     * Animation for hiding a menu out of the game screen.
     *
     * @param menu A menu
     */
    private void slideLeft(Node menu) {
        move = new Timeline(new KeyFrame(Duration.seconds(0.5), new KeyValue(menu.layoutXProperty(), -255)));
        move.play();
        if (!isMenu) {
            Open.setMouseTransparent(false);
            Open.setOpacity(1);
        }
    }

    /**
     * Load a new JavaFx window.
     *
     * @param event the event
     */
    public void restPopup(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ToF.getResource("rest.fxml"));
            Parent menu = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(menu));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Press a specific button resulting in opening a new JavaFX window.
     *
     * @param b the button
     * @see #restPopup(javafx.event.ActionEvent) Load a new JavaFX window
     */
    public void restPopup(Button b) {
        b.setOnAction(e -> restPopup(e));
    }

    public void lootPopup(ItemContainer other) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ToF.getResource("Loot.fxml"));
            Parent menu = (Parent) fxmlLoader.load();
            ((LootController)fxmlLoader.getController()).setInventories(ToF.getWorld().getPlayer().getInventory(), other);
            Stage stage = new Stage();
            stage.setScene(new Scene(menu));
            stage.show();
            update(ToF.getWorld().getPlayer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final Color CANNOT_MOVE = Color.SLATEGRAY;
    private static final Color CAN_MOVE = Color.BLACK;
    private static final Color SELECTED = Color.SNOW;

    /**
     * Set a specific color on a button depending whether the player can go to a
     * certain direction or not.
     *
     * @param p the player
     * @param d the direction
     * @param button the direction button
     */
    public void move(Player p, Direction d, Shape button) {
        button.setDisable(!p.canReach(d));
        button.setOnMouseEntered(e -> button.setFill(SELECTED));
        button.setOnMouseExited(e -> button.setFill(getColor(p.canReach(d))));
        button.setOnMouseReleased(e -> {
            p.moveTo(d);
            update(p);
        });
        button.setFill(getColor(p.canReach(d)));
    }

    public void move(Player p, Direction d, Button b) {
        b.setVisible(true);
        if(!p.canReach(d)){
            b.setVisible(false);
        }
        b.setOnMouseReleased(e ->{
            p.moveTo(d);
            update(p);
        });
    }
    
    /**
     * The color of a direction button
     *
     * @param canMove True if the player can move to a certain direction
     * @return True if the player can move, false else
     */
    private Color getColor(boolean canMove) {
        return canMove ? CAN_MOVE : CANNOT_MOVE;
    }

    /**
     * Reinitialize every direction button's color each time the player moves.
     *
     * @param p the player
     */
    public void update(Player p) {
        println("GUI", "Updating the UI...");
        
        move(p, NORTH, (Shape) MoveNorth);
        move(p, SOUTH, (Shape) MoveSouth);
        move(p, EAST, (Shape) MoveEast);
        move(p, WEST, (Shape) MoveWest);
        move(p, UP, ButtonUpstairs);
        move(p, DOWN, ButtonDownstairs);
        updateMap();
        updateBars();
        
        if(ToF.getWorld().isFullyExplored())
            ToF.getWorld().newMessage(new Message().add("You have explored everything!"));

        ToF.getWorld().nextTick();
    }

    public void fillBar(Bar b, ProgressBar bar) {

        bar.setProgress(b.getCurrent() * 1.0 / b.getMaximum());
        bar.setTooltip(new Tooltip(b.getName() + ": " + b.getCurrent() + "/" + b.getMaximum()));
    }

    public void updateBars() {
        fillBar(ToF.getWorld().getPlayer().getHealthBar(), BarHP);
        fillBar(ToF.getWorld().getPlayer().getManaBar(), BarMana);
        fillBar(ToF.getWorld().getPlayer().getStaminaBar(), BarStamina);
        fillBar(ToF.getWorld().getPlayer().getWeightBar(), BarPods);
    }

    Location player;

    public void updateMap() {
        ObservableList<Node> nodes = Map.getChildren();
        nodes.clear();
        player = ToF.getWorld().getPlayer().getLocation();
        ToF.getWorld()
                .selectRoomsByLocation(l -> l.getZ() == player.getZ())
                .forEach(this::drawRoom);
    }
    
    private static final int MAP_ROOM_SIZE = 5;
    private static final int MAP_ROOM_DIST = 15;
    private static final double TRANSPARENCY_COEF = 0.5;

    public void drawRoom(Room r) {
        // Position
        Location relative = r.getLocation().remove(player);
        Ellipse e = new Ellipse(
                relative.getY() * MAP_ROOM_DIST + Map.getPrefWidth() / 2,
                relative.getX() * MAP_ROOM_DIST + Map.getPrefHeight() / 2,
                MAP_ROOM_SIZE, MAP_ROOM_SIZE);

        // Color
        if (r.getLocation().equals(player)) {
            e.setFill(Color.AQUAMARINE);
        } else if (ToF.getWorld().getEntities(false).anyMatch(en -> en.getLocation().equals(r.getLocation()))){
            e.setFill(Color.CRIMSON);
        }
        
        if (!r.isExplored()){
            Color p = (Color) e.getFill();
            e.setFill(new Color(p.getRed(), p.getGreen(), p.getRed(),
                    TRANSPARENCY_COEF/(r.getLocation().dist(player)+TRANSPARENCY_COEF)
            ));
        }

        Map.getChildren().add(e);
    }
    
    public static void contextMenuItem(Item item, Node node, MouseEvent mouse){
        println("GUI", "Context menu for the item " + item);
        
        // View the item
        MenuItem view = new MenuItem("View");
        view.setOnAction(e -> viewItem(item));
        
        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(view);
        menu.show(node, mouse.getScreenX(), mouse.getSceneY());
    }
    
    /**
     * Load a new fxml file.
     *
     * @param item the item
     */
    private static void viewItem(Item item) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ToF.getResource("Item.fxml"));
            Parent menu = (Parent) fxmlLoader.load();
            ((ItemController)fxmlLoader.getController()).setItem(item);
            Stage stage = new Stage();
            stage.setScene(new Scene(menu));
            stage.show();
        } catch (IOException ex) {
            throw new IllegalStateException("Couldn't load the item view.", ex);
        }
    }
}
