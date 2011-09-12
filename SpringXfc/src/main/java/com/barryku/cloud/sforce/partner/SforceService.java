package com.barryku.cloud.sforce.partner;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * Sforce SOAP API
 *
 * This class was generated by Apache CXF 2.4.2
 * 2011-09-11T16:54:21.864-07:00
 * Generated source version: 2.4.2
 * 
 */
@WebServiceClient(name = "SforceService", 
                  wsdlLocation = "classpath:/partner.wsdl",
                  targetNamespace = "urn:partner.soap.sforce.com") 
public class SforceService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("urn:partner.soap.sforce.com", "SforceService");
    public final static QName Soap = new QName("urn:partner.soap.sforce.com", "Soap");
    static {
        URL url = SforceService.class.getClassLoader().getResource("/partner.wsdl");
        if (url == null) {
            url = SforceService.class.getClassLoader().getResource("partner.wsdl");
        }
        if (url == null) {
            java.util.logging.Logger.getLogger(SforceService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "classpath:/partner.wsdl");
        }       
        WSDL_LOCATION = url;
    }

    public SforceService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public SforceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SforceService() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns Soap
     */
    @WebEndpoint(name = "Soap")
    public Soap getSoap() {
        return super.getPort(Soap, Soap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns Soap
     */
    @WebEndpoint(name = "Soap")
    public Soap getSoap(WebServiceFeature... features) {
        return super.getPort(Soap, Soap.class, features);
    }

}
