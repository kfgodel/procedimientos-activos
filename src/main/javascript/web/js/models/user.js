App.User = DS.Model.extend({
  name: DS.attr('string'),
  login: DS.attr('string'),
  password: DS.attr('string')
});
