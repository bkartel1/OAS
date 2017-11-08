
package ws.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for createNewCustomerEntity complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createNewCustomerEntity"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="c" type="{http://ws.session.ejb/}customerEntity" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createNewCustomerEntity", propOrder = {
    "c"
})
public class CreateNewCustomerEntity {

    protected CustomerEntity c;

    /**
     * Gets the value of the c property.
     * 
     * @return
     *     possible object is
     *     {@link CustomerEntity }
     *     
     */
    public CustomerEntity getC() {
        return c;
    }

    /**
     * Sets the value of the c property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomerEntity }
     *     
     */
    public void setC(CustomerEntity value) {
        this.c = value;
    }

}
