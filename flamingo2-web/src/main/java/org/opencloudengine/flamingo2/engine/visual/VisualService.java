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
package org.opencloudengine.flamingo2.engine.visual;

import com.jcraft.jsch.JSchException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface VisualService {

    public Map loadHdfs(Map params) throws JSchException, IOException;

    public Map listVariablesHdfs(Map params) throws JSchException, IOException;

    public Map listVariablesLocal(Map params) throws JSchException, IOException;

    public Map createPng(Map params) throws JSchException, IOException;

    public Map saveFile(MultipartFile file, String options) throws IOException;

    public Map reloadData(Map params) throws IOException;
}
