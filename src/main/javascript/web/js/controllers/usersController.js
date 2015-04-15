App.UsersController = Ember.ArrayController.extend({
  needs: ["login"],
  actions: {
    create: function() {
      var newUser = this.store.createRecord('user', {});
      newUser.save();
    },
    logout: function() {
      var self = this;
      Ember.$.post("/j_logout", {}).then(
        function(response) {
          var loginController = self.get('controllers.login');
          loginController.set('authenticated', null);
          self.transitionToRoute('login');
        },
        function(response){
          console.log("Error logging out");
          console.log(response);
        }
      );
    }
  }
});