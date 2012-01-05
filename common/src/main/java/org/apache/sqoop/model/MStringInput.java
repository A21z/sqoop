/**
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
package org.apache.sqoop.model;

import org.apache.sqoop.utils.UrlSafeUtils;

/**
 * Represents a <tt>String</tt> input. The boolean flag <tt>mask</tt> supplied
 * to its constructor can be used to indicate if the string should be masked
 * from user-view. This is helpful for creating input strings that represent
 * sensitive information such as passwords.
 */
public final class MStringInput extends MInput<String> {

  private final boolean mask;
  private final int maxLength;

  /**
   * @param name the parameter name
   * @param label the parameter label
   * @param mask a flag indicating if the string should be masked
   * @param maxLength the maximum length of the string
   */
  public MStringInput(String name, boolean mask, int maxLength) {
    super(name);
    this.mask = mask;
    this.maxLength = maxLength;
  }

  /**
   * @return <tt>true</tt> if this string represents sensitive information that
   * should be masked
   */
  public boolean isMasked() {
    return mask;
  }

  /**
   * @return the maximum length of this string type
   */
  public int getMaxLength() {
    return maxLength;
  }

  @Override
  public String getUrlSafeValueString() {
    return UrlSafeUtils.urlEncode(getValue());
  }

  @Override
  public void restoreFromUrlSafeValueString(String valueString) {
    setValue(UrlSafeUtils.urlDecode(valueString));
  }
}
