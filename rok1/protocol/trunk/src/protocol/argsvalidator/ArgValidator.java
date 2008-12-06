/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol.argsvalidator;

/**
 *
 * @author sur
 */
public interface ArgValidator<T> {
    public T validate(String[] args);
}
