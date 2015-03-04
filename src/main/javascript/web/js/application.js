// Create the app
window.App = Ember.Application.create({
 LOG_TRANSITIONS: true
 // LOG_TRANSITIONS_INTERNAL: true // Log route hooks and transition
});

// Rest for the model store
App.ApplicationAdapter = DS.RESTAdapter.extend({
  namespace: 'api/v1'
});