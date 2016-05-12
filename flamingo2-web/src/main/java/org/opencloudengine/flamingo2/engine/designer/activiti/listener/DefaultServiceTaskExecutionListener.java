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
package org.opencloudengine.flamingo2.engine.designer.activiti.listener;

import org.activiti.engine.delegate.DelegateExecution;

public abstract class DefaultServiceTaskExecutionListener implements org.activiti.engine.delegate.ExecutionListener {

    private DelegateExecution delegateExecution;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        this.delegateExecution = delegateExecution;
        this.onNotify(delegateExecution);
    }

    public abstract void onNotify(DelegateExecution execution) throws Exception;

    public DelegateExecution getDelegateExecution() {
        return delegateExecution;
    }
}