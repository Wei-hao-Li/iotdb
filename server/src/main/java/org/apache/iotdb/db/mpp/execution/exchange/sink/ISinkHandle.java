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
package org.apache.iotdb.db.mpp.execution.exchange.sink;

import org.apache.iotdb.tsfile.read.common.block.TsBlock;

import com.google.common.util.concurrent.ListenableFuture;

public interface ISinkHandle {

  /** Get the local fragment instance ID that this sink handle belongs to. */
  String getSinkHandleId();

  /** Get the total amount of memory used by buffered TsBlocks. */
  long getBufferRetainedSizeInBytes();

  /** Get a future that will be completed when the output buffer is not full. */
  ListenableFuture<?> isFull();

  /**
   * Send a {@link TsBlock} to an un-partitioned output buffer. If no-more-TsBlocks has been set,
   * the invocation will be ignored. This can happen with limit queries. A {@link RuntimeException}
   * will be thrown if any exception happened during the data transmission.
   */
  void send(TsBlock tsBlock);

  /**
   * Notify the handle that there are no more TsBlocks. Any future calls to send a TsBlock should be
   * ignored.
   */
  void setNoMoreTsBlocks();

  /**
   * Notify the handle that there are no more TsBlocks for the specified channel. Any future calls
   * to send a TsBlock to the specified channel should be ignored.
   *
   * @param channelIndex index of the channel that should be closed
   */
  default void setNoMoreTsBlocksOfOneChannel(int channelIndex) {
    throw new UnsupportedOperationException();
  }

  /** Open specified channel of ISinkHandle. */
  default void tryOpenChannel(int channelIndex) {
    throw new UnsupportedOperationException();
  }

  /** If the handle is aborted. */
  boolean isAborted();

  /**
   * If there are no more TsBlocks to be sent and all the TsBlocks have been fetched by downstream
   * fragment instances.
   */
  boolean isFinished();

  /**
   * Abort the sink handle. Discard all TsBlocks which may still be in the memory buffer and cancel
   * the future returned by {@link #isFull()}.
   *
   * <p>Should only be called in abnormal case
   */
  void abort();

  /**
   * Close the sink handle. Discard all TsBlocks which may still be in the memory buffer and
   * complete the future returned by {@link #isFull()}.
   *
   * <p>Should only be called in normal case.
   */
  void close();

  /** Set max bytes this handle can reserve from memory pool */
  void setMaxBytesCanReserve(long maxBytesCanReserve);
}
