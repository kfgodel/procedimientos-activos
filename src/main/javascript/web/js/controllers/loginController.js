App.LoginController = Ember.ObjectController.extend({
  actions: {
    logIn: function() {
      return Ember.$.post("/api/v1/users/login", this.get('model'),'json').then(
        function(response) {
          console.log(response.data);
          return response.data;
        }
      );
    }
  }
});