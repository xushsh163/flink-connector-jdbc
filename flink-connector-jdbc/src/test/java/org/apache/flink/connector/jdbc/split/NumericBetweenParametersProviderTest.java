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
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.connector.jdbc.split;

import org.junit.Test;

import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;

/** Tests for {@link JdbcNumericBetweenParametersProvider}. */
public class NumericBetweenParametersProviderTest {

    @Test
    public void testBatchSizeDivisible() {
        JdbcNumericBetweenParametersProvider provider =
                new JdbcNumericBetweenParametersProvider(-5, 9).ofBatchSize(3);
        Serializable[][] actual = provider.getParameterValues();

        long[][] expected = {
            new long[] {-5, -3},
            new long[] {-2, 0},
            new long[] {1, 3},
            new long[] {4, 6},
            new long[] {7, 9}
        };
        check(expected, actual);
    }

    @Test
    public void testBatchSizeNotDivisible() {
        JdbcNumericBetweenParametersProvider provider =
                new JdbcNumericBetweenParametersProvider(-5, 11).ofBatchSize(4);
        Serializable[][] actual = provider.getParameterValues();

        long[][] expected = {
            new long[] {-5, -2},
            new long[] {-1, 2},
            new long[] {3, 5},
            new long[] {6, 8},
            new long[] {9, 11}
        };
        check(expected, actual);
    }

    @Test
    public void testBatchSizeTooLarge() {
        JdbcNumericBetweenParametersProvider provider =
                new JdbcNumericBetweenParametersProvider(0, 2).ofBatchSize(5);
        Serializable[][] actual = provider.getParameterValues();

        long[][] expected = {new long[] {0, 2}};
        check(expected, actual);
    }

    @Test
    public void testBatchNumDivisible() {
        JdbcNumericBetweenParametersProvider provider =
                new JdbcNumericBetweenParametersProvider(-5, 9).ofBatchNum(5);
        Serializable[][] actual = provider.getParameterValues();

        long[][] expected = {
            new long[] {-5, -3},
            new long[] {-2, 0},
            new long[] {1, 3},
            new long[] {4, 6},
            new long[] {7, 9}
        };
        check(expected, actual);
    }

    @Test
    public void testBatchNumNotDivisible() {
        JdbcNumericBetweenParametersProvider provider =
                new JdbcNumericBetweenParametersProvider(-5, 11).ofBatchNum(5);
        Serializable[][] actual = provider.getParameterValues();

        long[][] expected = {
            new long[] {-5, -2},
            new long[] {-1, 2},
            new long[] {3, 5},
            new long[] {6, 8},
            new long[] {9, 11}
        };
        check(expected, actual);
    }

    @Test
    public void testBatchNumTooLarge() {
        JdbcNumericBetweenParametersProvider provider =
                new JdbcNumericBetweenParametersProvider(0, 2).ofBatchNum(5);
        Serializable[][] actual = provider.getParameterValues();

        long[][] expected = {
            new long[] {0, 0},
            new long[] {1, 1},
            new long[] {2, 2}
        };
        check(expected, actual);
    }

    private void check(long[][] expected, Serializable[][] actual) {
        assertThat(actual).hasDimensions(expected.length, expected[0].length);
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].length; j++) {
                assertThat(((Long) actual[i][j]).longValue()).isEqualTo(expected[i][j]);
            }
        }
    }
}
