/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.iotdb.db.mpp.plan.execution.config.metadata;

import org.apache.iotdb.commons.path.PartialPath;
import org.apache.iotdb.commons.trigger.enums.TriggerEvent;
import org.apache.iotdb.commons.trigger.enums.TriggerType;
import org.apache.iotdb.db.mpp.plan.execution.config.ConfigTaskResult;
import org.apache.iotdb.db.mpp.plan.execution.config.IConfigTask;
import org.apache.iotdb.db.mpp.plan.execution.config.executor.IConfigTaskExecutor;
import org.apache.iotdb.db.mpp.plan.statement.metadata.CreateTriggerStatement;

import com.google.common.util.concurrent.ListenableFuture;

public class CreateTriggerTask implements IConfigTask {
  private final String triggerName;

  private final String className;

  private final TriggerEvent triggerEvent;

  private final TriggerType triggerType;

  private final PartialPath pathPattern;

  public CreateTriggerTask(CreateTriggerStatement createTriggerStatement) {
    this.triggerName = createTriggerStatement.getTriggerName();
    this.className = createTriggerStatement.getClassName();
    this.triggerEvent = createTriggerStatement.getTriggerEvent();
    this.triggerType = createTriggerStatement.getTriggerType();
    this.pathPattern = createTriggerStatement.getPathPattern();
  }

  @Override
  public ListenableFuture<ConfigTaskResult> execute(IConfigTaskExecutor configTaskExecutor)
      throws InterruptedException {
    return configTaskExecutor.createTrigger(
        triggerName, className, triggerEvent, triggerType, pathPattern);
  }
}
