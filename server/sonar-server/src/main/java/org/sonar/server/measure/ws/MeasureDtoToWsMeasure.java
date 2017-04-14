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
import org.sonar.db.measure.MeasureDto;
import org.sonar.db.metric.MetricDto;
import org.sonarqube.ws.WsMeasures;
import org.sonarqube.ws.WsMeasures.Measure;

import static org.sonar.server.measure.ws.MeasureValueFormatter.formatMeasureValue;
import static org.sonar.server.measure.ws.MeasureValueFormatter.formatNumericalValue;

class MeasureDtoToWsMeasure {

  private MeasureDtoToWsMeasure() {
    // static methods
  }

  static Measure.Builder createWsMeasureBuilder(MetricDto metricDto, MeasureDto measureDto) {
    return createWsMeasureBuilder(metricDto, measureDto.getValue(), measureDto.getData(), measureDto.getVariation());
  }

  static Measure.Builder createWsMeasureBuilder(MetricDto metric, @Nullable Double doubleValue, @Nullable String stringValue, @Nullable Double variation) {
    Measure.Builder measure = Measure.newBuilder();
    measure.setMetric(metric.getKey());
    // a measure value can be null, new_violations metric for example
    if (doubleValue != null || stringValue != null) {
      measure.setValue(formatMeasureValue(doubleValue, stringValue, metric));
    }

    WsMeasures.PeriodValue.Builder periodBuilder = WsMeasures.PeriodValue.newBuilder();
    if (variation != null) {
      measure.getPeriodsBuilder().addPeriodsValue(periodBuilder
        .clear()
        .setIndex(1)
        .setValue(formatNumericalValue(variation, metric)));
    }
    return measure;
  }
}
