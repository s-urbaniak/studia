/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package protocol.clientserver;

import protocol.properties.BiezProtProperties;
import protocol.properties.BiezProtProperties.Type;

/**
 *
 * @author sur
 */
public abstract class ClientServerFactory {
    public final static ClientServer createClientServer(BiezProtProperties props) {
        ClientServer instance = null;

        if ((props == null) || (props.getType() == null))
            throw new UnsupportedOperationException("BiezProtProperties must not be null.");

        if (Type.CLIENT.equals(props.getType()))
            instance = new Client(props);
        
        if (Type.SERVER.equals(props.getType()))
            instance = new Server(props);
        
        return instance;
    }
}
