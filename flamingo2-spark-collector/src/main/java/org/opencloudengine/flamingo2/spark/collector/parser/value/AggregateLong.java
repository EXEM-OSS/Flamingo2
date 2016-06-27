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
package org.opencloudengine.flamingo2.spark.collector.parser.value;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hyokun Park on 15. 8. 12..
 */
public class AggregateLong {
    public Long MIN = Long.valueOf(0);

    public Long MAX = Long.valueOf(0);

    public Long AVG = Long.valueOf(0);

    public Long SUM = Long.valueOf(0);

    public Long CNT = Long.valueOf(0);

    public String toJson() {
        Map map = new HashMap();
        Gson gson = new Gson();

        map.put("min", MIN);
        map.put("max", MAX);
        map.put("avg", AVG);
        map.put("sum", SUM);
        map.put("cnt", CNT);

        return gson.toJson(map);
    }
}
