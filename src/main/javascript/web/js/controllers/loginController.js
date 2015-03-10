App.LoginController = Ember.Controller.extend({
  actions: {
    logIn: function() {
      var self = this;
      Ember.$.post("/api/v1/users/login", this.get('model'),'json').then(
        function(response) {
          self.set('authenticated', response.user);

          // Continue with previous transition if any
          var previousTransition = self.get('previousTransition');
          if (previousTransition) {
            self.set('previousTransition', null);
            previousTransition.retry();
          } else {
            // Default back to homepage
            self.transitionToRoute('index');
          }
        },
        function(response){
          self.set('rejected', true);
        }
      );
    }
  }
});