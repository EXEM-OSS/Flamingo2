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
package org.opencloudengine.flamingo2.web.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    MenuRepository repository;

    @Override
    public List<Map<String, Object>> select(Map<String, Object> params) {
        List<Map<String, Object>> treeList = repository.select(params);
        List<Map<String, Object>> returnList = new ArrayList();

        String lvl;
        for (Map<String, Object> treeItem : treeList) {
            Boolean leaf = Boolean.valueOf(treeItem.get("leaf").toString());
            treeItem.put("leaf", leaf);
            lvl = treeItem.get("lvl").toString();

            if (lvl.equals("0")) {
                if (leaf) {
                    returnList.add(treeItem);
                } else {
                    String id = treeItem.get("menu_id").toString();
                    List<Map<String, Object>> childList = new ArrayList();
                    for (Map<String, Object> childItem : treeList) {
                        String parentId = childItem.get("prnts_menu_id").toString();
                        if (parentId.equals(id)) {
                            childList.add(childItem);
                        }
                    }

                    treeItem.put("children", childList);
                    returnList.add(treeItem);
                }
            }
        }
        return returnList;
    }

    @Override
    public List<Map<String, Object>> selectNode(Map<String, Object> params) {
        return repository.selectNode(params);
    }

    @Override
    public void insert(Map<String, Object> params) {
        repository.insert(params);
    }

    @Override
    public void update(Map<String, Object> params) {
        repository.update(params);
    }

    @Override
    public void delete(Map<String, Object> params) {
        repository.delete(params);
    }
}
