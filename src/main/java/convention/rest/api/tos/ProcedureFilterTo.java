package convention.rest.api.tos;

import com.google.common.base.MoreObjects;

/**
 * This type represents the search criteria that can be used to filter procedures
 * Created by kfgodel on 27/10/16.
 */
public class ProcedureFilterTo {

  private String searchText;
  public static final String searchText_FIELD = "searchText";


  public String getSearchText() {
    return searchText;
  }

  public void setSearchText(String searchText) {
    this.searchText = searchText;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add(searchText_FIELD, searchText)
      .toString();
  }
}
