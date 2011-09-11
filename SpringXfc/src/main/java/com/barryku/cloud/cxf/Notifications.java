
package com.barryku.cloud.cxf;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OrganizationId" type="{urn:enterprise.soap.sforce.com}ID"/>
 *         &lt;element name="ActionId" type="{urn:enterprise.soap.sforce.com}ID"/>
 *         &lt;element name="SessionId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EnterpriseUrl" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PartnerUrl" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Notification" type="{http://soap.sforce.com/2005/09/outbound}Book__cNotification" maxOccurs="100"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "organizationId",
    "actionId",
    "sessionId",
    "enterpriseUrl",
    "partnerUrl",
    "notification"
})
@XmlRootElement(name = "notifications")
public class Notifications {

    @XmlElement(name = "OrganizationId", required = true)
    protected String organizationId;
    @XmlElement(name = "ActionId", required = true)
    protected String actionId;
    @XmlElement(name = "SessionId", required = true, nillable = true)
    protected String sessionId;
    @XmlElement(name = "EnterpriseUrl", required = true)
    protected String enterpriseUrl;
    @XmlElement(name = "PartnerUrl", required = true)
    protected String partnerUrl;
    @XmlElement(name = "Notification", required = true)
    protected List<BookCNotification> notification;

    /**
     * Gets the value of the organizationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganizationId() {
        return organizationId;
    }

    /**
     * Sets the value of the organizationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganizationId(String value) {
        this.organizationId = value;
    }

    /**
     * Gets the value of the actionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionId() {
        return actionId;
    }

    /**
     * Sets the value of the actionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionId(String value) {
        this.actionId = value;
    }

    /**
     * Gets the value of the sessionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Sets the value of the sessionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionId(String value) {
        this.sessionId = value;
    }

    /**
     * Gets the value of the enterpriseUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnterpriseUrl() {
        return enterpriseUrl;
    }

    /**
     * Sets the value of the enterpriseUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnterpriseUrl(String value) {
        this.enterpriseUrl = value;
    }

    /**
     * Gets the value of the partnerUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartnerUrl() {
        return partnerUrl;
    }

    /**
     * Sets the value of the partnerUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartnerUrl(String value) {
        this.partnerUrl = value;
    }

    /**
     * Gets the value of the notification property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the notification property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNotification().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BookCNotification }
     * 
     * 
     */
    public List<BookCNotification> getNotification() {
        if (notification == null) {
            notification = new ArrayList<BookCNotification>();
        }
        return this.notification;
    }

}
