package mpdme.forms;

import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.util.Resources;
import mpdme.components.MenuButton;
import java.io.IOException;

/**
 *
 * @author sur
 */
public class MainMenu extends Form {
    public static final int MENU_LENGTH = 3;

    private Resources resources;

    public MainMenu() {
        super();
        addComponents();
    }

    public MainMenu(String title) {
        super(title);
        addComponents();
    }

    private void addComponents() {
        try {
            resources = Resources.open("/resources.res");
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        int width = Display.getInstance().getDisplayWidth();
        MenuButton button = new MenuButton("Play", resources.getImage("play"), resources.getImage("play_sel"));
        this.addComponent(button);

        button = new MenuButton("Playlist", resources.getImage("playlist"), resources.getImage("playlist_sel"));
        this.addComponent(button);

        button = new MenuButton("Search Artist", resources.getImage("search"), resources.getImage("search_sel"));
        this.addComponent(button);

        button = new MenuButton("Search Album", resources.getImage("search"), resources.getImage("search_sel"));
        this.addComponent(button);

        button = new MenuButton("Search Title", resources.getImage("search"), resources.getImage("search_sel"));
        this.addComponent(button);
    }
}
