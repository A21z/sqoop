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
package org.apache.sqoop.connector.spi;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.sqoop.model.MForm;

/**
 * Service provider interface for Sqoop Connectors.
 */
public interface SqoopConnector {

  /**
   * @param locale
   * @return the resource bundle associated with the given locale.
   */
  public ResourceBundle getBundle(Locale locale);

  /**
   * @return a list of <tt>MForm</tt> that provide metadata about input needed
   * by Sqoop to create a connection object using this connector.
   */
  public List<MForm> getConnectionForms();


  /**
   * @return a list of <tt>MForm</tt> that provide metadata about input needed
   * by Sqoop to create a job object using this connector.
   */
  public List<MForm> getJobForms();
}
