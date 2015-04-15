App.UsersController = Ember.ArrayController.extend({
  needs: ["login"],
  actions: {
    create: function() {
      var newUser = this.store.createRecord('user', {});
      newUser.save();
    }
  }
});