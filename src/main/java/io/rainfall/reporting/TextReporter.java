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

package io.rainfall.reporting;

import io.rainfall.Reporter;
import io.rainfall.statistics.Result;
import io.rainfall.statistics.RuntimeStatisticsObserversHolder;
import io.rainfall.statistics.Statistics;
import io.rainfall.statistics.StatisticsHolder;
import io.rainfall.statistics.StatisticsObserver;

import java.text.NumberFormat;
import java.util.Set;


/**
 * report the statistics to the text console
 *
 * @author Aurelien Broszniowski
 */

public class TextReporter implements Reporter {

  private static final String FORMAT = "%-15s %-15s %12s %10s %10s";
  //  private static final String FORMAT = "%-15s %-7s %12s %10s %10s %10s %10s %10s";
  private static final NumberFormat nf = NumberFormat.getInstance();
  private String CRLF = System.getProperty("line.separator");

  @Override
  public void report(final RuntimeStatisticsObserversHolder observersFactory) {
    StringBuilder sb = new StringBuilder();
    sb.append("==================================================== CUMULATIVE =========================================")
        .append(CRLF);
    sb.append(String.format(FORMAT, "Cache", "Type", "Txn_Count", "TPS", "Avg_Lat"))
//    sb.append(String.format(FORMAT, "Cache", "Type", "Txn_Count", "TPS", "Avg_Lat", "Min_Lat", "Max_Lat", "TotalExceptionCount"))
        .append(CRLF);
    sb.append("==========================================================================================================")
        .append(CRLF);

    Set<String> statKeys = observersFactory.getStatisticObserverKeys();
    for (String statKey : statKeys) {
      StatisticsObserver observer = observersFactory.getStatisticObserver(statKey);

      StatisticsHolder holder = observer.peek();
      logStats(sb, statKey, holder);
    }
    StatisticsObserver totalStatisticObserver = observersFactory.getTotalStatisticObserver();
    if (totalStatisticObserver != null)
      logStats(sb, "ALL", totalStatisticObserver.peek());

    System.out.println(sb.toString());
  }

  private void logStats(StringBuilder sb, String name, StatisticsHolder holder) {
    sb.append(format(holder.getTimestamp())).append(CRLF);
    Statistics statistics = holder.getStatistics();
    Result[] keys = statistics.getKeys();
    for (Result key : keys) {
      sb.append(String.format(FORMAT,
          name,
          key.value(),
          nf.format(statistics.getCounter(key)),
          nf.format(statistics.getTps(key)),
          nf.format(statistics.getAverageLatencyInMs(key))
      )).append(CRLF);
    }
    sb.append(String.format(FORMAT,
        name,
        "TOTAL",
        nf.format(statistics.sumOfCounters()),
        nf.format(statistics.averageTps()),
        nf.format(statistics.totalAverageLatencyInMs())
    )).append(CRLF);
  }

  private String format(final long timestamp) {
    long timeInSec = timestamp / 1000;
    long second = timeInSec % 60;
    long minute = (timeInSec / 60) % 60;
    long hour = (timeInSec / 60 * 60) % 24;

    return String.format("%02d:%02d:%02d", hour, minute, second);
  }

}