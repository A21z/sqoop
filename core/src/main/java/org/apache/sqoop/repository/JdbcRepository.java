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
package org.apache.sqoop.repository;

import java.sql.Connection;

import org.apache.log4j.Logger;
import org.apache.sqoop.connector.ConnectorHandler;
import org.apache.sqoop.core.SqoopException;
import org.apache.sqoop.model.MConnector;

public class JdbcRepository implements Repository {

  private static final Logger LOG =
      Logger.getLogger(JdbcRepository.class);

  private final JdbcRepositoryHandler handler;
  private final JdbcRepositoryContext repoContext;

  protected JdbcRepository(JdbcRepositoryHandler handler,
      JdbcRepositoryContext repoContext) {
    this.handler = handler;
    this.repoContext = repoContext;
  }

  @Override
  public JdbcRepositoryTransaction getTransaction() {
    return repoContext.getTransactionFactory().get();
  }

  @Override
  public MConnector registerConnector(MConnector mConnector) {
    MConnector result = null;
    JdbcRepositoryTransaction tx = null;
    String connectorUniqueName = mConnector.getUniqueName();
    try {
      tx = getTransaction();
      tx.begin();
      Connection conn = tx.getConnection();
      result = handler.findConnector(connectorUniqueName, conn);
      if (result == null) {
        // Insert (Register) connector FIXME


      }
      tx.commit();
    } catch (Exception ex) {
      if (tx != null) {
        tx.rollback();
      }
      if (ex instanceof SqoopException) {
        throw (SqoopException) ex;
      }
      throw new SqoopException(RepositoryError.JDBCREPO_0012,
          mConnector.toString(), ex);
    } finally {
      if (tx != null) {
        tx.close();
      }
    }

    return result;
  }
}
