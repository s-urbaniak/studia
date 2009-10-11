
package org.urbaniak.studia.sem2.integracja.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.urbaniak.studia.sem2.integracja.service package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Remove_QNAME = new QName("http://service.integracja.sem2.studia.urbaniak.org/", "remove");
    private final static QName _UpdateResponse_QNAME = new QName("http://service.integracja.sem2.studia.urbaniak.org/", "updateResponse");
    private final static QName _FetchResponse_QNAME = new QName("http://service.integracja.sem2.studia.urbaniak.org/", "fetchResponse");
    private final static QName _RemoveResponse_QNAME = new QName("http://service.integracja.sem2.studia.urbaniak.org/", "removeResponse");
    private final static QName _Update_QNAME = new QName("http://service.integracja.sem2.studia.urbaniak.org/", "update");
    private final static QName _Fetch_QNAME = new QName("http://service.integracja.sem2.studia.urbaniak.org/", "fetch");
    private final static QName _AddResponse_QNAME = new QName("http://service.integracja.sem2.studia.urbaniak.org/", "addResponse");
    private final static QName _Add_QNAME = new QName("http://service.integracja.sem2.studia.urbaniak.org/", "add");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.urbaniak.studia.sem2.integracja.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UpdateResponse }
     * 
     */
    public UpdateResponse createUpdateResponse() {
        return new UpdateResponse();
    }

    /**
     * Create an instance of {@link FetchResponse }
     * 
     */
    public FetchResponse createFetchResponse() {
        return new FetchResponse();
    }

    /**
     * Create an instance of {@link RemoveResponse }
     * 
     */
    public RemoveResponse createRemoveResponse() {
        return new RemoveResponse();
    }

    /**
     * Create an instance of {@link Artist }
     * 
     */
    public Artist createArtist() {
        return new Artist();
    }

    /**
     * Create an instance of {@link Remove }
     * 
     */
    public Remove createRemove() {
        return new Remove();
    }

    /**
     * Create an instance of {@link Update }
     * 
     */
    public Update createUpdate() {
        return new Update();
    }

    /**
     * Create an instance of {@link Add }
     * 
     */
    public Add createAdd() {
        return new Add();
    }

    /**
     * Create an instance of {@link Fetch }
     * 
     */
    public Fetch createFetch() {
        return new Fetch();
    }

    /**
     * Create an instance of {@link AddResponse }
     * 
     */
    public AddResponse createAddResponse() {
        return new AddResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Remove }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.integracja.sem2.studia.urbaniak.org/", name = "remove")
    public JAXBElement<Remove> createRemove(Remove value) {
        return new JAXBElement<Remove>(_Remove_QNAME, Remove.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.integracja.sem2.studia.urbaniak.org/", name = "updateResponse")
    public JAXBElement<UpdateResponse> createUpdateResponse(UpdateResponse value) {
        return new JAXBElement<UpdateResponse>(_UpdateResponse_QNAME, UpdateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FetchResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.integracja.sem2.studia.urbaniak.org/", name = "fetchResponse")
    public JAXBElement<FetchResponse> createFetchResponse(FetchResponse value) {
        return new JAXBElement<FetchResponse>(_FetchResponse_QNAME, FetchResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.integracja.sem2.studia.urbaniak.org/", name = "removeResponse")
    public JAXBElement<RemoveResponse> createRemoveResponse(RemoveResponse value) {
        return new JAXBElement<RemoveResponse>(_RemoveResponse_QNAME, RemoveResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Update }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.integracja.sem2.studia.urbaniak.org/", name = "update")
    public JAXBElement<Update> createUpdate(Update value) {
        return new JAXBElement<Update>(_Update_QNAME, Update.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Fetch }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.integracja.sem2.studia.urbaniak.org/", name = "fetch")
    public JAXBElement<Fetch> createFetch(Fetch value) {
        return new JAXBElement<Fetch>(_Fetch_QNAME, Fetch.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.integracja.sem2.studia.urbaniak.org/", name = "addResponse")
    public JAXBElement<AddResponse> createAddResponse(AddResponse value) {
        return new JAXBElement<AddResponse>(_AddResponse_QNAME, AddResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Add }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.integracja.sem2.studia.urbaniak.org/", name = "add")
    public JAXBElement<Add> createAdd(Add value) {
        return new JAXBElement<Add>(_Add_QNAME, Add.class, null, value);
    }

}
