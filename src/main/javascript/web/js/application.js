// Create the app
window.App = Ember.Application.create({
 LOG_TRANSITIONS: true
 // LOG_TRANSITIONS_INTERNAL: true // Log route hooks and transition
});

// Use localstorage as datasource
App.ApplicationAdapter = DS.FixtureAdapter.extend();