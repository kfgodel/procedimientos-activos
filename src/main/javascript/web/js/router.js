App.Router.map(function() {
  // Make users the default route
  this.resource('users', { path: '/' }, function(){
    this.route('edit', { path: "user/:user_id" });
  });
});

App.UsersRoute = Ember.Route.extend({
  model: function(){
    return this.store.find('user');
  }
});

App.UserEditRoute = Ember.Route.extend({
  model: function(params){
    return this.store.findById('user', params.user_id);
  }
});