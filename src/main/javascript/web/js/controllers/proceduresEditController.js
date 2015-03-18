App.ProceduresEditController = Ember.ObjectController.extend({
  actions: {
    saveProcedure: function() {
      this.get('model').save();
      this.transitionToRoute('procedures')
    },
    deleteProcedure: function(){
      var model = this.get('model');
      model.destroyRecord();
      this.transitionToRoute('procedures')
    }
  }
});