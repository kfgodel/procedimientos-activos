App.LoginController = Ember.ObjectController.extend({
  actions: {
    logIn: function() {
      var self = this;
      Ember.$.post("/api/v1/users/login", this.get('model'),'json').then(
        function(response) {
          self.set('authenticated', response.user);
          self.transitionToRoute('users');
        },
        function(response){
          self.set('rejected', true);
        }
      );
    }
  }
});