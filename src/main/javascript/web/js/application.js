// Create the app
window.App = Ember.Application.create({
 LOG_TRANSITIONS: true,
 LOG_TRANSITIONS_INTERNAL: true // Log route hooks and transition
});

// Rest for the model store
App.ApplicationAdapter = DS.RESTAdapter.extend({
  namespace: 'api/v1',
  /**
   * This error handler is used specially for ember-data authentication problems
   */
  ajaxError: function(jqXHR) {
    var error = this._super(jqXHR);
    if (jqXHR && jqXHR.status === 401) {
      console.log("Authentication error");
    }else{
      console.log("Rest error: " + jqXHR.status);
      console.log(jqXHR);
    }
    return error;
  }
});

// Change default JSON format to avoid root object name
// Makes backend aware of frontend type names
App.ApplicationSerializer = DS.RESTSerializer.extend({
  extractArray: function(store, type, payload) {
    var payloadTemp = {};
    payloadTemp[type.typeKey] = payload;
    return this._super(store, type, payloadTemp);
  },
  extractSingle: function(store, type, payload, id) {
    var payloadTemp = {};
    payloadTemp[type.typeKey] = [payload];
    return this._super(store, type, payloadTemp, id);
  },
  serializeIntoHash: function(hash, type, snapshot, options) {
    var serialized = this.serialize(snapshot, options);
    serialized["id"] = snapshot.id;
    Ember.$.extend(hash, serialized);
  }
});