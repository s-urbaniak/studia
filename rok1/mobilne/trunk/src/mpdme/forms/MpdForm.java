package mpdme.forms;

import com.sun.lwuit.Command;
import com.sun.lwuit.Form;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;

public abstract class MpdForm extends Form implements ActionListener {
    private static final int BACK_COMMAND = 50;

    protected MpdForm parentForm;

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
        if (this.parentForm == null)
            return;

        this.setCommandListener(this);
        Command backCommand = new Command("Back", BACK_COMMAND);
        this.setBackCommand(backCommand);
        this.addCommand(backCommand);
    }
    
    public abstract void initialize();
}
