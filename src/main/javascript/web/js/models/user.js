App.User = DS.Model.extend({
  name: DS.attr('string'),
  login: DS.attr('string'),
  password: DS.attr('string')
});

// Mocked models
App.User.FIXTURES = [
 {
   id: 1,
   name: 'Pepe gonzola',
   login: 'pepe',
   password: '1234'
 },
  {
    id: 2,
    name: 'Administrator',
    login: 'admin',
    password: '1234'
  }
];