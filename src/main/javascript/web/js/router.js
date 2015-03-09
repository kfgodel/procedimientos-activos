App.Router.map(function() {
  this.route('login');
  this.resource('users', function(){
    this.route('edit', { path: "edit/:user_id" });
  });
  this.route('otherPaths', { path: '/*missingPath' });
});

App.IndexRoute = Ember.Route.extend({
  beforeModel: function() {
    this.transitionTo('login');
  }
});

App.UsersRoute = Ember.Route.extend({
  model: function(){
    return this.store.find('user');
  }
});

App.UsersEditRoute = Ember.Route.extend({
  model: function(params){
    return this.store.findById('user', params.user_id);
  }
});