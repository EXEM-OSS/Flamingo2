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
import org.opencloudengine.flamingo2.util.ExceptionUtils;
import org.opencloudengine.flamingo2.util.JVMIDUtils;

import java.sql.Timestamp;
import java.util.Date;

public class SystemEvent {

    Long id;
    String category;
    String service;
    String action;
    Timestamp registrationDate;
    String status;
    String message;
    String cause;
    String exception;
    String identifier;
    String username;
    String yyyy;
    String mm;
    String dd;

    public static SystemEvent create(String category, String service, String action, String status, String message) {
        SystemEvent event = new SystemEvent();
        event.category = category;
        event.service = service;
        event.action = action;
        event.status = status;
        event.message = message;
        event.username = SessionUtils.getUsername();

        Date date = new Date();
        event.registrationDate = new Timestamp(date.getTime());
        event.yyyy = DateUtils.parseDate(date, "yyyy");
        event.mm = DateUtils.parseDate(date, "MM");
        event.dd = DateUtils.parseDate(date, "dd");
        event.identifier = event.yyyy + event.mm + event.dd + "_" + JVMIDUtils.generateUUID();
        return event;
    }

    public static SystemEvent create(String category, String service, String action, String status, String message, Exception e) {
        SystemEvent event = create(category, service, action, status, message);
        if (e.getCause() != null) event.cause = e.getCause().getMessage();
        event.exception = ExceptionUtils.getFullStackTrace(e);
        return event;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getDd() {
        return dd;
    }

    public void setDd(String dd) {
        this.dd = dd;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMm() {
        return mm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifer) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "ID='" + id + '\'' +
                ", Category='" + category + '\'' +
                ", Service='" + service + '\'' +
                ", Action='" + action + '\'' +
                ", Registration=" + registrationDate +
                ", Status='" + status + '\'' +
                ", Message='" + message + '\'' +
                ", Cause='" + cause + '\'' +
                ", Exception='" + exception + '\'' +
                ", Identifier='" + identifier + '\'' +
                ", Username='" + username + '\'' +
                ", YYYY='" + yyyy + '\'' +
                ", MM='" + mm + '\'' +
                ", DD='" + dd + '\'';
    }
}