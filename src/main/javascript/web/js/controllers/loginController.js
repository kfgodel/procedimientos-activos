App.LoginController = Ember.Controller.extend({
  actions: {
    logIn: function() {
      var self = this;

      var credentials = this.get('model');
      return Ember.$.post("/j_security_check", {
          j_username: credentials.login,
          j_password: credentials.password })
        .then(
        function(response) {
          self.set('authenticated', true);

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
          var statusCode = response.status;
          var errorMessage;
          if(statusCode == 401){
            errorMessage = "Invalid credentials";
          }else{
            errorMessage = "Unknown error: " + statusCode + " - " + response.statusText;
          }
          self.set("errorMessage", errorMessage);
        }
      );
    }
  }
});