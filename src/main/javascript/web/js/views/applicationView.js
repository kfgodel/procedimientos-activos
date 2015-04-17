/**
 * Root view of all the application nested views tree.
 * This view is created to add a custom 'container-fluid' class in the root view.
 * Details for the bootstrap 'container': http://www.helloerik.com/the-subtle-magic-behind-why-the-bootstrap-3-grid-works
 *
 * A custom application.hbs template is also needed (otherwise the application doesn't render sub-views)
 */
App.ApplicationView = Ember.View.extend({
  classNames: ['container-fluid'],
  templateName: 'views/empty-container'
});