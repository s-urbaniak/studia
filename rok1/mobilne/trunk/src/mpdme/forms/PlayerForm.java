/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mpdme.forms;

import com.sun.lwuit.layouts.BorderLayout;

public class PlayerForm extends MpdForm {
    public PlayerForm(MpdForm parent) {
        super(parent);
    }
    
    public void initialize() {
        this.setTitle("mpd Player Control");
        this.setLayout(new BorderLayout());
    }
}
