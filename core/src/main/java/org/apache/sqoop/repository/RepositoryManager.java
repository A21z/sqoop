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

import org.apache.log4j.Logger;
import org.apache.sqoop.core.ConfigurationConstants;
import org.apache.sqoop.core.Context;
import org.apache.sqoop.core.CoreError;
import org.apache.sqoop.core.SqoopConfiguration;
import org.apache.sqoop.core.SqoopException;
import org.apache.sqoop.utils.ClassLoadingUtils;

public final class RepositoryManager {

  private static final Logger LOG = Logger.getLogger(RepositoryManager.class);

  private static RepositoryProvider provider;

  public synchronized static void initialize() {
    Context context = SqoopConfiguration.getContext();

    String repoProviderClassName = context.getString(
        RepoConfigurationConstants.SYSCFG_REPO_PROVIDER);

    if (repoProviderClassName == null
        || repoProviderClassName.trim().length() == 0) {
      throw new SqoopException(RepositoryError.REPO_0001,
          RepoConfigurationConstants.SYSCFG_REPO_PROVIDER);
    }

    if (LOG.isTraceEnabled()) {
      LOG.trace("Repository provider: " + repoProviderClassName);
    }

    Class<?> repoProviderClass =
        ClassLoadingUtils.loadClass(repoProviderClassName);

    if (repoProviderClass == null) {
      throw new SqoopException(RepositoryError.REPO_0001,
          repoProviderClassName);
    }

    try {
      provider = (RepositoryProvider) repoProviderClass.newInstance();
    } catch (Exception ex) {
      throw new SqoopException(RepositoryError.REPO_0001,
          repoProviderClassName, ex);
    }

    provider.initialize(context);

    LOG.info("Repository initialized: OK");
  }

  public synchronized Repository getRepository() {
    return provider.getRepository();
  }
}
