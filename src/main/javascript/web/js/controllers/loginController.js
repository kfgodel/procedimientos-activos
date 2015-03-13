App.LoginController = Ember.Controller.extend({
  actions: {
    logIn: function() {
      var self = this;
      var namespace = this.store.adapterFor('application').namespace;

      Ember.$.ajax({
        type:"POST",
        url:namespace + "/users/login",
        data:JSON.stringify(this.get('model')),
        contentType:"application/json; charset=utf-8",
        dataType:"json"
      }).then(
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