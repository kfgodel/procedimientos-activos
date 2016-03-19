package ar.com.kfgodel.proact.persistent.filters.medicamentos;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.api.optionals.Optional;
import ar.com.tenpines.orm.api.SessionContext;
import ar.com.tenpines.orm.api.operations.SessionOperation;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import convention.persistent.Medicamento;
import convention.persistent.QMedicamento;

import java.util.List;

/**
 * This type represents a filter that fetches all the medicamentos ordered by name
 * filtering any field by a text piece
 * Created by kfgodel on 04/04/15.
 */
public class MedicamentosByTextPortionOrdByName implements SessionOperation<Nary<Medicamento>> {

  private Optional<String> filterText;

  public static MedicamentosByTextPortionOrdByName create(Optional<String> filterText) {
    MedicamentosByTextPortionOrdByName filter = new MedicamentosByTextPortionOrdByName();
    filter.filterText = filterText;
    return filter;
  }

  @Override
  public Nary<Medicamento> applyWithSessionOn(SessionContext sessionContext) {
    QMedicamento medicamento = QMedicamento.medicamento;
    HibernateQuery query = new HibernateQuery(sessionContext.getSession())
      .from(medicamento);

    filterText.ifPresent((textToRestrict)->{
      query.where(
        medicamento.name.containsIgnoreCase(textToRestrict)
          .or(medicamento.description.containsIgnoreCase(textToRestrict))
          .or(medicamento.tags.containsIgnoreCase(textToRestrict))
      );
    });

    List<Medicamento> foundMedicamentos = query
      .orderBy(medicamento.name.asc())
      .list(medicamento);
    return Nary.create(foundMedicamentos);
  }
}
