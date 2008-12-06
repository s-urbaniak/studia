package mpdme.forms;

import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.layouts.GridLayout;
import com.sun.lwuit.util.Resources;
import mpdme.components.MenuButton;
import java.io.IOException;

/**
 *
 * @author sur
 */
public class MainMenu extends Form {
    private Resources resources;
    private int elementWidth = 0;

    public MainMenu() {
        super();
        addComponents();
    }

    public MainMenu(String title) {
        super(title);
        addComponents();
    }

    private void addMenuButton(String title, Image unselected, Image selected) {
        MenuButton button = new MenuButton(title, unselected, selected);
        this.addComponent(button);
        elementWidth = Math.max(button.getPreferredW(), elementWidth);
    }

    private void addComponents() {
        try {
            resources = Resources.open("/resources.res");
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        int width = Display.getInstance().getDisplayWidth();

        addMenuButton("Play", resources.getImage("play"), resources.getImage("play_sel"));
        addMenuButton("Playlist", resources.getImage("playlist"), resources.getImage("playlist_sel"));
        addMenuButton("Search Artist", resources.getImage("search"), resources.getImage("search_sel"));
        addMenuButton("Search Album", resources.getImage("search"), resources.getImage("search_sel"));
        addMenuButton("Search Title", resources.getImage("search"), resources.getImage("search_sel"));

        int cols = width / this.elementWidth;
        int rows = 5 / cols;
        this.setLayout(new GridLayout(rows, cols));
    }
}
