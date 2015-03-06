App.UsersEditController = Ember.ObjectController.extend({
  actions: {
    save: function() {
      this.get('model').save();
    }
  }
});