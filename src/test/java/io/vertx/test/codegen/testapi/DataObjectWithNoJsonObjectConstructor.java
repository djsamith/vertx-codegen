package io.vertx.test.codegen.testapi;

import io.vertx.codegen.annotations.DataObject;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
public class DataObjectWithNoJsonObjectConstructor {

  public DataObjectWithNoJsonObjectConstructor() {
  }

  public DataObjectWithNoJsonObjectConstructor(DataObjectWithNoJsonObjectConstructor other) {
  }
}
