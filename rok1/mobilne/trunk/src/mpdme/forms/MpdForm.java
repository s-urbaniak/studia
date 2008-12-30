package mpdme.forms;

import com.sun.lwuit.Command;
import com.sun.lwuit.Form;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.util.Resources;
import java.io.IOException;
import mpdme.Midlet;
import mpdme.MpdException;

public abstract class MpdForm extends Form implements ActionListener {

    protected static final int BACK_COMMAND = 50;
    protected static final int EXIT_COMMAND = 51;

    protected MpdForm parentForm;
    private Resources resources;
    private static Midlet midlet = null;

    public static synchronized void setMidlet(Midlet midlet) {
        MpdForm.midlet = midlet;
    }

    protected static Midlet getMidlet() {
        return MpdForm.midlet;
    }

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
        addCommands();
        initialize();
    }

    public void actionPerformed(ActionEvent evt) {
        Command cmd = evt.getCommand();

        if (cmd == null)
            return;

        switch (cmd.getId()) {
            case BACK_COMMAND:
                this.quit();
                this.parentForm.show();
                break;
            case EXIT_COMMAND:
                this.quit();
                MpdForm.getMidlet().destroyApp(false);
                MpdForm.getMidlet().notifyDestroyed();
                break;
        }
    }

    private void addCommands() {
        Command exitCommand = new Command("Exit", EXIT_COMMAND);
        this.addCommand(exitCommand);
        this.setCommandListener(this);

        if (this.parentForm == null) {
            return;
        }

        this.setCommandListener(this);
        Command backCommand = new Command("Back", BACK_COMMAND);
        this.setBackCommand(backCommand);
        this.addCommand(backCommand);
    }

    public abstract void initialize();

    public abstract void quit();
}
