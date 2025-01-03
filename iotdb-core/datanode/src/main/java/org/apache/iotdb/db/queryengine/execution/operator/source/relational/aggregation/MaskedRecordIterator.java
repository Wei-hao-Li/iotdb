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

package org.apache.iotdb.db.queryengine.execution.operator.source.relational.aggregation;

import org.apache.iotdb.commons.udf.access.RecordIterator;
import org.apache.iotdb.commons.udf.utils.UDFDataTypeTransformer;
import org.apache.iotdb.udf.api.relational.access.Record;

import org.apache.tsfile.block.column.Column;
import org.apache.tsfile.common.conf.TSFileConfig;
import org.apache.tsfile.read.common.type.Type;
import org.apache.tsfile.utils.Binary;
import org.apache.tsfile.utils.DateUtils;

import java.time.LocalDate;
import java.util.List;

public class MaskedRecordIterator extends RecordIterator {
  private final int[] selectedPositions;

  public MaskedRecordIterator(
      List<Column> childrenColumns, List<Type> dataTypes, AggregationMask mask) {
    super(childrenColumns, dataTypes, mask.getSelectedPositionCount());
    this.selectedPositions = mask.getSelectedPositions();
  }

  @Override
  public Record next() {
    final int index = selectedPositions[currentIndex++];
    return new Record() {
      @Override
      public int getInt(int columnIndex) {
        return childrenColumns.get(columnIndex).getInt(index);
      }

      @Override
      public long getLong(int columnIndex) {
        return childrenColumns.get(columnIndex).getLong(index);
      }

      @Override
      public float getFloat(int columnIndex) {
        return childrenColumns.get(columnIndex).getFloat(index);
      }

      @Override
      public double getDouble(int columnIndex) {
        return childrenColumns.get(columnIndex).getDouble(index);
      }

      @Override
      public boolean getBoolean(int columnIndex) {
        return childrenColumns.get(columnIndex).getBoolean(index);
      }

      @Override
      public Binary getBinary(int columnIndex) {
        return childrenColumns.get(columnIndex).getBinary(index);
      }

      @Override
      public String getString(int columnIndex) {
        return childrenColumns
            .get(columnIndex)
            .getBinary(index)
            .getStringValue(TSFileConfig.STRING_CHARSET);
      }

      @Override
      public LocalDate getLocalDate(int columnIndex) {
        return DateUtils.parseIntToLocalDate(childrenColumns.get(columnIndex).getInt(index));
      }

      @Override
      public org.apache.iotdb.udf.api.type.Type getDataType(int columnIndex) {
        return UDFDataTypeTransformer.transformReadTypeToUDFDataType(dataTypes.get(columnIndex));
      }

      @Override
      public boolean isNull(int columnIndex) {
        return childrenColumns.get(columnIndex).isNull(index);
      }

      @Override
      public int size() {
        return childrenColumns.size();
      }
    };
  }
}
