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
package org.opencloudengine.flamingo2.engine.common;

import java.util.Map;

public class KeyValueRepositoryImpl implements KeyValueRepository {

    Map<String, String> map;

    String defaultKey;

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public void setDefaultKey(String defaultKey) {
        this.defaultKey = defaultKey;
    }

    @Override
    public String getValue(String key) {
        if (map.get(key) != null) {
            return map.get(key);
        }
        return defaultKey;
    }

    @Override
    public String getDefaultValue() {
        return defaultKey;
    }
}
