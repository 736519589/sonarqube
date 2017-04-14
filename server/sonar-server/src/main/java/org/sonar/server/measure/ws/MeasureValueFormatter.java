/*
 * SonarQube
 * Copyright (C) 2009-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.server.measure.ws;

import javax.annotation.Nullable;
import org.sonar.api.measures.Metric;
import org.sonar.db.measure.MeasureDto;
import org.sonar.db.metric.MetricDto;

class MeasureValueFormatter {
  private static final double DELTA = 0.000001d;

  private MeasureValueFormatter() {
    // static methods
  }

  static String formatMeasureValue(MeasureDto measure, MetricDto metric) {
    Double doubleValue = measure.getValue();
    String stringValue = measure.getData();
    return formatMeasureValue(doubleValue, stringValue, metric);
  }

  static String formatMeasureValue(@Nullable Double doubleValue, @Nullable String stringValue, MetricDto metric) {
    Metric.ValueType metricType = Metric.ValueType.valueOf(metric.getValueType());
    switch (metricType) {
      case BOOL:
        return formatBoolean(doubleValue);
      case INT:
        return formatInteger(doubleValue);
      case MILLISEC:
      case WORK_DUR:
        return formatLong(doubleValue);
      case FLOAT:
      case PERCENT:
      case RATING:
        return String.valueOf(doubleValue);
      case LEVEL:
      case STRING:
      case DATA:
      case DISTRIB:
        return stringValue;
      default:
        throw new IllegalArgumentException("Unsupported metric type: " + metricType.name());
    }
  }

  static String formatNumericalValue(Double value, MetricDto metric) {
    Metric.ValueType metricType = Metric.ValueType.valueOf(metric.getValueType());

    switch (metricType) {
      case BOOL:
        return formatBoolean(value);
      case INT:
        return formatInteger(value);
      case MILLISEC:
      case WORK_DUR:
        return formatLong(value);
      case FLOAT:
      case PERCENT:
      case RATING:
        return String.valueOf(value);
      case LEVEL:
      case STRING:
      case DATA:
      case DISTRIB:
      default:
        throw new IllegalArgumentException(String.format("Unsupported metric type '%s' for numerical value", metricType.name()));
    }
  }

  private static String formatBoolean(Double value) {
    return Math.abs(value - 1.0d) < DELTA ? "true" : "false";
  }

  private static String formatInteger(Double value) {
    return String.valueOf(value.intValue());
  }

  private static String formatLong(Double value) {
    return String.valueOf(value.longValue());
  }
}
