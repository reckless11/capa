/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package group.rxcloud.capa.component.telemetry.metrics;

import group.rxcloud.capa.component.telemetry.SamplerConfig;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.metrics.exemplar.ExemplarFilter;

/**
 * Sampler for metrics data.
 * Choose to sample all or none data according to the config file.
 */
public class CapaMetricsSampler implements ExemplarFilter {

    /**
     * Sampler instance, which samples all the data if no config was explicitly set.
     */
    private static final CapaMetricsSampler INSTANCE = new CapaMetricsSampler(SamplerConfig.DEFAULT_CONFIG);

    /**
     * Inner instance.
     */
    private ExemplarFilter inner;

    /**
     * Get the sampler instance.
     *
     * @return the sampler instance.
     */
    public static CapaMetricsSampler getInstance() {
        return INSTANCE;
    }

    private CapaMetricsSampler(SamplerConfig config) {
        update(config);
    }

    /**
     * Update the sample policy.
     *
     * @param config new sample config.
     * @return the updated sampler instance.
     */
    public CapaMetricsSampler update(SamplerConfig config) {
        if (config == null) {
            return this;
        }

        if (config.isMetricsSample()) {
            inner = ExemplarFilter.alwaysSample();
        } else {
            inner = ExemplarFilter.neverSample();
        }
        return this;
    }

    @Override
    public boolean shouldSampleMeasurement(long value, Attributes attributes, Context context) {
        return inner.shouldSampleMeasurement(value, attributes, context);
    }

    @Override
    public boolean shouldSampleMeasurement(double value, Attributes attributes, Context context) {
        return inner.shouldSampleMeasurement(value, attributes, context);
    }
}
