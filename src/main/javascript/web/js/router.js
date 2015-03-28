App.Router.map(function() {

  this.route('login');

  this.resource('users', function(){
    this.route('edit', { path: "edit/:user_id" });
  });

  this.resource('procedures', function () {
    this.route('view', {path: "view/:procedure_id"});
    this.route('edit', {path: "edit/:procedure_id"});
  });

  this.route('wrongPaths', { path: '/*wrongPath' });
});

App.IndexRoute = Ember.Route.extend({
  beforeModel: function() {
    this.transitionTo('procedures');
  }
});

App.LoginRoute = Ember.Route.extend({
  model: function(){
    return {login:'',password:''};
  }
});

App.UsersRoute = Ember.Route.extend({
  beforeModel: function(transition) {
    var loginController = this.controllerFor('login');
    if (!loginController.get('authenticated')) {
      loginController.set('previousTransition', transition);
      this.transitionTo('login');
    }
  },
  model: function(){
    return this.store.find('user');
  }
});

App.UsersEditRoute = Ember.Route.extend({
  beforeModel: function(transition) {
    var loginController = this.controllerFor('login');
    if (!loginController.get('authenticated')) {
      loginController.set('previousTransition', transition);
      this.transitionTo('login');
    }
  },
  model: function(params){
    return this.store.findById('user', params.user_id);
  }
});

App.ProceduresRoute = Ember.Route.extend({
  model: function(){
    return this.store.find('procedure');
  },
  actions: {
    logout: function() {
      Ember.$.post("/j_logout", {}).then(
        function(response) {
          self.set('authenticated', null);
          this.transitionTo('index');
        },
        function(response){
          self.set('rejected', true);
        }
      );
    }
  }
});