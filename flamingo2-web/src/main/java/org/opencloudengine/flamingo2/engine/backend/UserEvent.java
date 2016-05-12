/**
 * Copyright (C) 2011 Flamingo Project (http://www.cloudine.io).
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.flamingo2.engine.backend;

import org.opencloudengine.flamingo2.core.security.SessionUtils;
import org.opencloudengine.flamingo2.util.DateUtils;
import org.opencloudengine.flamingo2.util.JVMIDUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class UserEvent implements Serializable {

    Long id;
    String name;
    Timestamp registrationDate;
    String status;
    String message;
    Boolean isSee;
    String identifier;
    Long referenceId;
    String username;
    String yyyy;
    String mm;
    String dd;

    public static UserEvent create(String username, String name, String status) {
        UserEvent event = new UserEvent();
        event.name = name;
        event.isSee = false;
        event.status = status;
        event.username = username;
        Date date = new Date();
        event.registrationDate = new Timestamp(date.getTime());
        event.yyyy = DateUtils.parseDate(date, "yyyy");
        event.mm = DateUtils.parseDate(date, "MM");
        event.dd = DateUtils.parseDate(date, "dd");

        event.identifier = event.yyyy + event.mm + event.dd + "_" + System.currentTimeMillis() + "_" + JVMIDUtils.generateUUID();
        return event;
    }

    public static UserEvent create(String name, String status) {
        UserEvent event = new UserEvent();
        event.name = name;
        event.isSee = false;
        event.status = status;
        event.username = SessionUtils.getUsername();
        Date date = new Date();
        event.registrationDate = new Timestamp(date.getTime());
        event.yyyy = DateUtils.parseDate(date, "yyyy");
        event.mm = DateUtils.parseDate(date, "MM");
        event.dd = DateUtils.parseDate(date, "dd");

        event.identifier = event.yyyy + event.mm + event.dd + "_" + System.currentTimeMillis() + "_" + JVMIDUtils.generateUUID();
        return event;
    }

    public static UserEvent create(String name, String status, String message, String identifier, Long referenceId) {
        UserEvent event = new UserEvent();
        event.name = name;
        event.isSee = false;
        event.identifier = identifier;
        event.referenceId = referenceId;
        event.status = status;
        event.message = message;
        event.username = SessionUtils.getUsername();

        Date date = new Date();
        event.registrationDate = new Timestamp(date.getTime());
        event.yyyy = DateUtils.parseDate(date, "yyyy");
        event.mm = DateUtils.parseDate(date, "MM");
        event.dd = DateUtils.parseDate(date, "dd");
        return event;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsSee() {
        return isSee;
    }

    public void setIsSee(Boolean isSee) {
        this.isSee = isSee;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getYyyy() {
        return yyyy;
    }

    public void setYyyy(String yyyy) {
        this.yyyy = yyyy;
    }

    public String getMm() {
        return mm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public String getDd() {
        return dd;
    }

    public void setDd(String dd) {
        this.dd = dd;
    }

    @Override
    public String toString() {
        return "ID='" + id + '\'' +
                ", Name='" + name + '\'' +
                ", Registration=" + registrationDate +
                ", Status='" + status + '\'' +
                ", Message='" + message + '\'' +
                ", IsSee='" + isSee + '\'' +
                ", Identifier='" + identifier + '\'' +
                ", ReferenceId='" + referenceId + '\'' +
                ", Username='" + username + '\'' +
                ", YYYY='" + yyyy + '\'' +
                ", MM='" + mm + '\'' +
                ", DD='" + dd + '\'';
    }
}