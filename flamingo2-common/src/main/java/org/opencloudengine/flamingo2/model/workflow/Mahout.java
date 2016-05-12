package org.opencloudengine.flamingo2.model.workflow;

import javax.xml.bind.annotation.*;

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
 *         &lt;element ref="{http://www.openflamingo.org/schema/workflow}prepare" minOccurs="0"/>
 *         &lt;element ref="{http://www.openflamingo.org/schema/workflow}clusterName" minOccurs="0"/>
 *         &lt;element ref="{http://www.openflamingo.org/schema/workflow}command" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="override" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "prepare",
        "clusterName",
        "command"
})
@XmlRootElement(name = "mahout")
public class Mahout {
    protected Prepare prepare;
    protected ClusterName clusterName;
    protected Command command;
    @XmlAttribute(name = "override")
    protected Boolean override;

    public Prepare getPrepare() {
        return prepare;
    }

    public void setPrepare(Prepare prepare) {
        this.prepare = prepare;
    }

    public ClusterName getClusterName() {
        return clusterName;
    }

    public void setClusterName(ClusterName clusterName) {
        this.clusterName = clusterName;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Boolean getOverride() {
        return override;
    }

    public void setOverride(Boolean override) {
        this.override = override;
    }
}
