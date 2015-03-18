App.ProceduresController = Ember.ArrayController.extend({
  actions: {
    createProcedure: function() {
      var newRecord = this.store.createRecord('procedure', {});
      newRecord.save();
    }
  }
});