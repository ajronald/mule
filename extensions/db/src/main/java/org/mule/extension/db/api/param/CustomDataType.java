/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.db.api.param;

import org.mule.runtime.extension.api.annotation.Parameter;
import org.mule.runtime.extension.api.annotation.param.Optional;

/**
 * A user defined JDBC type
 *
 * @since 4.0
 */
public class CustomDataType {

  /**
   * Type identifier used by the JDBC driver.
   */
  @Parameter
  private int id;

  /**
   * Name of the data type used by the JDBC driver.
   */
  @Parameter
  private String typeName;

  /**
   * Indicates which Java class must be used to map the DB type.
   */
  @Parameter
  @Optional
  private String className;

  public int getId() {
    return id;
  }

  public String getTypeName() {
    return typeName;
  }

  public String getClassName() {
    return className;
  }
}
