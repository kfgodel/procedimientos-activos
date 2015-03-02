// Create the app
window.Todos = Ember.Application.create({
 LOG_TRANSITIONS: true
 // LOG_TRANSITIONS_INTERNAL: true // Log route hooks and transition
});

// Use localstorage as datasource
Todos.ApplicationAdapter = DS.LSAdapter.extend({
  namespace: 'todos-emberjs'
});