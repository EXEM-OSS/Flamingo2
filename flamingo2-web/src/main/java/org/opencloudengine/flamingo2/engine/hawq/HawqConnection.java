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
package org.opencloudengine.flamingo2.engine.hawq;

import java.sql.Connection;

/**
 * query 종료를 위해 pid 를 함께 관리하기 위한 class.
 *
 * @author Ha Neul, Kim
 * @since 2.0
 */
public class HawqConnection {

    private Connection connection;

    private int pid;

    public HawqConnection(Connection connection, int pid) {
        this.connection = connection;
        this.pid = pid;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "HawqConnection{" +
                "connection=" + connection +
                ", pid=" + pid +
                '}';
    }
}