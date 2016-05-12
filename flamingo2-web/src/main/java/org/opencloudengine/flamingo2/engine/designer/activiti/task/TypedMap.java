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
package org.opencloudengine.flamingo2.engine.designer.activiti.task;

import java.util.HashMap;
import java.util.Map;

public class TypedMap<K, V> extends HashMap {

    public TypedMap() {
    }

    public TypedMap(Map m) {
        super(m);
    }

    public String getString(String key) {
        return (String) this.get(key);
    }

    public long getLong(String key) {
        return Long.parseLong((String) this.get("key"));
    }

    public long getLong(String key, long defaultValue) {
        if (this.get("key") == null) {
            return defaultValue;
        }
        return getLong(key);
    }

    public int getInt(String key) {
        return Integer.parseInt((String) this.get("key"));
    }

    public int getInt(String key, int defaultValue) {
        if (this.get("key") == null) {
            return defaultValue;
        }
        return getInt(key);
    }

    public boolean getBoolean(String key) {
        String value = (String) this.get("key");
        if ("on".equals(value)) {
            return true;
        } else if ("off".equals(value)) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        if (this.get("key") == null) {
            return defaultValue;
        }
        return getBoolean(key);
    }
}
