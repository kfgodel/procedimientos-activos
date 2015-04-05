App.ProceduresController = Ember.ArrayController.extend({
  needs: ["login"],
  actions: {
    createProcedure: function() {
      var newRecord = this.store.createRecord('procedure', {});
      newRecord.save();
      this.transitionToRoute('procedures.edit', newRecord)
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