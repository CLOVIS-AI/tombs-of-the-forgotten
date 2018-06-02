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

import com.cc.tof.ToF;
import static com.cc.tof.ToF.println;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 *
 * @author a.chourouq
 */
public class IntroductionController implements Initializable {

    @FXML
    private Label IntroText;

    @FXML
    private Button next, Play;

    private ArrayList<String> introduction = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        introduction.add("'Ouch...'");
        introduction.add("'Wh... Where am I?'");
        introduction.add("You are slowly opening your eyes as you feel an unbearable headache.");
        introduction.add("You look around you and see nothing but a dark and vast place");
        introduction.add("You are wondering how you got here, and where you were before that.");
        introduction.add("You literaly have no clue of what happened here and not even who you are.");
        introduction.add("You stand up, stumble and almost fall. Your body hurt and you feel weak.");
        introduction.add("'What is it?...");
        introduction.add("There's a demolished door with a sign that says \"don't open\". What happened in this place?");
        introduction.add("You turn your head to your right.");
        introduction.add("An enormous metal door blocks your path. Ash and soot is all over it,\n "
                + "somehow untouched by time and the elements.");
        introduction.add("You step closer to inspect it and.. wait..");
        introduction.add("You're sure you saw a shadow under the cracks of the door.");
        introduction.add("Yes, you are not alone here...");

        Play.setVisible(false);

        next.setOnAction(e -> {
            displayText();
        });

    }

    int cpt = 0;

    public void displayText() {
        if (cpt == introduction.size()) {
            Play.setVisible(true);
            Play.setOnAction(e -> {
                launchInterfaceGame();
            });
            next.setVisible(false);
        } else {
            IntroText.setText(introduction.get(cpt));
            cpt++;
        }
    }

    public void launchInterfaceGame() {
        try {
            println("ToF", "Loading the game interface...");
            Parent ui = FXMLLoader.load(ToF.getResource("interface.fxml"));
            ui.relocate(-255, 0);

            println("ToF", "Creating the scene...");
            Scene sc = new Scene(ui, 1000, 600);

            println("ToF", "Swapping the scene...");
            ToF.getStage().setScene(sc);
        } catch (IOException ex) {
            throw new IllegalStateException("Could not load main UI", ex);
        }
    }

}
