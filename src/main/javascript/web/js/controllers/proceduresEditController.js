App.ProceduresEditController = Ember.ObjectController.extend({
  actions: {
    saveProcedure: function() {
      this.get('model').save();
    },
    deleteProcedure: function(){
      var model = this.get('model');
      model.deleteRecord();
      model.save();
    }
  }
});