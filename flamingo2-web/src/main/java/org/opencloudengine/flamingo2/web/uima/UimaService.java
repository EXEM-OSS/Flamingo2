/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opencloudengine.flamingo2.web.uima;

import org.opencloudengine.flamingo2.model.rest.Uima;

import java.util.List;
import java.util.Map;

/**
 * 로컬 DB에 저장된 UIMA Log 정보를 관리하기 위한 UIMA Service Interface
 *
 * @author Myeongha KIM
 * @since 2.1.0
 */
public interface UimaService {

    int getTotalCount(Map uimaMap);

    List<Uima> getAllUimaLogs(Map uimaMap);

    List<Map<String,Object>> getUimaLogSummary(Map uimaMap);
}
