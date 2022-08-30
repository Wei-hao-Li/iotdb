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

package org.apache.iotdb.commons.trigger;

import org.apache.iotdb.common.rpc.thrift.TDataNodeLocation;
import org.apache.iotdb.commons.path.PartialPath;
import org.apache.iotdb.confignode.rpc.thrift.TTriggerState;

/** This Class used to save the specific information of one Trigger. */
public class TriggerInformation { // TODO Serialized
  private PartialPath pathPattern;
  private String className;
  private String jarName;
  private TTriggerState triggerState;

  /** indicate this Trigger is Stateful or Stateless */
  private boolean isStateful;

  /** only used for Stateful Trigger */
  private TDataNodeLocation dataNodeLocation;

  public TriggerInformation(
      PartialPath pathPattern,
      String className,
      String jarName,
      TTriggerState triggerState,
      boolean isStateful,
      TDataNodeLocation dataNodeLocation) {
    this.pathPattern = pathPattern;
    this.className = className;
    this.jarName = jarName;
    this.triggerState = triggerState;
    this.isStateful = isStateful;
    this.dataNodeLocation = dataNodeLocation;
  }

  public PartialPath getPathPattern() {
    return pathPattern;
  }

  public void setPathPattern(PartialPath pathPattern) {
    this.pathPattern = pathPattern;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getJarName() {
    return jarName;
  }

  public void setJarName(String jarName) {
    this.jarName = jarName;
  }

  public TTriggerState getTriggerState() {
    return triggerState;
  }

  public void setTriggerState(TTriggerState triggerState) {
    this.triggerState = triggerState;
  }

  public boolean isStateful() {
    return isStateful;
  }

  public void setStateful(boolean stateful) {
    isStateful = stateful;
  }

  public TDataNodeLocation getDataNodeLocation() {
    return dataNodeLocation;
  }

  public void setDataNodeLocation(TDataNodeLocation dataNodeLocation) {
    this.dataNodeLocation = dataNodeLocation;
  }
}
