App.UsersController = Ember.ArrayController.extend({
  actions: {
    create: function() {
      var newUser = this.store.createRecord('user', {});
      newUser.save();
    }
  }
});