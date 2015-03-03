App.Router.map(function() {
  // Make users the default route
  this.route('users', { path: '/' });
});

App.UsersRoute = Ember.Route.extend({
  model: function(){
    return this.store.find('user');
  }
});