package mpdme.listener;

import com.sun.lwuit.events.ActionListener;
import mpdme.forms.MpdForm;

public abstract class MenuButtonListener implements ActionListener {
    private MpdForm parentForm;

    public MpdForm getParentForm() {
        return parentForm;
    }

    public void setParentForm(MpdForm parentForm) {
        this.parentForm = parentForm;
    }
}
