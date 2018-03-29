/*
 * Copyright 2014 Aurélien Broszniowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.rainfall;

import io.rainfall.exceptions.TestException;
import io.rainfall.statistics.StatisticsHolder;
import io.rainfall.utils.ConcurrentPseudoRandom;

import java.util.List;
import java.util.Map;

/**
 * This executes a {@link Scenario}, with the specific {@link Configuration}, and {@link Assertion}
 *
 * @author Aurelien Broszniowski
 */

public abstract class Execution {

  protected ConcurrentPseudoRandom weightRnd = new ConcurrentPseudoRandom();

  public abstract <E extends Enum<E>> void execute(final StatisticsHolder<E> statisticsHolder, final Scenario scenario,
                                                   final Map<Class<? extends Configuration>, Configuration> configurations,
                                                   final List<AssertionEvaluator> assertions) throws TestException;

}
