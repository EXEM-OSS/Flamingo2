/*
 * Copyright (C) 2011 Flamingo Project (https://github.com/OpenCloudEngine/flamingo2).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.flamingo2.core.exception;

import org.apache.commons.lang.math.RandomUtils;
import org.opencloudengine.flamingo2.util.DateUtils;

/**
 * Default Service Exception.
 *
 * @author Byoung Gon, Kim
 * @since 0.5
 */
public class ServiceException extends RuntimeException {

    /**
     * Serialization UID
     */
    private static final long serialVersionUID = 1;

    private String exceptionId = DateUtils.getCurrentDateTime() + "_" + RandomUtils.nextLong();

    private String message = null;

    private int exitCode;

    private int code;

    private String recentLog;

    /**
     * 기본 생성자.
     */
    public ServiceException() {
        super();
    }

    /**
     * 기본 생성자.
     *
     * @param message 예외 메시지
     */
    public ServiceException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * 기본 생성자.
     *
     * @param message 예외 메시지
     * @param cause   예외 원인
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    /**
     * 기본 생성자.
     *
     * @param message 예외 메시지
     * @param code    에러 코드
     * @param cause   예외 원인
     */
    public ServiceException(String message, int code, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    /**
     * 기본 생성자.
     *
     * @param cause 예외 원인
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * 기본 생성자.
     *
     * @param exitCode  프로세스 종료 코드
     * @param recentLog 최근 실행 로그
     */
    public ServiceException(int exitCode, String recentLog) {
        this.exitCode = exitCode;
        this.message = String.valueOf(exitCode);
        this.recentLog = recentLog;
    }

    public int getExitCode() {
        return exitCode;
    }

    public String getRecentLog() {
        return recentLog;
    }

    /**
     * Root Cause를 반환한다.
     *
     * @return Root Cause
     */
    public Throwable getRootCause() {
        return super.getCause();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    public String getExceptionId() {
        return exceptionId;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setRecentLog(String recentLog) {
        this.recentLog = recentLog;
    }

    @Override
    public String toString() {
        return message;
    }
}
