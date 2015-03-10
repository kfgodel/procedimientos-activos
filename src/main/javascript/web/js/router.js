App.Router.map(function() {
  this.route('login');
  this.resource('users', function(){
    this.route('edit', { path: "edit/:user_id" });
  });
  this.route('otherPaths', { path: '/*missingPath' });
});

App.IndexRoute = Ember.Route.extend({
  beforeModel: function() {
    this.transitionTo('users');
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
  model: function(params){
    return this.store.findById('user', params.user_id);
  }
});