App.Router.map(function() {

  this.route('login');

  this.resource('users', function(){
    this.route('edit', { path: "edit/:user_id" });
  });

  this.resource('procedures', function () {
    this.route('view', {path: "view/:procedure_id"});
    this.route('edit', {path: "edit/:procedure_id"});
  });

  // Catches all the malformed urls (not matching previous routes)
  this.route('wrongPaths', { path: '/*wrongPath' });
});


App.ApplicationRoute = Ember.Route.extend({
  actions: {
    /**
     * This error handler can recover from unauthenticated requests while transitioning to a route.
     * It tries to authenticate first, and then will retry the transition.
     * Helpful for non authenticated routes that make authenticated requests
     */
    error: function(error, transition) {
      if(error.status == 401){
        //It's an authentication problem? Try to authenticate first
        var loginController = this.controllerFor('login');
        loginController.set('previousTransition', transition);
        this.transitionTo('login');
      }else{
        // For other error, just log it
        console.log("Route error, transition:");
        console.log(error);
        console.log(transition)
        return this._super(error, transition);
      }
    }
  }
});

/**
 * This Mixin adds a pre-transition step to authenticate the user if not authenticated yet.<br>
 *   This prevents accessing to routes where requests will fail if not authenticated.
 *   However, this doesn't help recovering the session if lost, once in.
 */
App.AuthenticatedRoute = Ember.Mixin.create({
  beforeModel: function(transition) {
    var loginController = this.controllerFor('login');
    if (!loginController.get('authenticated')) {
      loginController.set('previousTransition', transition);
      this.transitionTo('login');
    }
  }
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

App.UsersRoute = Ember.Route.extend(App.AuthenticatedRoute, {
  model: function(){
    return this.store.find('user');
  }
});

App.UsersEditRoute = Ember.Route.extend(App.AuthenticatedRoute, {
  model: function(params){
    return this.store.findById('user', params.user_id);
  }
});

App.ProceduresRoute = Ember.Route.extend(App.AuthenticatedRoute, {
  model: function(){
    return this.store.find('procedure');
  }
});