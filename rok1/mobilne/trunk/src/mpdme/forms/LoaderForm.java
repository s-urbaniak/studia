/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mpdme.forms;

import com.sun.lwuit.Button;
import com.sun.lwuit.StaticAnimation;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.util.Resources;
import java.io.IOException;

public class LoaderForm extends MpdForm {
    public LoaderForm() {
        super(null);
    }

    public void initialize() {
        Resources resources;

        try {
            resources = Resources.open("/resources.res");
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        this.setLayout(new BorderLayout());
        this.setTitle("Loader test");
        Button loader = new Button(resources.getAnimation("loader"));
        this.addComponent(BorderLayout.CENTER, loader);
    }
}
