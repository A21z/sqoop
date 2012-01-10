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
package org.apache.sqoop.repository.derby;

import org.apache.sqoop.core.ErrorCode;

public enum DerbyRepoError implements ErrorCode {

  /** An unknown error has occurred. */
  DERBYREPO_0000("An unknown error has occurred"),

  /** The Derby Repository handler was unable to determine if schema exists.*/
  DERBYREPO_0001("Unable to determine if schema exists"),

  /** The system was unable to shutdown embedded derby repository server. */
  DERBYREPO_0002("Unable to shutdown embedded  Derby instance"),

  /** The system was unable to run the specified query. */
  DERBYREPO_0003("Unable to run specified query"),

  /** The system was unable to query the repository for connector metadata. */
  DERBYREPO_0004("Unable to retrieve connector metadata"),

  /** The metadata repository contains more than one connector with same name */
  DERBYREPO_0005("Invalid metadata state - multiple connectors with name"),

  /** The system does not support the given input type.*/
  DERBYREPO_0006("Unknown input type encountered"),

  /** The system does not support the given form type.*/
  DERBYREPO_0007("Unknown form type encountered"),

  /** No input metadata was found for the given form. */
  DERBYREPO_0008("The form contains no input metadata"),

  /** The system could not load the form due to unexpected position of input.*/
  DERBYREPO_0009("The form input retrieved does not match expected position"),

  /**
   * The system could not load the connector due to unexpected position
   * of form.
   */
  DERBYREPO_0010("The form retrieved does not match expteced position"),

  /**
   * The system was not able to register connector metadata due to a
   * pre-assigned persistence identifier.
   */
  DERBYREPO_0011("Connector metadata cannot have preassigned persistence id"),

  /**
   * The system was unable to register connector metadata due to an unexpected
   * update count.
   */
  DERBYREPO_0012("Unexpected update count on connector registration"),

  /**
   * The system was unable to register connector metadata due to a failure to
   * retrieve the generated identifier.
   */
  DERBYREPO_0013("Unable to retrieve generated identifier for new connector"),

  /**
   * The system was unable to register connector metadata due to a server
   * error.
   */
  DERBYREPO_0014("Registration of connector metadata failed"),

  /**
   * The system was not able to register connector metadata due to an unexpected
   * update count.
   */
  DERBYREPO_0015("Unexpected update count on form registration"),

  /**
   * The system was unable to register connector metadata due to a failure to
   * retrieve the generated identifier for a form.
   */
  DERBYREPO_0016("Unable to retrieve generated identifier for form"),

  /**
   * The system was unable to register connector metadata due to an unexpected
   * update count for form input registration.
   */
  DERBYREPO_0017("Unexpected update count for form input"),

  /**
   * The system was unable to register connector metadata due to a failure to
   * retrieve the generated identifier for a form input.
   */
  DERBYREPO_0018("Unable to retrieve generated identifier for form input");


  private final String message;

  private DerbyRepoError(String message) {
    this.message = message;
  }

  public String getCode() {
    return name();
  }

  public String getMessage() {
    return message;
  }
}
