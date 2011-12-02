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
package org.apache.sqoop.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public final class SqoopConfiguration {

  public static final Logger LOG = Logger.getLogger(SqoopConfiguration.class);

  private static File configDir = null;
  private static boolean initialized = false;
  private static ConfigurationProvider provider = null;
  private static Map<String, String> config = null;

  public synchronized static void initialize() {
    if (initialized) {
      LOG.warn("Attempt to reinitialize the system, ignoring");
      return;
    }

    String configDirPath = System.getProperty(
        ConfigurationConstants.SYSPROP_CONFIG_DIR);
    if (configDirPath == null || configDirPath.trim().length() == 0) {
      throw new SqoopException(CoreError.CORE_0001, "Environment variable "
          + ConfigurationConstants.SYSPROP_CONFIG_DIR + " is not set.");
    }

    configDir = new File(configDirPath);
    if (!configDir.exists() || !configDir.isDirectory()) {
      throw new SqoopException(CoreError.CORE_0001, configDirPath);
    }

    String bootstrapConfigFilePath = null;
    try {
      String configDirCanonicalPath = configDir.getCanonicalPath();
      bootstrapConfigFilePath = configDirCanonicalPath
              + "/" + ConfigurationConstants.FILENAME_BOOTCFG_FILE;

    } catch (IOException ex) {
      throw new SqoopException(CoreError.CORE_0001, configDirPath, ex);
    }

    File bootstrapConfig = new File(bootstrapConfigFilePath);
    if (!bootstrapConfig.exists() || !bootstrapConfig.isFile()
        || !bootstrapConfig.canRead()) {
      throw new SqoopException(CoreError.CORE_0002, bootstrapConfigFilePath);
    }

    Properties bootstrapProperties = new Properties();
    InputStream bootstrapPropStream = null;
    try {
      bootstrapPropStream = new FileInputStream(bootstrapConfig);
      bootstrapProperties.load(bootstrapPropStream);
    } catch (IOException ex) {
      throw new SqoopException(
          CoreError.CORE_0002, bootstrapConfigFilePath, ex);
    }

    String configProviderClassName = bootstrapProperties.getProperty(
        ConfigurationConstants.BOOTCFG_CONFIG_PROVIDER);

    if (configProviderClassName == null
        || configProviderClassName.trim().length() == 0) {
      throw new SqoopException(
          CoreError.CORE_0003, ConfigurationConstants.BOOTCFG_CONFIG_PROVIDER);
    }

    Class<?> configProviderClass = null;
    try {
      configProviderClass = Class.forName(configProviderClassName);
    } catch (ClassNotFoundException cnfe) {
      LOG.warn("Exception while trying to load configuration provider", cnfe);
    }

    if (configProviderClass == null) {
      ClassLoader ctxLoader = Thread.currentThread().getContextClassLoader();
      if (ctxLoader != null) {
        try {
          configProviderClass = ctxLoader.loadClass(configProviderClassName);
        } catch (ClassNotFoundException cnfe) {
          LOG.warn("Exception while trying to load configuration provider: "
              + configProviderClassName, cnfe);
        }
      }
    }

    if (configProviderClass == null) {
      throw new SqoopException(CoreError.CORE_0004, configProviderClassName);
    }

    try {
      provider = (ConfigurationProvider) configProviderClass.newInstance();
    } catch (Exception ex) {
      throw new SqoopException(CoreError.CORE_0005,
          configProviderClassName, ex);
    }

    // Initialize the configuration provider
    provider.initialize(configDir, bootstrapProperties);
    refreshConfiguration();
    provider.registerListener(new CoreConfigurationListener());

    initialized = true;
  }

  public synchronized static Context getContext() {
    if (!initialized) {
      throw new SqoopException(CoreError.CORE_0007);
    }

    Map<String,String> parameters = new HashMap<String, String>();
    parameters.putAll(config);

    return new Context(parameters);
  }

  private synchronized static void configureLogging() {
    Properties props = new Properties();
    for (String key : config.keySet()) {
      if (key.startsWith(ConfigurationConstants.PREFIX_LOG_CONFIG)) {
        String logConfigKey = key.substring(
            ConfigurationConstants.PREFIX_GLOBAL_CONFIG.length());
        props.put(logConfigKey, config.get(key));
      }
    }

    PropertyConfigurator.configure(props);
  }

  private synchronized static void refreshConfiguration()
  {
    config = provider.getConfiguration();
    configureLogging();
  }

  private SqoopConfiguration() {
    // Disable explicit object creation
  }

  public static class CoreConfigurationListener implements ConfigurationListener
  {
    @Override
    public void configurationChanged() {
      refreshConfiguration();
    }
  }
}
