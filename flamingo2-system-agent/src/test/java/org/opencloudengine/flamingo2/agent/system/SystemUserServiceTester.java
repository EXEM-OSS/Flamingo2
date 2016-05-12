/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opencloudengine.flamingo2.agent.system;

import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import static org.slf4j.helpers.MessageFormatter.arrayFormat;

public class SystemUserServiceTester {

	public static void main(String[] args) throws InterruptedException {
		String url = getRemoteServiceUrl("10.0.1.9", 18080);
//		String url = getRemoteServiceUrl("localhost", 29090);

		SystemUserService service = getRemoteService(url, SystemUserService.class);

//        System.out.println(service.existUser("hong"));
//        System.out.println(service.createUser("hong","hong"));
//        System.out.println(service.changePassword("hong","hong"));
//        System.out.println(service.changePassword("hong","kim"));
        System.out.println(service.deleteUser("hong"));
	}

	public static <T> T getRemoteService(String url, Class<T> clazz) {
		HttpInvokerProxyFactoryBean factoryBean = new HttpInvokerProxyFactoryBean();
		factoryBean.setServiceUrl(url);
		factoryBean.setServiceInterface(clazz);
		factoryBean.afterPropertiesSet();
		return (T) factoryBean.getObject();
	}

	public static String getRemoteServiceUrl(String ip, int port) {
		return arrayFormat("http://{}:{}/remote/agent/system", new Object[]{ip, port}).getMessage();
	}
}
