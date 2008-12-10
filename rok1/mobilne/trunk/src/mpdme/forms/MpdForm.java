package mpdme.forms;

import com.sun.lwuit.Command;
import com.sun.lwuit.Form;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.util.Resources;
import java.io.IOException;
import mpdme.MpdException;

public abstract class MpdForm extends Form implements ActionListener {

    private static final int BACK_COMMAND = 50;
    protected MpdForm parentForm;
    private Resources resources;

    protected Resources getResources() {
        if (this.resources == null) {
            try {
                resources = Resources.open("/resources.res");
            } catch (IOException ex) {
                throw new MpdException(ex);
            }
        }

        return this.resources;
    }

    public MpdForm getParentForm() {
        return parentForm;
    }

    public void setParentForm(MpdForm parent) {
        this.parentForm = parent;
    }

    public MpdForm(MpdForm parentForm) {
        super();
        this.parentForm = parentForm;
        addBackCommand();
        initialize();
    }

    public void actionPerformed(ActionEvent evt) {
        Command cmd = evt.getCommand();
        switch (cmd.getId()) {
            case BACK_COMMAND:
                this.parentForm.show();
        }
    }

    private void addBackCommand() {
        if (this.parentForm == null) {
            return;
        }
        this.setCommandListener(this);
        Command backCommand = new Command("Back", BACK_COMMAND);
        this.setBackCommand(backCommand);
        this.addCommand(backCommand);
    }

    public abstract void initialize();
}
