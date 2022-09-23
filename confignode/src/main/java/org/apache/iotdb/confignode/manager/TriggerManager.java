/*
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
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.iotdb.confignode.manager;

import org.apache.iotdb.common.rpc.thrift.TDataNodeLocation;
import org.apache.iotdb.common.rpc.thrift.TSStatus;
import org.apache.iotdb.commons.path.PartialPath;
import org.apache.iotdb.commons.trigger.TriggerInformation;
import org.apache.iotdb.confignode.persistence.TriggerInfo;
import org.apache.iotdb.confignode.rpc.thrift.TCreateTriggerReq;
import org.apache.iotdb.confignode.rpc.thrift.TDropTriggerReq;
import org.apache.iotdb.confignode.rpc.thrift.TTriggerState;
import org.apache.iotdb.trigger.api.enums.TriggerType;
import org.apache.iotdb.tsfile.utils.Binary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(TriggerManager.class);

  private final ConfigManager configManager;
  private final TriggerInfo triggerInfo;

  public TriggerManager(ConfigManager configManager, TriggerInfo triggerInfo) {
    this.configManager = configManager;
    this.triggerInfo = triggerInfo;
  }

  public TriggerInfo getTriggerInfo() {
    return triggerInfo;
  }

  public TSStatus createTrigger(TCreateTriggerReq req) {
    boolean isStateful = TriggerType.construct(req.getTriggerType()) == TriggerType.STATEFUL;
    TDataNodeLocation dataNodeLocation =
        isStateful ? configManager.getNodeManager().getLowestLoadDataNode() : null;
    TriggerInformation triggerInformation =
        new TriggerInformation(
            PartialPath.deserialize(req.pathPattern),
            req.getTriggerName(),
            req.getClassName(),
            req.getJarPath(), // TODO: download jar if is URI
            req.getAttributes(),
            TTriggerState.INACTIVE,
            isStateful,
            dataNodeLocation,
            req.getJarMD5());
    return configManager
        .getProcedureManager()
        .createTrigger(triggerInformation, new Binary(req.getJarFile()));
  }

  public TSStatus dropTrigger(TDropTriggerReq req) {
    // TODO
    return null;
  }
}
