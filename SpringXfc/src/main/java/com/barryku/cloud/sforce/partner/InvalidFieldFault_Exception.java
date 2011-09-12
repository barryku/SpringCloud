
package com.barryku.cloud.sforce.partner;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.4.2
 * 2011-09-11T16:54:21.557-07:00
 * Generated source version: 2.4.2
 */

@WebFault(name = "InvalidFieldFault", targetNamespace = "urn:fault.partner.soap.sforce.com")
public class InvalidFieldFault_Exception extends Exception {
    
    private com.barryku.cloud.sforce.partner.InvalidFieldFault invalidFieldFault;

    public InvalidFieldFault_Exception() {
        super();
    }
    
    public InvalidFieldFault_Exception(String message) {
        super(message);
    }
    
    public InvalidFieldFault_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFieldFault_Exception(String message, com.barryku.cloud.sforce.partner.InvalidFieldFault invalidFieldFault) {
        super(message);
        this.invalidFieldFault = invalidFieldFault;
    }

    public InvalidFieldFault_Exception(String message, com.barryku.cloud.sforce.partner.InvalidFieldFault invalidFieldFault, Throwable cause) {
        super(message, cause);
        this.invalidFieldFault = invalidFieldFault;
    }

    public com.barryku.cloud.sforce.partner.InvalidFieldFault getFaultInfo() {
        return this.invalidFieldFault;
    }
}
