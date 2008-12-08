/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mpdme.forms;

import com.sun.lwuit.Container;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.GridLayout;
import com.sun.lwuit.util.Resources;
import java.io.IOException;
import mpdme.components.MenuButton;

public class PlayerForm extends MpdForm {
    public PlayerForm(MpdForm parent) {
        super(parent);
    }
    
    public void initialize() {
        Resources resources;

        try {
            resources = Resources.open("/resources.res");
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        this.setTitle("mpd Player Control");
        this.setLayout(new BorderLayout());

        Container buttonContainer = new Container(new GridLayout(1, 4));
        MenuButton button;

        button = new MenuButton("Prev", resources.getImage("prev"), resources.getImage("prev_sel"));
        buttonContainer.addComponent(button);

        button = new MenuButton("Play", resources.getImage("play_btn"), resources.getImage("play_btn_sel"));
        buttonContainer.addComponent(button);

        button = new MenuButton("Stop", resources.getImage("stop"), resources.getImage("stop_sel"));
        buttonContainer.addComponent(button);

        button = new MenuButton("Next", resources.getImage("next"), resources.getImage("next_sel"));
        buttonContainer.addComponent(button);

        this.addComponent(BorderLayout.SOUTH, buttonContainer);
    }
}
