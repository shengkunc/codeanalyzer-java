/**
 * (C) Copyright IBM Corporation 2019.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.websphere.samples.daytrader.web.prims.beanval;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;

public class SimpleBean2 extends SimpleBean1 {

  private List<@PositiveOrZero Integer> numbers= new ArrayList<Integer>();
  private List<@NotBlank String> strings = new ArrayList<String>();

  @PastOrPresent
  LocalDateTime now = LocalDateTime.now();

  @FutureOrPresent
  LocalDateTime future = LocalDateTime.now().plusDays(1);

  public SimpleBean2() throws Exception {
    super();

    numbers.add(1);
    numbers.add(2);

    strings.add("string1");
    strings.add("string2");
  }
}
