package org.opencloudengine.flamingo2.model.rest;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Organization Domain Object.
 *
 * @author Myeongha KIM
 * @since 2.0
 */
public class Organization implements Serializable {

    private Long id;

    private String orgCD;

    private String orgNM;

    private String description;

    private Timestamp registerDate;

    private Timestamp updateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgCD() {
        return orgCD;
    }

    public void setOrgCD(String orgCD) {
        this.orgCD = orgCD;
    }

    public String getOrgNM() {
        return orgNM;
    }

    public void setOrgNM(String orgNM) {
        this.orgNM = orgNM;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Timestamp registerDate) {
        this.registerDate = registerDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", orgCD='" + orgCD + '\'' +
                ", orgNM='" + orgNM + '\'' +
                ", description='" + description + '\'' +
                ", registerDate=" + registerDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
