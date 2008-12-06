package mdpme;

import com.sun.lwuit.plaf.UIManager;
import com.sun.lwuit.util.Resources;
import java.io.IOException;

/**
 *
 * @author sur
 */
public class ThemeManager {
    private Resources r = null;

    public void loadTheme() throws IOException {
        r = Resources.open("/businessTheme.res");
        UIManager.getInstance().setThemeProps(r.getTheme("businessTheme"));
    }
}
