App.ProceduresController = Ember.ArrayController.extend({
  //queryParams: ['nameOrDescription'],
  nameOrDescription: "",
  actions: {
    createProcedure: function() {
      var newRecord = this.store.createRecord('procedure', {});
      newRecord.save();
      this.transitionToRoute('procedures.edit', newRecord)
    }
  },

  filteredProcedures: function() {
    var nameOrDescription = this.get('nameOrDescription');
    var procedures = this.get('model');

    if (!nameOrDescription) {
      return procedures;
    }
    return procedures.filter(function(item, index, enumerable){
      var foundInName = item.get("name").indexOf(nameOrDescription) > -1;
      var foundInDescription = item.get("description").indexOf(nameOrDescription) > -1;
      return foundInName || foundInDescription;
    });
  }.property('model', 'nameOrDescription')
});