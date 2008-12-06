/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package protocol.properties;

public class BiezProtProperties {
    public enum Type { 
        SERVER {
            @Override
            public String toString() { 
                return "Server"; 
            }
        }, 
        CLIENT {
            @Override
            public String toString() { 
                return "Client"; 
            }
        }
    };

    public Type type;

    public Integer port;

    public String serverHostname;

    public String getServerHostname() {
        return serverHostname;
    }

    public void setServerHostname(String serverHostname) {
        this.serverHostname = serverHostname;
    }
    
    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
