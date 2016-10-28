package convention.rest.api.tos;

import java.util.List;

/**
 * This type allows returning a list of objects as an object to standarize result types (only json object)
 * Created by kfgodel on 27/10/16.
 */
public class ListTo {

  private List list;

  public List getList() {
    return list;
  }

  public void setList(List list) {
    this.list = list;
  }

  public static ListTo create(List wrapped) {
    ListTo listTo = new ListTo();
    listTo.list = wrapped;
    return listTo;
  }

}
