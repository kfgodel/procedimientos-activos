App.ProceduresEditController = Ember.ObjectController.extend({
  actions: {
    saveProcedure: function() {
      var procedure = this.get('model');
      procedure.save();
      this.transitionToRoute('procedures.view', procedure );
    },
    deleteProcedure: function(){
      var model = this.get('model');
      model.destroyRecord();
      this.transitionToRoute('procedures')
    },
    cancelEdition: function(){
      var procedure = this.get('model');
      this.transitionToRoute('procedures.view', procedure);
    }
  }
});