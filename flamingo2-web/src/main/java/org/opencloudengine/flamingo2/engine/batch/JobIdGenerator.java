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
package org.opencloudengine.flamingo2.engine.batch;

import org.opencloudengine.flamingo2.model.rest.Workflow;
import org.opencloudengine.flamingo2.util.DateUtils;
import org.opencloudengine.flamingo2.util.JVMIDUtils;
import org.slf4j.helpers.MessageFormatter;

import java.util.Date;

/**
 * Job ID를 생성한다.
 *
 * @author Byoung Gon, Kim
 * @version 0.4
 */
public class JobIdGenerator {

    /**
     * 해당 워크플로우의 Job ID를 생성한다.
     *
     * @param workflow 워크플로우
     * @return Job ID
     */
    public static String generate(Workflow workflow) {
        String random = JVMIDUtils.generateUUID();
        long workflowId = workflow.getId();
        Date date = new Date();
        return MessageFormatter.arrayFormat("JOB_{}_{}_{}_{}", new Object[]{
                DateUtils.parseDate(date, "yyyyMMdd"), DateUtils.parseDate(date, "HHmmss"), workflowId, random
        }).getMessage();
    }

    /**
     * 해당 워크플로우의 Job ID를 생성한다.
     *
     * @param workflow 워크플로우
     * @return Job ID
     */
    public static String generateKey(Workflow workflow) {
        String random = JVMIDUtils.generateUUID();
        long workflowId = workflow.getId();
        Date date = new Date();
        return MessageFormatter.arrayFormat("JOB_{}_{}_{}", new Object[]{
                DateUtils.parseDate(date, "yyyyMMdd"), workflowId, random
        }).getMessage();
    }

}
