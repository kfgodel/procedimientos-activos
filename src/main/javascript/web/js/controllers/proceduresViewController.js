App.ProceduresViewController = Ember.ObjectController.extend({
  actions: {
    editProcedure : function (procedure) {
      this.transitionToRoute('procedures.edit', procedure)
    },
    deleteProcedure: function (procedure) {
      procedure.destroyRecord();
      this.transitionToRoute('procedures')
    }
  }
});