
package com.barryku.cloud.cxf;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.barryku.cloud.cxf package. 
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

    private final static QName _BookCTypeC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Type__c");
    private final static QName _BookCName_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Name");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.barryku.cloud.cxf
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link NotificationsResponse }
     * 
     */
    public NotificationsResponse createNotificationsResponse() {
        return new NotificationsResponse();
    }

    /**
     * Create an instance of {@link Notifications }
     * 
     */
    public Notifications createNotifications() {
        return new Notifications();
    }

    /**
     * Create an instance of {@link BookCNotification }
     * 
     */
    public BookCNotification createBookCNotification() {
        return new BookCNotification();
    }

    /**
     * Create an instance of {@link SObject }
     * 
     */
    public SObject createSObject() {
        return new SObject();
    }

    /**
     * Create an instance of {@link BookC }
     * 
     */
    public BookC createBookC() {
        return new BookC();
    }

    /**
     * Create an instance of {@link AggregateResult }
     * 
     */
    public AggregateResult createAggregateResult() {
        return new AggregateResult();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Type__c", scope = BookC.class)
    public JAXBElement<String> createBookCTypeC(String value) {
        return new JAXBElement<String>(_BookCTypeC_QNAME, String.class, BookC.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = BookC.class)
    public JAXBElement<String> createBookCName(String value) {
        return new JAXBElement<String>(_BookCName_QNAME, String.class, BookC.class, value);
    }

}
