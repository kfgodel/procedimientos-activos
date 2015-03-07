App.UsersEditController = Ember.ObjectController.extend({
  actions: {
    save: function() {
      this.get('model').save();
    },
    remove: function(){
      var model = this.get('model');
      model.deleteRecord();
      model.save();
    }
  }
});