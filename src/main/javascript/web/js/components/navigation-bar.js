App.NavigationBarComponent = Ember.Component.extend({
    action:'logout',
    actions: {
        logout: function() {
            //Send the default action that is 'logout'
            this.sendAction('action');
        }
    }
});