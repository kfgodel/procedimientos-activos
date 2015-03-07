App.Router.map(function() {
  this.resource('users', function(){
    this.route('edit', { path: "edit/:user_id" });
  });
  this.route('otherPaths', { path: '/*missingPath' });
});

App.IndexRoute = Ember.Route.extend({
  beforeModel: function() {
    // by default move to users route
    this.transitionTo('users');
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