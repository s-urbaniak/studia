package mpdme.forms;

import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.GridLayout;
import com.sun.lwuit.util.Resources;
import mpdme.components.MenuButton;
import mpdme.listener.MenuButtonListener;

/**
 *
 * @author sur
 */
public class MainMenu extends MpdForm {
    private int elementWidth = 0;

    public void quit() {
    }

    private class ShowPlayerListener extends MenuButtonListener
    {
        public void actionPerformed(ActionEvent evt) {
            Form playerForm = new PlayerForm(this.getParentForm());
            playerForm.show();
        }
    }
    
    public MainMenu() {
        super(null);
    }

    private MenuButton addMenuButton(String title, Image unselected, Image selected) {
        MenuButton button = new MenuButton(title, unselected, selected);
        this.addComponent(button);
        elementWidth = Math.max(button.getPreferredW(), elementWidth);
        return button;
    }

    public void initialize() {
        Resources resources = this.getResources();

        int width = Display.getInstance().getDisplayWidth();

        MenuButton button;
        MenuButtonListener listener;

        button = addMenuButton("Play", resources.getImage("play"), resources.getImage("play_sel"));
        listener = new ShowPlayerListener();
        listener.setParentForm(this);
        button.addActionListener(listener);

        addMenuButton("Playlist", resources.getImage("playlist"), resources.getImage("playlist_sel"));
        addMenuButton("Search Artist", resources.getImage("search"), resources.getImage("search_sel"));
        addMenuButton("Search Album", resources.getImage("search"), resources.getImage("search_sel"));
        addMenuButton("Search Title", resources.getImage("search"), resources.getImage("search_sel"));

        int cols = width / this.elementWidth;
        int rows = 5 / cols;
        this.setTitle("mpd Mobile Edition");
        this.setLayout(new GridLayout(rows, cols));
    }
}
