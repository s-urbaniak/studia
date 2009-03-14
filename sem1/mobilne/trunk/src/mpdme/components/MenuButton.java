/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mpdme.components;

import com.sun.lwuit.Button;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.plaf.Style;

/**
 *
 * @author sur
 */
public class MenuButton extends Button {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MenuButton(String text, Image icon, Image selectedIcon) {
        super(text, icon);
        this.setRolloverIcon(selectedIcon);
        Style s = this.getStyle();
        s.setBorder(null);
        s.setBgTransparency(0);
        s.setBgSelectionColor(0xffffff);
        this.setAlignment(Label.CENTER);
        this.setTextPosition(Label.BOTTOM);
    }

    public Image getPressedIcon() {
        Image i = getIcon();
        return i.scaled((int) (i.getWidth() * 0.8), (int) (i.getHeight() * 0.8));
    }

    public void replaceWith(String text, Image icon, Image selectedIcon, int id) {
        this.setText(text);
        this.setIcon(icon);
        this.setRolloverIcon(selectedIcon);
        this.setId(id);
    }
}
