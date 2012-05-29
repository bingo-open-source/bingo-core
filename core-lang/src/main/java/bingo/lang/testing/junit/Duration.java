/**
 * Copyright (C) 2010 Mycila <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bingo.lang.testing.junit;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Duration implements TestRule {

	public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long time = System.currentTimeMillis();
                try {
                    base.evaluate();
                } finally {
                    time = System.currentTimeMillis() - time;
                    if (description.isTest()) {
                        System.out.println(String.format("[%s] [%s] %s#%s - %s ms",
                            Duration.this.getClass().getSimpleName(),
                            Thread.currentThread().getName(),
                            description.getTestClass().getName(),
                            description.getMethodName(),
                            time));
                    } else {
                        System.out.println(String.format("[%s] [%s] %s - %s ms",
                            Duration.this.getClass().getSimpleName(),
                            Thread.currentThread().getName(),
                            description.getTestClass().getName(),
                            time));
                    }
                }
            }
        };
    }
}
