package com.redhat.syseng.soleng.rhpam.model.containers;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="kie-containers">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="kie-container" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="config-items" maxOccurs="unbounded" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="itemName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="itemValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="itemType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="messages">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="severity" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="release-id">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="artifact-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="group-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="resolved-release-id">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="artifact-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="group-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="scanner">
 *                               &lt;complexType>
 *                                 &lt;simpleContent>
 *                                   &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                                     &lt;attribute name="status" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                   &lt;/extension>
 *                                 &lt;/simpleContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                           &lt;attribute name="container-alias" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="container-id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="status" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="msg" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "kieContainers"
})
@XmlRootElement(name = "response")
public class Response {

    @XmlElement(name = "kie-containers", required = true)
    protected Response.KieContainers kieContainers;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "msg")
    protected String msg;

    /**
     * Gets the value of the kieContainers property.
     * 
     * @return
     *     possible object is
     *     {@link Response.KieContainers }
     *     
     */
    public Response.KieContainers getKieContainers() {
        return kieContainers;
    }

    /**
     * Sets the value of the kieContainers property.
     * 
     * @param value
     *     allowed object is
     *     {@link Response.KieContainers }
     *     
     */
    public void setKieContainers(Response.KieContainers value) {
        this.kieContainers = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the msg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Sets the value of the msg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsg(String value) {
        this.msg = value;
    }


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
     *         &lt;element name="kie-container" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="config-items" maxOccurs="unbounded" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="itemName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="itemValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="itemType" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="messages">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="severity" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="release-id">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="artifact-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="group-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="resolved-release-id">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="artifact-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="group-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="scanner">
     *                     &lt;complexType>
     *                       &lt;simpleContent>
     *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *                           &lt;attribute name="status" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                         &lt;/extension>
     *                       &lt;/simpleContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *                 &lt;attribute name="container-alias" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="container-id" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="status" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
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
        "kieContainer"
    })
    public static class KieContainers {

        @XmlElement(name = "kie-container")
        protected List<Response.KieContainers.KieContainer> kieContainer;

        /**
         * Gets the value of the kieContainer property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the kieContainer property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getKieContainer().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Response.KieContainers.KieContainer }
         * 
         * 
         */
        public List<Response.KieContainers.KieContainer> getKieContainer() {
            if (kieContainer == null) {
                kieContainer = new ArrayList<Response.KieContainers.KieContainer>();
            }
            return this.kieContainer;
        }


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
         *         &lt;element name="config-items" maxOccurs="unbounded" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="itemName" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="itemValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="itemType" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="messages">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="severity" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="release-id">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="artifact-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="group-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="resolved-release-id">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="artifact-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="group-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="scanner">
         *           &lt;complexType>
         *             &lt;simpleContent>
         *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
         *                 &lt;attribute name="status" type="{http://www.w3.org/2001/XMLSchema}string" />
         *               &lt;/extension>
         *             &lt;/simpleContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *       &lt;attribute name="container-alias" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="container-id" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="status" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "configItems",
            "messages",
            "releaseId",
            "resolvedReleaseId",
            "scanner"
        })
        public static class KieContainer {

            @XmlElement(name = "config-items")
            protected List<Response.KieContainers.KieContainer.ConfigItems> configItems;
            @XmlElement(required = true)
            protected Response.KieContainers.KieContainer.Messages messages;
            @XmlElement(name = "release-id", required = true)
            protected Response.KieContainers.KieContainer.ReleaseId releaseId;
            @XmlElement(name = "resolved-release-id", required = true)
            protected Response.KieContainers.KieContainer.ResolvedReleaseId resolvedReleaseId;
            @XmlElement(required = true)
            protected Response.KieContainers.KieContainer.Scanner scanner;
            @XmlAttribute(name = "container-alias")
            protected String containerAlias;
            @XmlAttribute(name = "container-id")
            protected String containerId;
            @XmlAttribute(name = "status")
            protected String status;

            /**
             * Gets the value of the configItems property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the configItems property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getConfigItems().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Response.KieContainers.KieContainer.ConfigItems }
             * 
             * 
             */
            public List<Response.KieContainers.KieContainer.ConfigItems> getConfigItems() {
                if (configItems == null) {
                    configItems = new ArrayList<Response.KieContainers.KieContainer.ConfigItems>();
                }
                return this.configItems;
            }

            /**
             * Gets the value of the messages property.
             * 
             * @return
             *     possible object is
             *     {@link Response.KieContainers.KieContainer.Messages }
             *     
             */
            public Response.KieContainers.KieContainer.Messages getMessages() {
                return messages;
            }

            /**
             * Sets the value of the messages property.
             * 
             * @param value
             *     allowed object is
             *     {@link Response.KieContainers.KieContainer.Messages }
             *     
             */
            public void setMessages(Response.KieContainers.KieContainer.Messages value) {
                this.messages = value;
            }

            /**
             * Gets the value of the releaseId property.
             * 
             * @return
             *     possible object is
             *     {@link Response.KieContainers.KieContainer.ReleaseId }
             *     
             */
            public Response.KieContainers.KieContainer.ReleaseId getReleaseId() {
                return releaseId;
            }

            /**
             * Sets the value of the releaseId property.
             * 
             * @param value
             *     allowed object is
             *     {@link Response.KieContainers.KieContainer.ReleaseId }
             *     
             */
            public void setReleaseId(Response.KieContainers.KieContainer.ReleaseId value) {
                this.releaseId = value;
            }

            /**
             * Gets the value of the resolvedReleaseId property.
             * 
             * @return
             *     possible object is
             *     {@link Response.KieContainers.KieContainer.ResolvedReleaseId }
             *     
             */
            public Response.KieContainers.KieContainer.ResolvedReleaseId getResolvedReleaseId() {
                return resolvedReleaseId;
            }

            /**
             * Sets the value of the resolvedReleaseId property.
             * 
             * @param value
             *     allowed object is
             *     {@link Response.KieContainers.KieContainer.ResolvedReleaseId }
             *     
             */
            public void setResolvedReleaseId(Response.KieContainers.KieContainer.ResolvedReleaseId value) {
                this.resolvedReleaseId = value;
            }

            /**
             * Gets the value of the scanner property.
             * 
             * @return
             *     possible object is
             *     {@link Response.KieContainers.KieContainer.Scanner }
             *     
             */
            public Response.KieContainers.KieContainer.Scanner getScanner() {
                return scanner;
            }

            /**
             * Sets the value of the scanner property.
             * 
             * @param value
             *     allowed object is
             *     {@link Response.KieContainers.KieContainer.Scanner }
             *     
             */
            public void setScanner(Response.KieContainers.KieContainer.Scanner value) {
                this.scanner = value;
            }

            /**
             * Gets the value of the containerAlias property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getContainerAlias() {
                return containerAlias;
            }

            /**
             * Sets the value of the containerAlias property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setContainerAlias(String value) {
                this.containerAlias = value;
            }

            /**
             * Gets the value of the containerId property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getContainerId() {
                return containerId;
            }

            /**
             * Sets the value of the containerId property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setContainerId(String value) {
                this.containerId = value;
            }

            /**
             * Gets the value of the status property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getStatus() {
                return status;
            }

            /**
             * Sets the value of the status property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setStatus(String value) {
                this.status = value;
            }


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
             *         &lt;element name="itemName" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="itemValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="itemType" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
                "itemName",
                "itemValue",
                "itemType"
            })
            public static class ConfigItems {

                @XmlElement(required = true)
                protected String itemName;
                @XmlElement(required = true)
                protected String itemValue;
                @XmlElement(required = true)
                protected String itemType;

                /**
                 * Gets the value of the itemName property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getItemName() {
                    return itemName;
                }

                /**
                 * Sets the value of the itemName property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setItemName(String value) {
                    this.itemName = value;
                }

                /**
                 * Gets the value of the itemValue property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getItemValue() {
                    return itemValue;
                }

                /**
                 * Sets the value of the itemValue property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setItemValue(String value) {
                    this.itemValue = value;
                }

                /**
                 * Gets the value of the itemType property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getItemType() {
                    return itemType;
                }

                /**
                 * Sets the value of the itemType property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setItemType(String value) {
                    this.itemType = value;
                }

            }


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
             *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="severity" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
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
                "content",
                "severity",
                "timestamp"
            })
            public static class Messages {

                @XmlElement(required = true)
                protected String content;
                @XmlElement(required = true)
                protected String severity;
                @XmlElement(required = true)
                @XmlSchemaType(name = "dateTime")
                protected XMLGregorianCalendar timestamp;

                /**
                 * Gets the value of the content property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getContent() {
                    return content;
                }

                /**
                 * Sets the value of the content property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setContent(String value) {
                    this.content = value;
                }

                /**
                 * Gets the value of the severity property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getSeverity() {
                    return severity;
                }

                /**
                 * Sets the value of the severity property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setSeverity(String value) {
                    this.severity = value;
                }

                /**
                 * Gets the value of the timestamp property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link XMLGregorianCalendar }
                 *     
                 */
                public XMLGregorianCalendar getTimestamp() {
                    return timestamp;
                }

                /**
                 * Sets the value of the timestamp property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link XMLGregorianCalendar }
                 *     
                 */
                public void setTimestamp(XMLGregorianCalendar value) {
                    this.timestamp = value;
                }

            }


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
             *         &lt;element name="artifact-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="group-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
                "artifactId",
                "groupId",
                "version"
            })
            public static class ReleaseId {

                @XmlElement(name = "artifact-id", required = true)
                protected String artifactId;
                @XmlElement(name = "group-id", required = true)
                protected String groupId;
                @XmlElement(required = true)
                protected String version;

                /**
                 * Gets the value of the artifactId property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getArtifactId() {
                    return artifactId;
                }

                /**
                 * Sets the value of the artifactId property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setArtifactId(String value) {
                    this.artifactId = value;
                }

                /**
                 * Gets the value of the groupId property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getGroupId() {
                    return groupId;
                }

                /**
                 * Sets the value of the groupId property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setGroupId(String value) {
                    this.groupId = value;
                }

                /**
                 * Gets the value of the version property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getVersion() {
                    return version;
                }

                /**
                 * Sets the value of the version property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setVersion(String value) {
                    this.version = value;
                }

            }


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
             *         &lt;element name="artifact-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="group-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
                "artifactId",
                "groupId",
                "version"
            })
            public static class ResolvedReleaseId {

                @XmlElement(name = "artifact-id", required = true)
                protected String artifactId;
                @XmlElement(name = "group-id", required = true)
                protected String groupId;
                @XmlElement(required = true)
                protected String version;

                /**
                 * Gets the value of the artifactId property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getArtifactId() {
                    return artifactId;
                }

                /**
                 * Sets the value of the artifactId property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setArtifactId(String value) {
                    this.artifactId = value;
                }

                /**
                 * Gets the value of the groupId property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getGroupId() {
                    return groupId;
                }

                /**
                 * Sets the value of the groupId property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setGroupId(String value) {
                    this.groupId = value;
                }

                /**
                 * Gets the value of the version property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getVersion() {
                    return version;
                }

                /**
                 * Sets the value of the version property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setVersion(String value) {
                    this.version = value;
                }

            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;simpleContent>
             *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
             *       &lt;attribute name="status" type="{http://www.w3.org/2001/XMLSchema}string" />
             *     &lt;/extension>
             *   &lt;/simpleContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "value"
            })
            public static class Scanner {

                @XmlValue
                protected String value;
                @XmlAttribute(name = "status")
                protected String status;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getValue() {
                    return value;
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setValue(String value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the status property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getStatus() {
                    return status;
                }

                /**
                 * Sets the value of the status property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setStatus(String value) {
                    this.status = value;
                }

            }

        }

    }

}