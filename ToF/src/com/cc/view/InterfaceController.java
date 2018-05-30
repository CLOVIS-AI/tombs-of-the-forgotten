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

import com.cc.players.Player;
import com.cc.tof.ToF;
import com.cc.utils.Bar;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
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

        viewItem(ViewWeapon);
        viewItem(ViewApparel);
        viewItem(ViewEatable);
        viewItem(ViewScroll);
        viewItem(ViewOther);
        viewItem(ViewAll);

        restPopup(ButtonRest);
        lootPopup(ButtonOpen);

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
        
        // Faire en sorte d'ouvrir la page loot lorsque l'on gagne un combat ou
        // lorsque l'on fouille un coffre.
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
     * Load a new fxml file.
     *
     * @param event the event
     */
    public void viewItem(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ToF.getResource("Item.fxml"));
            Parent menu = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(menu));
            stage.show();
        } catch (IOException ex) {
            throw new IllegalStateException("Couldn't load the item view.", ex);
        }
    }

    /**
     * Press a specific button resulting in opening a new fxml file.
     *
     * @param i The button
     * @see #viewItem(javafx.event.ActionEvent) load a new fxml file
     */
    public void viewItem(MenuItem i) {
        i.setOnAction(e -> {
            viewItem(e);
        });
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

    public void lootPopup(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ToF.getResource("Loot.fxml"));
            Parent menu = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(menu));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void lootPopup(Button b) {
        b.setOnAction(e -> lootPopup(e));
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
        button.setDisable(!p.canMoveTo(d));
        button.setOnMouseEntered(e -> button.setFill(SELECTED));
        button.setOnMouseExited(e -> button.setFill(getColor(p.canMoveTo(d))));
        button.setOnMouseReleased(e -> {
            p.moveTo(d);
            update(p);
        });
        button.setFill(getColor(p.canMoveTo(d)));
    }

    public void move(Player p, Direction d, Button b) {
        b.setVisible(true);
        if(!p.canMoveTo(d)){
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
        move(p, NORTH, (Shape) MoveNorth);
        move(p, SOUTH, (Shape) MoveSouth);
        move(p, EAST, (Shape) MoveEast);
        move(p, WEST, (Shape) MoveWest);
        move(p, UP, ButtonUpstairs);
        move(p, DOWN, ButtonDownstairs);
        updateMap();
    }

    public void fillBar(Bar b, ProgressBar bar) {

        bar.setProgress(b.getCurrent() * 1.0 / b.getMaximum());
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
                .filter(Room::isExplored)
                .forEach(this::drawRoom);
    }

    public void drawRoom(Room r) {
        // Position
        Location relative = r.getLocation().remove(player);
        Ellipse e = new Ellipse(
                relative.getY() * 30 + Map.getPrefWidth() / 2,
                relative.getX() * 30 + Map.getPrefHeight() / 2,
                10, 10);

        // Color
        if (r.getLocation().equals(player)) {
            e.setFill(Color.AQUAMARINE);
        }

        Map.getChildren().add(e);
    }
}
